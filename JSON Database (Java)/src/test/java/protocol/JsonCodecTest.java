package protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonCodecTest {
    private static final String NAME_KEY = "name";
    private static final String STORED_VALUE = "Sorabh";
    private static final String SET_REQUEST = "{\"type\":\"set\",\"key\":\"name\",\"value\":\"Sorabh\"}";
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String EXIT_REQUEST = "{\"type\":\"exit\"}";
    private static final String REQUEST_WITH_NESTED_KEY = "{\"type\":\"get\",\"key\":[\"person\",\"surname\"]}";
    private static final String NESTED_KEY = "[\"person\",\"surname\"]";
    private static final String REQUEST_WITHOUT_TYPE = "{\"key\":\"name\"}";
    private static final String REQUEST_WITH_UNKNOWN_TYPE = "{\"type\":\"store\",\"key\":\"name\"}";
    private static final String TEXT_THAT_IS_NOT_JSON = "give me a record";
    private static final String OK_RESPONSE = "{\"response\":\"OK\"}";
    private static final String OK_RESPONSE_WITH_VALUE = "{\"response\":\"OK\",\"value\":\"Sorabh\"}";
    private static final String ERROR_RESPONSE = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
    private static final String NO_SUCH_KEY = "No such key";

    private JsonCodec codec;

    @BeforeEach
    void createCodec() {
        codec = new JsonCodec();
    }

    @Test
    void toJson_setRequest_writesTypeKeyAndValue() {
        Request request = new Request(RequestType.SET, new JsonPrimitive(NAME_KEY), new JsonPrimitive(STORED_VALUE));

        assertEquals(SET_REQUEST, codec.toJson(request));
    }

    @Test
    void toJson_getRequest_leavesOutTheValue() {
        Request request = new Request(RequestType.GET, new JsonPrimitive(NAME_KEY), null);

        assertEquals(GET_REQUEST, codec.toJson(request));
    }

    @Test
    void toJson_exitRequest_writesOnlyTheType() {
        Request request = new Request(RequestType.EXIT, null, null);

        assertEquals(EXIT_REQUEST, codec.toJson(request));
    }

    @Test
    void toJson_successfulResponse_writesOnlyTheResponse() {
        assertEquals(OK_RESPONSE, codec.toJson(Response.ok()));
    }

    @Test
    void toJson_successfulResponseWithAValue_writesTheResponseAndTheValue() {
        assertEquals(OK_RESPONSE_WITH_VALUE, codec.toJson(Response.ok(new JsonPrimitive(STORED_VALUE))));
    }

    @Test
    void toJson_failedResponse_writesTheResponseAndTheReason() {
        assertEquals(ERROR_RESPONSE, codec.toJson(Response.error(NO_SUCH_KEY)));
    }

    @Test
    void parseRequest_setRequest_readsTypeKeyAndValue() {
        Request request = codec.parseRequest(SET_REQUEST);

        assertEquals(new Request(RequestType.SET, new JsonPrimitive(NAME_KEY), new JsonPrimitive(STORED_VALUE)),
                request);
    }

    @Test
    void parseRequest_requestWithoutAType_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> codec.parseRequest(REQUEST_WITHOUT_TYPE));
    }

    @Test
    void parseRequest_requestWithATypeTheServerDoesNotKnow_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> codec.parseRequest(REQUEST_WITH_UNKNOWN_TYPE));
    }

    @Test
    void parseRequest_textThatIsNotJson_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> codec.parseRequest(TEXT_THAT_IS_NOT_JSON));
    }

    @Test
    void parseRequest_keyWrittenAsAnArray_readsItAsAJsonArray() {
        JsonElement expectedKey = JsonParser.parseString(NESTED_KEY);

        assertEquals(expectedKey, codec.parseRequest(REQUEST_WITH_NESTED_KEY).key());
    }
}
