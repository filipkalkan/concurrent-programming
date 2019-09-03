#include <pthread.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "client_handler.h"
#include "connection.h"
#include "fail.h"
#include "msg_store.h"

// ----------------------------------------------------------------------------

#define PACKET_LOGIN            'L'
#define PACKET_CREATE_TOPIC     'C'
#define PACKET_SELECT_TOPIC     'S'
#define PACKET_POST_MESSAGE     'P'
#define PACKET_RESTART          'R'
#define PACKET_LOGOUT           'O'

#define PACKET_PUBLISH_TOPIC    "T %d %d %s %s\n"
#define PACKET_PUBLISH_MESSAGE  "M %d %d %s %s\n"
#define PACKET_LOGGED_OUT       "O\n"

#define CONNECTION_BUFFER_SIZE    (1024)

// ----------------------------------------------------------------------------
// Local function: thread for sending messages

static void *
client_writer_thread_main(void *arg)
{
  struct connection *conn    = (struct connection *) arg;
  struct msg_store *store    = connection_get_msg_store(conn);
  struct client_state *state = connection_get_client_state(conn);

  while (true) {
    char *username;
    char *text;
    int topic_id;
    int nbr_messages;
    int message_id;

    switch (msg_store_await_message_or_topic(store, state)) {

      case TOPIC_STATE_LOGOUT_REQUESTED:
        connection_send(conn, PACKET_LOGGED_OUT);
        // fall-through to DISCONNECTED below

      case TOPIC_STATE_DISCONNECTED:
        // this means the reader thread has already exited
        // we can now safely dispose of the entire connection
        connection_dispose(conn);
        pthread_exit(NULL);
        break;

      default:
        if (msg_store_poll_topic(store, state,
                                 &topic_id, &nbr_messages, &username, &text))
        {
          connection_send(conn, PACKET_PUBLISH_TOPIC,
                          topic_id, nbr_messages, username, text);
        }

        if (msg_store_poll_message(store, state,
                                   &topic_id, &message_id, &username, &text))
        {
          connection_send(conn, PACKET_PUBLISH_MESSAGE,
                          topic_id, message_id, username, text);
        }
    }
  }
}

// ----------------------------------------------------------------------------
// Local function: thread for receiving messages

static void *
client_reader_thread_main(void *arg)
{
  struct connection *conn    = (struct connection *) arg;
  struct msg_store *store    = connection_get_msg_store(conn);
  struct client_state *state = connection_get_client_state(conn);

  char buffer[CONNECTION_BUFFER_SIZE];  // buffer for received data
  char *username = NULL;

  while (true) {
    bool still_connected = connection_receive(conn, buffer, sizeof(buffer));
    if (! still_connected) {
      printf("[%p] client '%s' disconnected\n", state, username);
      msg_store_select_topic(store, state, TOPIC_STATE_DISCONNECTED);
      if (username != NULL) {
        free(username);
      }
      pthread_exit(NULL);
    }

    char type = buffer[0];
    char *text = buffer + 1;
    switch (type)
    {
      case PACKET_LOGIN:
        printf("[%p] client '%s' connected\n", state, text);
        if (username != NULL) {
          fail("[%p] already logged in as '%s'\n", state, username);
        }
        username = strdup(text);

        // now start the writer thread
        pthread_t write_thread;
        int status = pthread_create(&write_thread,
                                    NULL,
                                    &client_writer_thread_main,
                                    conn);
        fail_if(status < 0, "pthread_create");
        break;

      case PACKET_LOGOUT:
        msg_store_select_topic(store, state, TOPIC_STATE_LOGOUT_REQUESTED);
        break;

      case PACKET_POST_MESSAGE:
        msg_store_add_message(store, state, username, text);
        break;

      case PACKET_CREATE_TOPIC:
        msg_store_add_topic(store, username, text);
        break;

      case PACKET_SELECT_TOPIC:
        msg_store_select_topic(store, state, atoi(text));
        break;

      case PACKET_RESTART:
        exit(EXIT_SUCCESS);
        break;

      default:
        fail("[%p] unexpected message type '%c'\n", state, type);
    }
  }
}

// ============================================================================

void
client_handler_launch(struct connection *conn)
{
  // Start the reader thread here. The writer thread is started in
  // client_reader_thread_main() above, upon receiving PACKET_LOGIN
  // or PACKET_LOGIN_DELETE_ALL.

  pthread_t read_thread;
  int status = pthread_create(&read_thread,
                              NULL,
                              &client_reader_thread_main,
                              conn);
  fail_if(status < 0, "pthread_create");
}
