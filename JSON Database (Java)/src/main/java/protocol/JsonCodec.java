package protocol;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Translates requests and responses between objects and the JSON text sent over the connection.
 */
public class JsonCodec {
    static final String MALFORMED_REQUEST_REASON = "The request is not valid JSON";
    static final String UNKNOWN_TYPE_REASON = "Unknown request type";

    private final Gson gson = new Gson();

    public String toJson(Request request) {
        return gson.toJson(request);
    }

    public String toJson(Response response) {
        return gson.toJson(response);
    }

    public Request parseRequest(String json) {
        Request request = parsed(json);
        if (request == null || request.type() == null) {
            throw new InvalidRequestException(UNKNOWN_TYPE_REASON);
        }
        return request;
    }

    private Request parsed(String json) {
        try {
            return gson.fromJson(json, Request.class);
        } catch (JsonParseException malformedJson) {
            throw new InvalidRequestException(MALFORMED_REQUEST_REASON);
        }
    }
}
