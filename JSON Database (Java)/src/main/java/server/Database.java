package server;

import com.google.gson.JsonElement;
import java.util.Optional;
import protocol.RecordPath;

/**
 * The records the server keeps, each addressed by the path of names that leads to it.
 */
public interface Database {
    Optional<JsonElement> read(RecordPath path);

    void write(RecordPath path, JsonElement value);

    boolean delete(RecordPath path);
}
