package client;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import protocol.InvalidRequestException;
import protocol.JsonCodec;
import protocol.Request;
import protocol.RequestType;

/**
 * Turns the options the client was started with into the request to send.
 */
public class ArgumentsRequestSource implements RequestSource {
    static final String MISSING_TYPE_REASON = "A request needs a type, for example -t get";
    static final String MISSING_KEY_REASON = "A get, set or delete request needs a key, for example -k name";

    private final ClientArguments arguments;
    private final JsonCodec codec;

    public ArgumentsRequestSource(ClientArguments arguments, JsonCodec codec) {
        this.arguments = arguments;
        this.codec = codec;
    }

    @Override
    public String readRequest() {
        return codec.toJson(requestFromArguments());
    }

    private Request requestFromArguments() {
        RequestType type = arguments.requestType()
                .orElseThrow(() -> new InvalidRequestException(MISSING_TYPE_REASON));
        if (type == RequestType.EXIT) {
            return new Request(type, null, null);
        }
        return new Request(type, key(), value());
    }

    private JsonElement key() {
        return new JsonPrimitive(arguments.key()
                .orElseThrow(() -> new InvalidRequestException(MISSING_KEY_REASON)));
    }

    private JsonElement value() {
        return arguments.value().map(JsonPrimitive::new).orElse(null);
    }
}
