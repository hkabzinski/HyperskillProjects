package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

/**
 * Stage 3: serves one client at a time, storing text in 1000 cells, until a client asks it to exit.
 */
public class Main {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static final int WAITING_CLIENTS_ALLOWED = 50;
    private static final int CELLS = 1000;
    private static final String EMPTY_CELL = "";
    private static final String STARTED_MESSAGE = "Server started!";
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String SET = "set";
    private static final String GET = "get";
    private static final String DELETE = "delete";
    private static final String EXIT = "exit";
    private static final String SEPARATOR = " ";
    private static final int REQUEST_PARTS = 3;

    private static final String[] CELL_CONTENTS = new String[CELLS];

    public static void main(String[] args) throws IOException {
        Arrays.fill(CELL_CONTENTS, EMPTY_CELL);
        try (ServerSocket serverSocket =
                     new ServerSocket(PORT, WAITING_CLIENTS_ALLOWED, InetAddress.getByName(ADDRESS))) {
            System.out.println(STARTED_MESSAGE);
            serveClientsUntilExitRequest(serverSocket);
        }
    }

    private static void serveClientsUntilExitRequest(ServerSocket serverSocket) throws IOException {
        boolean serving = true;
        while (serving) {
            serving = serveOneClient(serverSocket);
        }
    }

    private static boolean serveOneClient(ServerSocket serverSocket) throws IOException {
        try (Socket clientSocket = serverSocket.accept();
             DataInputStream fromClient = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream())) {
            String[] request = fromClient.readUTF().split(SEPARATOR, REQUEST_PARTS);
            if (EXIT.equals(request[0])) {
                toClient.writeUTF(OK);
                return false;
            }
            toClient.writeUTF(answerTo(request));
            return true;
        }
    }

    private static String answerTo(String[] request) {
        int cell = Integer.parseInt(request[1]) - 1;
        if (cell < 0 || cell >= CELL_CONTENTS.length) {
            return ERROR;
        }
        return switch (request[0]) {
            case SET -> store(request[2], cell);
            case GET -> read(cell);
            case DELETE -> delete(cell);
            default -> ERROR;
        };
    }

    private static String store(String text, int cell) {
        CELL_CONTENTS[cell] = text;
        return OK;
    }

    private static String read(int cell) {
        return CELL_CONTENTS[cell].isEmpty() ? ERROR : CELL_CONTENTS[cell];
    }

    private static String delete(int cell) {
        CELL_CONTENTS[cell] = EMPTY_CELL;
        return OK;
    }
}
