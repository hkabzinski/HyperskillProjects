package server;

import client.SocketServerConnection;
import java.util.function.BooleanSupplier;
import protocol.ServerAddress;

/**
 * Runs a server in a thread of its own and waits until it is ready, so a test can talk to it
 * the way a client would.
 */
class RunningServer implements AutoCloseable {
    static final String EXIT_REQUEST = "{\"type\":\"exit\"}";
    private static final long WAIT_STEP_MILLIS = 20;
    private static final int WAIT_STEPS_ALLOWED = 250;
    private static final long STOP_TIMEOUT_MILLIS = 10_000;

    private final int port;
    private final Thread serving;

    RunningServer(Runnable server, int port, BooleanSupplier readyForClients) {
        this.port = port;
        this.serving = new Thread(server);
        serving.setDaemon(true);
        serving.start();
        awaitReady(readyForClients);
    }

    String exchange(String requestJson) {
        return new SocketServerConnection(ServerAddress.HOST, port).exchange(requestJson);
    }

    boolean isServing() {
        return serving.isAlive();
    }

    void awaitStop() {
        join();
    }

    @Override
    public void close() {
        if (serving.isAlive()) {
            exchange(EXIT_REQUEST);
        }
        join();
    }

    private void awaitReady(BooleanSupplier readyForClients) {
        for (int step = 0; step < WAIT_STEPS_ALLOWED; step++) {
            if (readyForClients.getAsBoolean()) {
                return;
            }
            waitOneStep();
        }
        throw new IllegalStateException("The server never became ready for clients.");
    }

    private void waitOneStep() {
        try {
            Thread.sleep(WAIT_STEP_MILLIS);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private void join() {
        try {
            serving.join(STOP_TIMEOUT_MILLIS);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
