package server;

import com.google.gson.JsonObject;

/**
 * Where the records live between requests, so the database does not depend on a particular medium.
 */
public interface DatabaseStorage {
    JsonObject load();

    void save(JsonObject records);
}
