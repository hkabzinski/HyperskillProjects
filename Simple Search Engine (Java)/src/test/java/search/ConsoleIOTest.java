package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConsoleIOTest {
    private static final String FIRST_LINE = "1";
    private static final String SECOND_LINE = "ANY";
    private static final String TEXT = "No matching people found.";

    private final ByteArrayOutputStream written = new ByteArrayOutputStream();

    @Test
    void readLine_calledTwice_returnsLinesInOrder() {
        ConsoleIO io = consoleWithInput(FIRST_LINE + "\n" + SECOND_LINE + "\n");
        io.readLine();

        assertEquals(SECOND_LINE, io.readLine());
    }

    @Test
    void print_text_writesItWithoutLineBreak() {
        ConsoleIO io = consoleWithInput("");

        io.print(TEXT);

        assertEquals(TEXT, written.toString(StandardCharsets.UTF_8));
    }

    @Test
    void println_text_writesItWithLineBreak() {
        ConsoleIO io = consoleWithInput("");

        io.println(TEXT);

        assertEquals(TEXT + System.lineSeparator(), written.toString(StandardCharsets.UTF_8));
    }

    private ConsoleIO consoleWithInput(String input) {
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        return new ConsoleIO(in, new PrintStream(written, true, StandardCharsets.UTF_8));
    }
}
