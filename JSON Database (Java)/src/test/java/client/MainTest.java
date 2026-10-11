package client;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {
    private static final String[] NO_OPTIONS = {};
    private static final String[] GET_WITHOUT_A_KEY = {"-t", "get"};

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream written;

    @BeforeEach
    void captureWhatTheClientPrints() {
        written = new ByteArrayOutputStream();
        System.setOut(new PrintStream(written, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreStandardOutput() {
        System.setOut(originalOut);
    }

    @Test
    void main_withoutAnyOptions_explainsThatARequestNeedsAType() {
        Main.main(NO_OPTIONS);

        assertTrue(printedText().contains(ArgumentsRequestSource.MISSING_TYPE_REASON));
    }

    @Test
    void main_getWithoutAKey_explainsThatTheRequestNeedsAKey() {
        Main.main(GET_WITHOUT_A_KEY);

        assertTrue(printedText().contains(ArgumentsRequestSource.MISSING_KEY_REASON));
    }

    @Test
    void main_withoutAnyOptions_reportsThatTheClientStarted() {
        Main.main(NO_OPTIONS);

        assertTrue(printedText().contains(DatabaseClient.STARTED_MESSAGE));
    }

    private String printedText() {
        return written.toString(StandardCharsets.UTF_8);
    }
}
