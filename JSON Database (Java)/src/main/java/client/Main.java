package client;

import console.ConsoleIO;
import console.TextIO;
import java.nio.file.Path;
import java.util.Optional;
import protocol.InvalidRequestException;
import protocol.JsonCodec;
import protocol.ServerAddress;

public class Main {
    private static final String REQUEST_DIRECTORY = "src/client/data";

    public static void main(String[] args) {
        TextIO io = new ConsoleIO(System.out);
        try {
            clientFor(ClientArguments.parsedFrom(args), io).sendOneRequest();
        } catch (InvalidRequestException invalidRequest) {
            io.println(invalidRequest.getMessage());
        }
    }

    private static DatabaseClient clientFor(ClientArguments arguments, TextIO io) {
        return new DatabaseClient(requestSourceFor(arguments),
                new SocketServerConnection(ServerAddress.HOST, ServerAddress.PORT),
                io);
    }

    private static RequestSource requestSourceFor(ClientArguments arguments) {
        Optional<String> inputFileName = arguments.inputFileName();
        if (inputFileName.isPresent()) {
            return new FileRequestSource(requestFile(inputFileName.get()));
        }
        return new ArgumentsRequestSource(arguments, new JsonCodec());
    }

    private static Path requestFile(String fileName) {
        return Path.of(System.getProperty("user.dir"), REQUEST_DIRECTORY, fileName);
    }
}
