package protocol;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.util.List;

/**
 * The names leading to one record in the database, such as ["person", "surname"].
 * A path always holds at least one name, except for the parent of a top level record,
 * which is the database itself.
 */
public record RecordPath(List<String> names) {
    static final String MISSING_KEY_REASON = "No key";
    static final String UNSUPPORTED_KEY_REASON = "A key must be a name or an array of names";

    public RecordPath(List<String> names) {
        this.names = List.copyOf(names);
    }

    public static RecordPath fromKey(JsonElement key) {
        if (key == null || key.isJsonNull()) {
            throw new InvalidRequestException(MISSING_KEY_REASON);
        }
        if (key.isJsonPrimitive()) {
            return new RecordPath(List.of(key.getAsString()));
        }
        if (key.isJsonArray()) {
            return fromNames(key.getAsJsonArray());
        }
        throw new InvalidRequestException(UNSUPPORTED_KEY_REASON);
    }

    private static RecordPath fromNames(JsonArray names) {
        if (names.isEmpty()) {
            throw new InvalidRequestException(MISSING_KEY_REASON);
        }
        return new RecordPath(names.asList().stream().map(JsonElement::getAsString).toList());
    }

    public RecordPath parent() {
        return new RecordPath(names.subList(0, names.size() - 1));
    }

    public String lastName() {
        return names.get(names.size() - 1);
    }
}
