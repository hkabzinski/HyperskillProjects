package client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import console.RecordingIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseClientTest {
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String OK_RESPONSE_WITH_THE_NAME = "{\"response\":\"OK\",\"value\":\"Sorabh\"}";
    private static final String REPORT_OF_THE_WHOLE_EXCHANGE = """
            Client started!
            Sent: {"type":"get","key":"name"}
            Received: {"response":"OK","value":"Sorabh"}
            """;

    private RecordingIO io;
    private FakeServerConnection connection;
    private DatabaseClient client;

    @BeforeEach
    void createClient() {
        io = new RecordingIO();
        connection = new FakeServerConnection(OK_RESPONSE_WITH_THE_NAME);
        client = new DatabaseClient(new FixedRequestSource(GET_REQUEST), connection, io);
    }

    @Test
    void sendOneRequest_requestFromItsSource_sendsThatRequestToTheServer() {
        client.sendOneRequest();

        assertEquals(GET_REQUEST, connection.receivedRequest());
    }

    @Test
    void sendOneRequest_serverAnswers_reportsTheStartTheRequestAndTheAnswer() {
        client.sendOneRequest();

        assertEquals(REPORT_OF_THE_WHOLE_EXCHANGE, io.output());
    }
}
