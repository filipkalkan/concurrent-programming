//  -------------------------------------------------------------------------
//
//  msg_store:
//
//  Functions for storing and retrieving topics and messages. Every
//  message belongs to a particular topic.
//
//  Each client subscribes to one topic at a time. New topics are
//  always published to all clients. New messages are published to all
//  clients that subscribe to the topic the message belongs to.
//
//  -------------------------------------------------------------------------

struct msg_store;
struct client_state;                       // defined below

/**
 * Create a new message store.
 */
struct msg_store *
msg_store_create();

/**
 * Adds a new topic to the message store.  The topic will be published
 * to all connected clients.  Returns the ID of the newly created
 * topic.
 */
int
msg_store_add_topic(struct msg_store *store,
                    char *username,
                    char *text);

/**
 * Adds a new message to the topic currently subscribed to by
 * _client_.  The message will be published to all clients that
 * subscribe to this topic.
 */
void
msg_store_add_message(struct msg_store *store,
                      struct client_state *client,
                      char *username,
                      char *text);

/**
 * Retrieves the next topic that needs to be published to this client.
 * The result is stored in variables pointed to by parameters
 * _topic_id_, _nbr_messages, _username_, and _text_.  Returns true if
 * there was such a topic, false otherwise.
 *
 * This function does not block: it returns immediately if there are
 * no more topics.
 */
bool
msg_store_poll_topic(struct msg_store *store,
                     struct client_state *client,
                     int *topic_id,
                     int *nbr_messages,
                     char **username,
                     char **text);

/**
 * Retrieves the next message (within the current topic) that needs to
 * be published to this client.  The result is stored in variables
 * pointed to by parameters _topic_id_, _message_id_, _username_, and
 * _text_.  Returns true if there was such a message, false otherwise.
 *
 * This function does not block: it returns immediately if there are
 * no more messages.
 */
bool
msg_store_poll_message(struct msg_store *store,
                       struct client_state *client,
                       int *topic_id,
                       int *message_id,
                       char **username,
                       char **text);

/**
 * Initialize client_state for a particular client. The client is
 * initially set to not subscribe to any topic.
 */
void
msg_store_init_client(struct msg_store *store,
                      struct client_state *client);

/**
 * Set _client_ to subscribe to topid _topic_id_.
 */
void
msg_store_select_topic(struct msg_store *store,
                       struct client_state *client,
                       int topic_id);

/**
 * Waits until either a new message or a new topic is available for
 * _client_.
 *
 * Returns the client's reading state
 * (current topic ID, or one of the TOPIC_STATE constants below).
 */
int
msg_store_await_message_or_topic(struct msg_store *store,
                                 struct client_state *client);

// ----------------------------------------------------------------------------

/**
 * Struct for keeping track of what has been reported to a particular client.
 */
struct client_state {
  int current_topic_id;          // topic selected by this client
  int last_topic_published;      // id of last topic published to this client
  int last_message_published;    // id of last message published to this client
};

/** Special values for 'current_topic_id', to denote client state */
enum {
  TOPIC_STATE_NO_TOPIC         = -1,    // not subscribing to any topic
  TOPIC_STATE_LOGOUT_REQUESTED = -2,    // client has requested to log out
  TOPIC_STATE_DISCONNECTED     = -4     // client is disconnected
};
