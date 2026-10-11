package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import protocol.ServerAddress;

class MainTest {
    private static final String[] NO_ARGUMENTS = {};
    private static final String[] SET_THE_NAME = {"-t", "set", "-k", "name", "-v", "Sorabh"};
    private static final String[] GET_THE_NAME = {"-t", "get", "-k", "name"};
    private static final String[] GET_THE_NAME_FROM_A_FILE = {"-in", "testGet.json"};
    private static final String[] EXIT = {"-t", "exit"};
    private static final String DATABASE_FILE_NAME = "db.json";
    private static final String NAME_STORED = "{\"name\":\"Sorabh\"}";
    private static final String RECEIVED_THE_STORED_NAME = "Received: {\"response\":\"OK\",\"value\":\"Sorabh\"}";

    @TempDir
    private Path directory;

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream written;
    private Path databaseFile;
    private RunningServer server;

    @BeforeEach
    void startServer() {
        written = new ByteArrayOutputStream();
        System.setOut(new PrintStream(written, true, StandardCharsets.UTF_8));
        databaseFile = directory.resolve(DATABASE_FILE_NAME);
        System.setProperty(DatabaseFileLocation.FILE_PROPERTY, databaseFile.toString());
        server = new RunningServer(() -> Main.main(NO_ARGUMENTS), ServerAddress.PORT, this::serverHasStarted);
    }

    @AfterEach
    void stopServer() {
        server.close();
        System.setOut(originalOut);
        System.clearProperty(DatabaseFileLocation.FILE_PROPERTY);
    }

    @Test
    void main_clientSetsAValue_storesItInTheDatabaseFile() throws IOException {
        client.Main.main(SET_THE_NAME);

        assertEquals(NAME_STORED, Files.readString(databaseFile));
    }

    @Test
    void main_clientAsksForAValueAnEarlierClientStored_reportsThatValue() {
        client.Main.main(SET_THE_NAME);

        client.Main.main(GET_THE_NAME);

        assertTrue(printedText().contains(RECEIVED_THE_STORED_NAME));
    }

    @Test
    void main_clientSendsARequestFromAFile_reportsTheAnswerToThatRequest() {
        client.Main.main(SET_THE_NAME);

        client.Main.main(GET_THE_NAME_FROM_A_FILE);

        assertTrue(printedText().contains(RECEIVED_THE_STORED_NAME));
    }

    @Test
    void main_clientSendsAnExitRequest_stopsTheServer() {
        client.Main.main(EXIT);

        server.awaitStop();

        assertFalse(server.isServing());
    }

    private boolean serverHasStarted() {
        return printedText().contains(DatabaseServer.STARTED_MESSAGE);
    }

    private String printedText() {
        return written.toString(StandardCharsets.UTF_8);
    }
}
