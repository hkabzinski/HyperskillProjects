package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Reaches the server through a socket, sending one request and reading the answer to it.
 */
public class SocketServerConnection implements ServerConnection {
    private final String host;
    private final int port;

    public SocketServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String exchange(String requestJson) {
        try (Socket socket = new Socket(InetAddress.getByName(host), port);
             DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
             DataInputStream fromServer = new DataInputStream(socket.getInputStream())) {
            toServer.writeUTF(requestJson);
            return fromServer.readUTF();
        } catch (IOException connectionFailed) {
            throw new UncheckedIOException(connectionFailed);
        }
    }
}
