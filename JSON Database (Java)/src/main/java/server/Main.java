package server;

import console.ConsoleIO;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import protocol.JsonCodec;
import protocol.ServerAddress;

public class Main {
    private static final int SESSIONS_SERVED_AT_ONCE = 8;

    public static void main(String[] args) {
        ExecutorService sessions = Executors.newFixedThreadPool(SESSIONS_SERVED_AT_ONCE);
        Path databaseFile = new DatabaseFileLocation().databaseFile();
        serverFor(databaseFile, ServerAddress.PORT, sessions).serveClientsUntilStopped();
    }

    private static DatabaseServer serverFor(Path databaseFile, int port, ExecutorService sessions) {
        ShutdownSignal shutdownSignal = new ShutdownSignal();
        Database database = new JsonDatabase(new JsonFileStorage(databaseFile), new ReentrantReadWriteLock());
        ClientSession session = new ClientSession(new RequestHandler(database, shutdownSignal), new JsonCodec());
        return new DatabaseServer(port, session, sessions, shutdownSignal, new ConsoleIO(System.out));
    }
}
