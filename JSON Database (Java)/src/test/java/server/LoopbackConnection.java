package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A pair of sockets connected to each other on this computer, so a test can play the client
 * while a session serves the other end.
 */
class LoopbackConnection implements AutoCloseable {
    private static final int ANY_FREE_PORT = 0;

    private final ServerSocket listeningSocket;
    private final Socket clientSide;
    private final Socket serverSide;

    LoopbackConnection() throws IOException {
        listeningSocket = new ServerSocket(ANY_FREE_PORT);
        clientSide = new Socket(InetAddress.getLoopbackAddress(), listeningSocket.getLocalPort());
        serverSide = listeningSocket.accept();
    }

    Socket serverSide() {
        return serverSide;
    }

    void sendRequest(String requestJson) throws IOException {
        new DataOutputStream(clientSide.getOutputStream()).writeUTF(requestJson);
    }

    String readResponse() throws IOException {
        return new DataInputStream(clientSide.getInputStream()).readUTF();
    }

    @Override
    public void close() throws IOException {
        clientSide.close();
        listeningSocket.close();
    }
}
