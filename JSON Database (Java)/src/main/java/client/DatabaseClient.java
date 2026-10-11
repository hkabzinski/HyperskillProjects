package client;

import console.TextIO;

/**
 * Sends one request to the database server and reports what came back.
 */
public class DatabaseClient {
    static final String STARTED_MESSAGE = "Client started!";
    static final String SENT_PREFIX = "Sent: ";
    static final String RECEIVED_PREFIX = "Received: ";

    private final RequestSource requestSource;
    private final ServerConnection connection;
    private final TextIO io;

    public DatabaseClient(RequestSource requestSource, ServerConnection connection, TextIO io) {
        this.requestSource = requestSource;
        this.connection = connection;
        this.io = io;
    }

    public void sendOneRequest() {
        io.println(STARTED_MESSAGE);
        String request = requestSource.readRequest();
        io.println(SENT_PREFIX + request);
        io.println(RECEIVED_PREFIX + connection.exchange(request));
    }
}
