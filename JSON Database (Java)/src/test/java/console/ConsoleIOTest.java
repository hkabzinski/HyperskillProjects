package console;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConsoleIOTest {
    private static final String TEXT = "Server started!";
    private static final String TEXT_WITH_LINE_BREAK = TEXT + System.lineSeparator();

    @Test
    void println_someText_writesTheTextAndALineBreak() {
        ByteArrayOutputStream written = new ByteArrayOutputStream();

        new ConsoleIO(new PrintStream(written, true, StandardCharsets.UTF_8)).println(TEXT);

        assertEquals(TEXT_WITH_LINE_BREAK, written.toString(StandardCharsets.UTF_8));
    }
}
