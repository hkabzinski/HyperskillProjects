package console;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Records everything a program reports, so a test can read it back.
 */
public class RecordingIO implements TextIO {
    private static final String LINE_BREAK = "\n";

    private final List<String> lines = new CopyOnWriteArrayList<>();

    @Override
    public void println(String text) {
        lines.add(text);
    }

    public String output() {
        return lines.stream().map(line -> line + LINE_BREAK).collect(Collectors.joining());
    }

    public boolean hasPrinted(String text) {
        return lines.contains(text);
    }
}
