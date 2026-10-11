package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import console.RecordingIO;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.JsonCodec;

class DatabaseServerTest {
    private static final int TEST_PORT = 24680;
    private static final int SESSIONS_SERVED_AT_ONCE = 4;
    private static final String SET_REQUEST = "{\"type\":\"set\",\"key\":\"name\",\"value\":\"Sorabh\"}";
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String OK_RESPONSE = "{\"response\":\"OK\"}";
    private static final String OK_RESPONSE_WITH_THE_NAME = "{\"response\":\"OK\",\"value\":\"Sorabh\"}";
    private static final String NAME_STORED = "{\"name\":\"Sorabh\"}";
    private static final String ONLY_THE_STARTED_MESSAGE = DatabaseServer.STARTED_MESSAGE + "\n";

    private RecordingIO io;
    private InMemoryStorage storage;
    private RunningServer server;

    @BeforeEach
    void startServer() {
        io = new RecordingIO();
        storage = new InMemoryStorage();
        DatabaseServer databaseServer = databaseServer();
        server = new RunningServer(databaseServer::serveClientsUntilStopped,
                TEST_PORT,
                () -> io.hasPrinted(DatabaseServer.STARTED_MESSAGE));
    }

    @AfterEach
    void stopServer() {
        server.close();
    }

    @Test
    void serveClientsUntilStopped_waitingForClients_reportsOnlyThatTheServerStarted() {
        assertEquals(ONLY_THE_STARTED_MESSAGE, io.output());
    }

    @Test
    void serveClientsUntilStopped_clientSendsASetRequest_answersThatItSucceeded() {
        assertEquals(OK_RESPONSE, server.exchange(SET_REQUEST));
    }

    @Test
    void serveClientsUntilStopped_clientSendsASetRequest_storesTheRecord() {
        server.exchange(SET_REQUEST);

        assertEquals(NAME_STORED, storage.savedRecords());
    }

    @Test
    void serveClientsUntilStopped_twoClientsOneAfterTheOther_answersBothFromTheSameRecords() {
        server.exchange(SET_REQUEST);

        assertEquals(OK_RESPONSE_WITH_THE_NAME, server.exchange(GET_REQUEST));
    }

    @Test
    void serveClientsUntilStopped_clientSendsAnExitRequest_answersThatItSucceeded() {
        assertEquals(OK_RESPONSE, server.exchange(RunningServer.EXIT_REQUEST));
    }

    @Test
    void serveClientsUntilStopped_clientSendsAnExitRequest_stopsServing() {
        server.exchange(RunningServer.EXIT_REQUEST);

        server.awaitStop();

        assertFalse(server.isServing());
    }

    private DatabaseServer databaseServer() {
        ShutdownSignal shutdownSignal = new ShutdownSignal();
        Database database = new JsonDatabase(storage, new ReentrantReadWriteLock());
        ClientSession session = new ClientSession(new RequestHandler(database, shutdownSignal), new JsonCodec());
        ExecutorService sessions = Executors.newFixedThreadPool(SESSIONS_SERVED_AT_ONCE);
        return new DatabaseServer(TEST_PORT, session, sessions, shutdownSignal, io);
    }
}
