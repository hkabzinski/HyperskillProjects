package client;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Stage 3: sends the one request its command line describes and prints the answer.
 */
public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final String STARTED_MESSAGE = "Client started!";
    private static final String SENT_PREFIX = "Sent: ";
    private static final String RECEIVED_PREFIX = "Received: ";
    private static final String SEPARATOR = " ";
    private static final String EXIT = "exit";

    @Parameter(names = "-t")
    private String type;

    @Parameter(names = "-i")
    private String index;

    @Parameter(names = "-m")
    private String message;

    public static void main(String[] args) throws IOException {
        Main client = new Main();
        JCommander.newBuilder().addObject(client).build().parse(args);
        client.sendOneRequest();
    }

    private void sendOneRequest() throws IOException {
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
             DataInputStream fromServer = new DataInputStream(socket.getInputStream());
             DataOutputStream toServer = new DataOutputStream(socket.getOutputStream())) {
            System.out.println(STARTED_MESSAGE);

            String request = request();
            toServer.writeUTF(request);
            System.out.println(SENT_PREFIX + request);

            System.out.println(RECEIVED_PREFIX + fromServer.readUTF());
        }
    }

    private String request() {
        if (EXIT.equals(type)) {
            return EXIT;
        }
        if (message == null) {
            return type + SEPARATOR + index;
        }
        return type + SEPARATOR + index + SEPARATOR + message;
    }
}
