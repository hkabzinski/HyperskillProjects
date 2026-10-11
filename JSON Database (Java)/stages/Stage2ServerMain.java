package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Stage 2: answers the single request of one client and stops.
 */
public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final int WAITING_CLIENTS_ALLOWED = 50;
    private static final String STARTED_MESSAGE = "Server started!";
    private static final String RECEIVED_PREFIX = "Received: ";
    private static final String SENT_PREFIX = "Sent: ";
    private static final String RECORD_NUMBER_PREFIX = "# ";
    private static final String ANSWER_START = "A record # ";
    private static final String ANSWER_END = " was sent!";

    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket =
                     new ServerSocket(PORT, WAITING_CLIENTS_ALLOWED, InetAddress.getByName(ADDRESS))) {
            System.out.println(STARTED_MESSAGE);
            answerOneClient(serverSocket);
        }
    }

    private static void answerOneClient(ServerSocket serverSocket) throws IOException {
        try (Socket clientSocket = serverSocket.accept();
             DataInputStream fromClient = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream())) {
            String request = fromClient.readUTF();
            System.out.println(RECEIVED_PREFIX + request);

            String answer = answerTo(request);
            toClient.writeUTF(answer);
            System.out.println(SENT_PREFIX + answer);
        }
    }

    private static String answerTo(String request) {
        String recordNumber = request.substring(request.indexOf(RECORD_NUMBER_PREFIX) + RECORD_NUMBER_PREFIX.length());
        return ANSWER_START + recordNumber + ANSWER_END;
    }
}
