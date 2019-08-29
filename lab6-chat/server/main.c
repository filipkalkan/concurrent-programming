#include <stdbool.h>
#include <stdio.h>
#include <unistd.h>

#include "client_handler.h"
#include "connection.h"
#include "server.h"

int main()
{
  server_start();

  while (true) {
    struct connection *connection_to_client = server_await_connection();
    client_handler_launch(connection_to_client);
  }
}
