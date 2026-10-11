package search;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads the people from a text file, one person per line.
 */
public class PersonFileSource implements PersonSource {
    private final Path dataFile;

    public PersonFileSource(Path dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public List<Person> readPeople() {
        try {
            return Files.readAllLines(dataFile).stream()
                    .filter(line -> !line.isBlank())
                    .map(Person::new)
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read the data file " + dataFile, e);
        }
    }
}
