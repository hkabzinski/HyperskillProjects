package server;

import console.TextIO;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import protocol.ServerAddress;

/**
 * Waits for clients on a port and lets a session serve every connection in a task of its own,
 * until a request asks the server to stop.
 */
public class DatabaseServer {
    static final String STARTED_MESSAGE = "Server started!";
    private static final int WAITING_CLIENTS_ALLOWED = 50;
    private static final int WAIT_FOR_CLIENT_MILLIS = 200;
    private static final int WAIT_FOR_SESSIONS_SECONDS = 10;

    private final int port;
    private final ClientSession session;
    private final ExecutorService sessions;
    private final ShutdownSignal shutdownSignal;
    private final TextIO io;

    public DatabaseServer(int port,
                          ClientSession session,
                          ExecutorService sessions,
                          ShutdownSignal shutdownSignal,
                          TextIO io) {
        this.port = port;
        this.session = session;
        this.sessions = sessions;
        this.shutdownSignal = shutdownSignal;
        this.io = io;
    }

    public void serveClientsUntilStopped() {
        try (ServerSocket serverSocket = listeningSocket()) {
            io.println(STARTED_MESSAGE);
            acceptClientsUntilStopped(serverSocket);
        } catch (IOException couldNotListen) {
            throw new UncheckedIOException(couldNotListen);
        } finally {
            awaitRunningSessions();
        }
    }

    private ServerSocket listeningSocket() throws IOException {
        ServerSocket serverSocket = new ServerSocket(
                port, WAITING_CLIENTS_ALLOWED, InetAddress.getByName(ServerAddress.HOST));
        serverSocket.setSoTimeout(WAIT_FOR_CLIENT_MILLIS);
        return serverSocket;
    }

    private void acceptClientsUntilStopped(ServerSocket serverSocket) throws IOException {
        while (!shutdownSignal.isRaised()) {
            acceptOneClient(serverSocket);
        }
    }

    private void acceptOneClient(ServerSocket serverSocket) throws IOException {
        try {
            Socket clientSocket = serverSocket.accept();
            sessions.execute(() -> session.serve(clientSocket));
        } catch (SocketTimeoutException noClientConnectedInTime) {
            // Nobody connected while the socket waited, so look at the shutdown signal and wait again.
        }
    }

    private void awaitRunningSessions() {
        sessions.shutdown();
        try {
            sessions.awaitTermination(WAIT_FOR_SESSIONS_SECONDS, TimeUnit.SECONDS);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
