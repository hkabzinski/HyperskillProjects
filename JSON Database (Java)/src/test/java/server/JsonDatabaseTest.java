package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.RecordPath;

class JsonDatabaseTest {
    private static final String NAME = "name";
    private static final String PERSON = "person";
    private static final String SORABH = "Sorabh";
    private static final RecordPath NAME_PATH = new RecordPath(List.of(NAME));
    private static final RecordPath PERSON_NAME_PATH = new RecordPath(List.of(PERSON, NAME));
    private static final String NO_RECORDS = "{}";
    private static final String NAME_STORED = "{\"name\":\"Sorabh\"}";
    private static final String NAME_STORED_INSIDE_A_PERSON = "{\"person\":{\"name\":\"Sorabh\"}}";

    private InMemoryStorage storage;
    private CountingReadWriteLock lock;
    private Database database;

    @BeforeEach
    void createDatabase() {
        storage = new InMemoryStorage();
        lock = new CountingReadWriteLock();
        database = new JsonDatabase(storage, lock);
    }

    @Test
    void read_pathThatWasWrittenBefore_returnsTheStoredValue() {
        database.write(NAME_PATH, new JsonPrimitive(SORABH));

        assertEquals(Optional.of(new JsonPrimitive(SORABH)), database.read(NAME_PATH));
    }

    @Test
    void read_pathThatWasNeverWritten_returnsNothing() {
        assertEquals(Optional.empty(), database.read(NAME_PATH));
    }

    @Test
    void write_valueForAPath_savesItToStorage() {
        database.write(NAME_PATH, new JsonPrimitive(SORABH));

        assertEquals(NAME_STORED, storage.savedRecords());
    }

    @Test
    void write_pathInsideAnObjectThatIsMissing_savesTheObjectThatWasCreated() {
        database.write(PERSON_NAME_PATH, new JsonPrimitive(SORABH));

        assertEquals(NAME_STORED_INSIDE_A_PERSON, storage.savedRecords());
    }

    @Test
    void delete_pathThatWasWrittenBefore_removesItFromStorage() {
        database.write(NAME_PATH, new JsonPrimitive(SORABH));

        database.delete(NAME_PATH);

        assertEquals(NO_RECORDS, storage.savedRecords());
    }

    @Test
    void delete_pathThatWasWrittenBefore_reportsThatItWasDeleted() {
        database.write(NAME_PATH, new JsonPrimitive(SORABH));

        assertTrue(database.delete(NAME_PATH));
    }

    @Test
    void delete_pathThatWasNeverWritten_reportsThatNothingWasDeleted() {
        assertFalse(database.delete(NAME_PATH));
    }

    @Test
    void delete_pathThatWasNeverWritten_leavesStorageUntouched() {
        database.delete(NAME_PATH);

        assertEquals(NO_RECORDS, storage.savedRecords());
    }

    @Test
    void read_anyPath_locksTheRecordsForReadingAndReleasesThemAgain() {
        database.read(NAME_PATH);

        assertTrue(lock.lockedAndUnlockedForReading());
    }

    @Test
    void write_anyValue_locksTheRecordsForWritingAndReleasesThemAgain() {
        database.write(NAME_PATH, new JsonPrimitive(SORABH));

        assertTrue(lock.lockedAndUnlockedForWriting());
    }

    @Test
    void delete_anyPath_locksTheRecordsForWritingAndReleasesThemAgain() {
        database.delete(NAME_PATH);

        assertTrue(lock.lockedAndUnlockedForWriting());
    }

    @Test
    void read_databaseGuardedByARealReadWriteLock_stillReturnsTheStoredValue() {
        Database guardedDatabase = new JsonDatabase(storage, new ReentrantReadWriteLock());
        guardedDatabase.write(NAME_PATH, new JsonPrimitive(SORABH));

        assertEquals(Optional.of(new JsonPrimitive(SORABH)), guardedDatabase.read(NAME_PATH));
    }
}
