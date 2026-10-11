package server;

import java.nio.file.Path;

/**
 * Where the server keeps its records: the file named by a system property when there is one,
 * and the file in the data directory of the server otherwise.
 */
public class DatabaseFileLocation {
    static final String FILE_PROPERTY = "database.file";
    static final String DEFAULT_FILE = "src/server/data/db.json";
    private static final String WORKING_DIRECTORY_PROPERTY = "user.dir";

    public Path databaseFile() {
        String configuredFile = System.getProperty(FILE_PROPERTY);
        if (configuredFile != null) {
            return Path.of(configuredFile);
        }
        return Path.of(System.getProperty(WORKING_DIRECTORY_PROPERTY), DEFAULT_FILE);
    }
}
