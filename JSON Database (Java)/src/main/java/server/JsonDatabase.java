package server;

import com.google.gson.JsonElement;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import protocol.RecordPath;

/**
 * The records kept in storage as JSON, read by any number of requests at once
 * but changed by one request at a time.
 */
public class JsonDatabase implements Database {
    private final DatabaseStorage storage;
    private final Lock readLock;
    private final Lock writeLock;

    public JsonDatabase(DatabaseStorage storage, ReadWriteLock lock) {
        this.storage = storage;
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    @Override
    public Optional<JsonElement> read(RecordPath path) {
        readLock.lock();
        try {
            return storedRecords().find(path);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void write(RecordPath path, JsonElement value) {
        writeLock.lock();
        try {
            RecordTree records = storedRecords();
            records.put(path, value);
            storage.save(records.asJsonObject());
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean delete(RecordPath path) {
        writeLock.lock();
        try {
            return deleteFromStorage(path);
        } finally {
            writeLock.unlock();
        }
    }

    private boolean deleteFromStorage(RecordPath path) {
        RecordTree records = storedRecords();
        if (!records.remove(path)) {
            return false;
        }
        storage.save(records.asJsonObject());
        return true;
    }

    private RecordTree storedRecords() {
        return new RecordTree(storage.load());
    }
}
