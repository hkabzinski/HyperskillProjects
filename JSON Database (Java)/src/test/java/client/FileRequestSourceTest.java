package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileRequestSourceTest {
    private static final String REQUEST_FILE_NAME = "testSet.json";
    private static final String MISSING_FILE_NAME = "nowhere.json";
    private static final String SET_REQUEST = "{\"type\":\"set\",\"key\":\"name\",\"value\":\"Sorabh\"}";
    private static final String SET_REQUEST_ON_ITS_OWN_LINE = SET_REQUEST + "\n";
    private static final String REQUEST_WITH_A_NESTED_VALUE = """
            {"type":"set","key":"person","value":{"name":"Elon Musk","car":{"model":"Tesla Roadster"}}}""";

    @TempDir
    private Path directory;

    @Test
    void readRequest_fileHoldingARequest_returnsThatRequest() throws IOException {
        RequestSource source = sourceOfFileWith(SET_REQUEST);

        assertEquals(SET_REQUEST, source.readRequest());
    }

    @Test
    void readRequest_fileEndingWithALineBreak_returnsTheRequestWithoutIt() throws IOException {
        RequestSource source = sourceOfFileWith(SET_REQUEST_ON_ITS_OWN_LINE);

        assertEquals(SET_REQUEST, source.readRequest());
    }

    @Test
    void readRequest_fileHoldingARequestWithANestedValue_returnsItUnchanged() throws IOException {
        RequestSource source = sourceOfFileWith(REQUEST_WITH_A_NESTED_VALUE);

        assertEquals(REQUEST_WITH_A_NESTED_VALUE, source.readRequest());
    }

    @Test
    void readRequest_fileThatDoesNotExist_throwsUncheckedIOException() {
        RequestSource source = new FileRequestSource(directory.resolve(MISSING_FILE_NAME));

        assertThrows(UncheckedIOException.class, source::readRequest);
    }

    private RequestSource sourceOfFileWith(String contents) throws IOException {
        Path requestFile = directory.resolve(REQUEST_FILE_NAME);
        Files.writeString(requestFile, contents);
        return new FileRequestSource(requestFile);
    }
}
