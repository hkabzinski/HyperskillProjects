package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Stage 2: asks the server for one record and prints the answer.
 */
public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final int RECORD_NUMBER = 12;
    private static final String STARTED_MESSAGE = "Client started!";
    private static final String SENT_PREFIX = "Sent: ";
    private static final String RECEIVED_PREFIX = "Received: ";
    private static final String REQUEST_START = "Give me a record # ";

    public static void main(String[] args) throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream fromServer = new DataInputStream(socket.getInputStream());
             DataOutputStream toServer = new DataOutputStream(socket.getOutputStream())) {
            System.out.println(STARTED_MESSAGE);

            String request = REQUEST_START + RECORD_NUMBER;
            toServer.writeUTF(request);
            System.out.println(SENT_PREFIX + request);

            System.out.println(RECEIVED_PREFIX + fromServer.readUTF());
        }
    }
}
