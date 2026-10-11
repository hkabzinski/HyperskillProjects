package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.JsonCodec;

class ClientSessionTest {
    private static final String SET_REQUEST = "{\"type\":\"set\",\"key\":\"name\",\"value\":\"Sorabh\"}";
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String TEXT_THAT_IS_NOT_A_REQUEST = "give me a record";
    private static final String OK_RESPONSE = "{\"response\":\"OK\"}";
    private static final String OK_RESPONSE_WITH_THE_NAME = "{\"response\":\"OK\",\"value\":\"Sorabh\"}";
    private static final String MALFORMED_REQUEST_RESPONSE =
            "{\"response\":\"ERROR\",\"reason\":\"The request is not valid JSON\"}";

    private ClientSession session;

    @BeforeEach
    void createSession() {
        Database database = new JsonDatabase(new InMemoryStorage(), new ReentrantReadWriteLock());
        session = new ClientSession(new RequestHandler(database, new ShutdownSignal()), new JsonCodec());
    }

    @Test
    void serve_setRequest_answersTheClientThatItSucceeded() throws IOException {
        assertEquals(OK_RESPONSE, answerTo(SET_REQUEST));
    }

    @Test
    void serve_getRequestForAValueStoredByAnEarlierClient_answersWithThatValue() throws IOException {
        answerTo(SET_REQUEST);

        assertEquals(OK_RESPONSE_WITH_THE_NAME, answerTo(GET_REQUEST));
    }

    @Test
    void serve_textThatIsNotARequest_answersTheClientWithTheReason() throws IOException {
        assertEquals(MALFORMED_REQUEST_RESPONSE, answerTo(TEXT_THAT_IS_NOT_A_REQUEST));
    }

    @Test
    void serve_requestThatWasAnswered_closesTheConnectionToTheClient() throws IOException {
        try (LoopbackConnection connection = new LoopbackConnection()) {
            connection.sendRequest(SET_REQUEST);

            session.serve(connection.serverSide());

            assertTrue(connection.serverSide().isClosed());
        }
    }

    private String answerTo(String requestJson) throws IOException {
        try (LoopbackConnection connection = new LoopbackConnection()) {
            connection.sendRequest(requestJson);
            session.serve(connection.serverSide());
            return connection.readResponse();
        }
    }
}
