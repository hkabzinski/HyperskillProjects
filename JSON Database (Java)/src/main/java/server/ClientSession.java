package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Socket;
import protocol.InvalidRequestException;
import protocol.JsonCodec;
import protocol.Response;

/**
 * Serves one connected client: reads its request, answers it and closes the connection.
 */
public class ClientSession {
    private final RequestHandler handler;
    private final JsonCodec codec;

    public ClientSession(RequestHandler handler, JsonCodec codec) {
        this.handler = handler;
        this.codec = codec;
    }

    public void serve(Socket clientSocket) {
        try (Socket connection = clientSocket;
             DataInputStream fromClient = new DataInputStream(connection.getInputStream());
             DataOutputStream toClient = new DataOutputStream(connection.getOutputStream())) {
            toClient.writeUTF(codec.toJson(responseTo(fromClient.readUTF())));
        } catch (IOException connectionFailed) {
            throw new UncheckedIOException(connectionFailed);
        }
    }

    private Response responseTo(String requestJson) {
        try {
            return handler.handle(codec.parseRequest(requestJson));
        } catch (InvalidRequestException invalidRequest) {
            return Response.error(invalidRequest.getMessage());
        }
    }
}
