package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JsonFileStorageTest {
    private static final String DATABASE_FILE_NAME = "db.json";
    private static final String MISSING_DIRECTORY_NAME = "data";
    private static final String NO_RECORDS = "{}";
    private static final String TWO_RECORDS = "{\"key1\":\"a string value\",\"key2\":\"another string value\"}";
    private static final String ONE_RECORD = "{\"key1\":\"a string value\"}";
    private static final String TEXT_THAT_IS_NOT_JSON = "the records are gone";
    private static final String JSON_THAT_IS_NOT_AN_OBJECT = "[\"key1\",\"key2\"]";

    @TempDir
    private Path directory;

    @Test
    void load_fileThatDoesNotExistYet_returnsNoRecords() {
        DatabaseStorage storage = new JsonFileStorage(directory.resolve(DATABASE_FILE_NAME));

        assertEquals(recordsOf(NO_RECORDS), storage.load());
    }

    @Test
    void load_fileWithRecords_returnsThem() throws IOException {
        DatabaseStorage storage = storageOfFileWith(TWO_RECORDS);

        assertEquals(recordsOf(TWO_RECORDS), storage.load());
    }

    @Test
    void load_fileWithTextThatIsNotJson_returnsNoRecords() throws IOException {
        DatabaseStorage storage = storageOfFileWith(TEXT_THAT_IS_NOT_JSON);

        assertEquals(recordsOf(NO_RECORDS), storage.load());
    }

    @Test
    void load_fileWithJsonThatIsNotAnObject_returnsNoRecords() throws IOException {
        DatabaseStorage storage = storageOfFileWith(JSON_THAT_IS_NOT_AN_OBJECT);

        assertEquals(recordsOf(NO_RECORDS), storage.load());
    }

    @Test
    void save_someRecords_writesThemToTheFileAsJson() throws IOException {
        Path file = directory.resolve(DATABASE_FILE_NAME);

        new JsonFileStorage(file).save(recordsOf(TWO_RECORDS));

        assertEquals(TWO_RECORDS, Files.readString(file));
    }

    @Test
    void save_fileThatAlreadyHoldsRecords_replacesThem() throws IOException {
        Path file = directory.resolve(DATABASE_FILE_NAME);
        Files.writeString(file, TWO_RECORDS);

        new JsonFileStorage(file).save(recordsOf(ONE_RECORD));

        assertEquals(ONE_RECORD, Files.readString(file));
    }

    @Test
    void save_fileInADirectoryThatDoesNotExist_createsTheDirectory() {
        Path file = directory.resolve(MISSING_DIRECTORY_NAME).resolve(DATABASE_FILE_NAME);

        new JsonFileStorage(file).save(recordsOf(ONE_RECORD));

        assertTrue(Files.exists(file));
    }

    private DatabaseStorage storageOfFileWith(String contents) throws IOException {
        Path file = directory.resolve(DATABASE_FILE_NAME);
        Files.writeString(file, contents);
        return new JsonFileStorage(file);
    }

    private JsonObject recordsOf(String json) {
        return JsonParser.parseString(json).getAsJsonObject();
    }
}
