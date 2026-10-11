package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.ServerAddress;

class SocketServerConnectionTest {
    private static final int PORT_WITHOUT_A_SERVER = 24681;
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String OK_RESPONSE_WITH_THE_NAME = "{\"response\":\"OK\",\"value\":\"Sorabh\"}";

    private SingleRequestServer server;

    @BeforeEach
    void startServer() throws IOException {
        server = new SingleRequestServer(OK_RESPONSE_WITH_THE_NAME);
    }

    @AfterEach
    void stopServer() throws IOException {
        server.close();
    }

    @Test
    void exchange_serverThatAnswers_returnsTheAnswer() {
        ServerConnection connection = new SocketServerConnection(ServerAddress.HOST, server.port());

        assertEquals(OK_RESPONSE_WITH_THE_NAME, connection.exchange(GET_REQUEST));
    }

    @Test
    void exchange_serverThatAnswers_sendsTheRequestToIt() {
        ServerConnection connection = new SocketServerConnection(ServerAddress.HOST, server.port());

        connection.exchange(GET_REQUEST);

        assertEquals(GET_REQUEST, server.receivedRequest());
    }

    @Test
    void exchange_noServerListeningOnThatPort_throwsUncheckedIOException() {
        ServerConnection connection = new SocketServerConnection(ServerAddress.HOST, PORT_WITHOUT_A_SERVER);

        assertThrows(UncheckedIOException.class, () -> connection.exchange(GET_REQUEST));
    }
}
