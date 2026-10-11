package server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Optional;
import protocol.RecordPath;

/**
 * A tree of records: finds, stores and removes the value that sits at a path of names,
 * creating the objects a new path needs along the way.
 */
public class RecordTree {
    private final JsonObject root;

    public RecordTree(JsonObject root) {
        this.root = root;
    }

    public Optional<JsonElement> find(RecordPath path) {
        JsonElement found = root;
        for (String name : path.names()) {
            if (!(found instanceof JsonObject object) || !object.has(name)) {
                return Optional.empty();
            }
            found = object.get(name);
        }
        return Optional.of(found);
    }

    public void put(RecordPath path, JsonElement value) {
        objectAt(path.parent()).add(path.lastName(), value);
    }

    public boolean remove(RecordPath path) {
        return find(path.parent())
                .filter(JsonElement::isJsonObject)
                .map(JsonElement::getAsJsonObject)
                .map(parent -> parent.remove(path.lastName()) != null)
                .orElse(false);
    }

    public JsonObject asJsonObject() {
        return root;
    }

    private JsonObject objectAt(RecordPath path) {
        JsonObject object = root;
        for (String name : path.names()) {
            object = childObject(object, name);
        }
        return object;
    }

    private JsonObject childObject(JsonObject parent, String name) {
        if (parent.get(name) instanceof JsonObject child) {
            return child;
        }
        JsonObject created = new JsonObject();
        parent.add(name, created);
        return created;
    }
}
