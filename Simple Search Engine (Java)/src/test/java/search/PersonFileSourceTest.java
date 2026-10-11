package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class PersonFileSourceTest {
    private static final String DATA_FILE_NAME = "people.txt";
    private static final String MISSING_FILE_NAME = "nowhere.txt";
    private static final String TWO_LINES = """
            Katie Jacobs
            Erick Burgess
            """;
    private static final String LINES_AROUND_A_BLANK_ONE = """
            Katie Jacobs

            Erick Burgess
            """;
    private static final String PADDED_LINE = "Richard    Roy    roy@gmail.com";

    @TempDir
    private Path directory;

    @Test
    void readPeople_fileWithTwoLines_returnsOnePersonPerLine() throws IOException {
        PersonSource source = sourceOf(TWO_LINES);

        assertEquals(List.of(TestPeople.KATIE_JACOBS, TestPeople.ERICK_BURGESS), source.readPeople());
    }

    @Test
    void readPeople_fileWithBlankLine_skipsThatLine() throws IOException {
        PersonSource source = sourceOf(LINES_AROUND_A_BLANK_ONE);

        assertEquals(List.of(TestPeople.KATIE_JACOBS, TestPeople.ERICK_BURGESS), source.readPeople());
    }

    @Test
    void readPeople_lineWithExtraSpaces_keepsTheLineVerbatim() throws IOException {
        PersonSource source = sourceOf(PADDED_LINE);

        assertEquals(List.of(new Person(PADDED_LINE)), source.readPeople());
    }

    @Test
    void readPeople_emptyFile_returnsNoPeople() throws IOException {
        PersonSource source = sourceOf("");

        assertEquals(List.of(), source.readPeople());
    }

    @Test
    void readPeople_missingFile_throwsUncheckedIOException() {
        PersonSource source = new PersonFileSource(directory.resolve(MISSING_FILE_NAME));

        assertThrows(UncheckedIOException.class, source::readPeople);
    }

    private PersonSource sourceOf(String contents) throws IOException {
        Path dataFile = directory.resolve(DATA_FILE_NAME);
        Files.writeString(dataFile, contents);
        return new PersonFileSource(dataFile);
    }
}
