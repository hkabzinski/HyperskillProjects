package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Keeps the records in a JSON file on the hard drive, so they outlive a restart of the server.
 */
public class JsonFileStorage implements DatabaseStorage {
    private final Path file;
    private final Gson gson = new Gson();

    public JsonFileStorage(Path file) {
        this.file = file;
    }

    @Override
    public JsonObject load() {
        if (!Files.exists(file)) {
            return new JsonObject();
        }
        return recordsIn(readFile());
    }

    @Override
    public void save(JsonObject records) {
        try {
            createDirectoryOfFile();
            Files.writeString(file, gson.toJson(records));
        } catch (IOException couldNotWrite) {
            throw new UncheckedIOException(couldNotWrite);
        }
    }

    private String readFile() {
        try {
            return Files.readString(file);
        } catch (IOException couldNotRead) {
            throw new UncheckedIOException(couldNotRead);
        }
    }

    private JsonObject recordsIn(String json) {
        try {
            JsonElement parsed = JsonParser.parseString(json);
            return parsed.isJsonObject() ? parsed.getAsJsonObject() : new JsonObject();
        } catch (JsonParseException fileWithoutRecords) {
            return new JsonObject();
        }
    }

    private void createDirectoryOfFile() throws IOException {
        Path directory = file.toAbsolutePath().getParent();
        if (directory != null) {
            Files.createDirectories(directory);
        }
    }
}
