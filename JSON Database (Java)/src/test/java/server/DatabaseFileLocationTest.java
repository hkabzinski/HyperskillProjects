package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatabaseFileLocationTest {
    private static final String CONFIGURED_FILE = "build/records.json";
    private static final Path DEFAULT_FILE = Path.of("src", "server", "data", "db.json");

    private DatabaseFileLocation location;

    @BeforeEach
    void createLocation() {
        System.clearProperty(DatabaseFileLocation.FILE_PROPERTY);
        location = new DatabaseFileLocation();
    }

    @AfterEach
    void forgetConfiguredFile() {
        System.clearProperty(DatabaseFileLocation.FILE_PROPERTY);
    }

    @Test
    void databaseFile_propertyNamingAFile_returnsThatFile() {
        System.setProperty(DatabaseFileLocation.FILE_PROPERTY, CONFIGURED_FILE);

        assertEquals(Path.of(CONFIGURED_FILE), location.databaseFile());
    }

    @Test
    void databaseFile_withoutTheProperty_returnsTheFileInTheDataDirectoryOfTheServer() {
        assertTrue(location.databaseFile().endsWith(DEFAULT_FILE));
    }

    @Test
    void databaseFile_withoutTheProperty_returnsAnAbsoluteFile() {
        assertTrue(location.databaseFile().isAbsolute());
    }
}
