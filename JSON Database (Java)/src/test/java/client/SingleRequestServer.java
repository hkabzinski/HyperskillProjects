package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A stand-in server that answers one request with a prepared response, so the connection of the
 * client can be tested without the real database server.
 */
class SingleRequestServer implements AutoCloseable {
    private static final int ANY_FREE_PORT = 0;
    private static final int ONE_WAITING_CLIENT = 1;
    private static final long ANSWER_TIMEOUT_MILLIS = 5_000;

    private final ServerSocket listeningSocket;
    private final Thread answering;
    private volatile String receivedRequest;

    SingleRequestServer(String response) throws IOException {
        listeningSocket = new ServerSocket(ANY_FREE_PORT, ONE_WAITING_CLIENT, InetAddress.getLoopbackAddress());
        answering = new Thread(() -> answerOneRequest(response));
        answering.setDaemon(true);
        answering.start();
    }

    int port() {
        return listeningSocket.getLocalPort();
    }

    String receivedRequest() {
        awaitAnswer();
        return receivedRequest;
    }

    @Override
    public void close() throws IOException {
        listeningSocket.close();
    }

    private void answerOneRequest(String response) {
        try (Socket clientSocket = listeningSocket.accept();
             DataInputStream fromClient = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream())) {
            receivedRequest = fromClient.readUTF();
            toClient.writeUTF(response);
        } catch (IOException noRequestArrived) {
            // The test closed the server without sending a request.
        }
    }

    private void awaitAnswer() {
        try {
            answering.join(ANSWER_TIMEOUT_MILLIS);
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
        }
    }
}
