package client;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Takes the request from a file prepared beforehand, which is how a client sends values
 * that are too awkward to type on the command line.
 */
public class FileRequestSource implements RequestSource {
    private final Path requestFile;

    public FileRequestSource(Path requestFile) {
        this.requestFile = requestFile;
    }

    @Override
    public String readRequest() {
        try {
            return Files.readString(requestFile).trim();
        } catch (IOException couldNotRead) {
            throw new UncheckedIOException(couldNotRead);
        }
    }
}
