package protocol;

import com.google.gson.JsonElement;

/**
 * One request from a client: what to do, which record to do it to, and the value a set stores.
 * The key and the value are absent for the requests that do not need them.
 */
public record Request(RequestType type, JsonElement key, JsonElement value) {
}
