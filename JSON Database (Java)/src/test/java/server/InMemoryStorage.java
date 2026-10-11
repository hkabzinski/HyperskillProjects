package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Keeps the records in memory as the JSON text a file would hold, so a test can look at
 * exactly what the database stored.
 */
class InMemoryStorage implements DatabaseStorage {
    private static final String NO_RECORDS = "{}";

    private final Gson gson = new Gson();
    private String records;

    InMemoryStorage() {
        this(NO_RECORDS);
    }

    InMemoryStorage(String records) {
        this.records = records;
    }

    @Override
    public JsonObject load() {
        return gson.fromJson(records, JsonObject.class);
    }

    @Override
    public void save(JsonObject records) {
        this.records = gson.toJson(records);
    }

    String savedRecords() {
        return records;
    }
}
