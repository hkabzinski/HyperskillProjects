package console;

import java.io.PrintStream;

public class ConsoleIO implements TextIO {
    private final PrintStream out;

    public ConsoleIO(PrintStream out) {
        this.out = out;
    }

    @Override
    public void println(String text) {
        out.println(text);
    }
}
