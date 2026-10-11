package protocol;

import com.google.gson.JsonElement;

/**
 * The server's answer to one request: whether it succeeded, the value a get found,
 * and the reason a failed request gives.
 */
public record Response(String response, JsonElement value, String reason) {
    static final String SUCCESS = "OK";
    static final String FAILURE = "ERROR";

    public static Response ok() {
        return new Response(SUCCESS, null, null);
    }

    public static Response ok(JsonElement value) {
        return new Response(SUCCESS, value, null);
    }

    public static Response error(String reason) {
        return new Response(FAILURE, null, reason);
    }
}
