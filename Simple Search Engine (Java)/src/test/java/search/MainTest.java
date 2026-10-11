package search;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {
    private static final String DATA_FILE_NAME = "people.txt";
    private static final String TWO_PEOPLE = """
            Katie Jacobs
            Erick Harrington harrington@gmail.com
            """;
    private static final String SEARCH_FOR_KATIE_THEN_EXIT = "1\nANY\nKATIE\n0\n";
    private static final String EXIT_IMMEDIATELY = "0\n";

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @TempDir
    private Path directory;

    @AfterEach
    void restoreStandardStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void main_dataFileAndASearchOnStandardInput_printsTheMatchingPerson() throws IOException {
        String output = runMainWith(SEARCH_FOR_KATIE_THEN_EXIT, dataFileWith(TWO_PEOPLE));

        assertTrue(output.contains(TestPeople.KATIE_JACOBS.details()));
    }

    @Test
    void main_exitChosenRightAway_saysGoodbye() throws IOException {
        String output = runMainWith(EXIT_IMMEDIATELY, dataFileWith(TWO_PEOPLE));

        assertTrue(output.trim().endsWith(SearchMenu.FAREWELL_MESSAGE));
    }

    @Test
    void main_withoutTheDataOption_explainsHowToPassTheDataFile() {
        ByteArrayOutputStream written = redirectStandardStreams(EXIT_IMMEDIATELY);

        Main.main(new String[0]);

        assertTrue(written.toString(StandardCharsets.UTF_8).contains(Main.MISSING_DATA_OPTION_MESSAGE));
    }

    private String runMainWith(String input, String dataFileName) {
        ByteArrayOutputStream written = redirectStandardStreams(input);

        Main.main(new String[] {CommandLineArguments.DATA_OPTION, dataFileName});

        return written.toString(StandardCharsets.UTF_8);
    }

    private ByteArrayOutputStream redirectStandardStreams(String input) {
        ByteArrayOutputStream written = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(written, true, StandardCharsets.UTF_8));
        return written;
    }

    private String dataFileWith(String contents) throws IOException {
        Path dataFile = directory.resolve(DATA_FILE_NAME);
        Files.writeString(dataFile, contents);
        return dataFile.toString();
    }
}
