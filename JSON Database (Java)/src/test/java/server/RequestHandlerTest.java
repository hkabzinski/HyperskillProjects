package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.InvalidRequestException;
import protocol.Request;
import protocol.RequestType;
import protocol.Response;

class RequestHandlerTest {
    private static final String SORABH = "Sorabh";
    private static final JsonElement NAME_KEY = new JsonPrimitive("name");
    private static final JsonElement PERSON_SURNAME_KEY = JsonParser.parseString("[\"person\",\"surname\"]");
    private static final JsonElement PERSON_KEY = JsonParser.parseString("[\"person\"]");
    private static final JsonElement NO_KEY = null;
    private static final JsonElement NO_VALUE = null;
    private static final String PERSON_WITHOUT_A_SURNAME = "{\"person\":{}}";
    private static final String PERSON_WITH_A_SURNAME = "{\"surname\":\"Sorabh\"}";

    private InMemoryStorage storage;
    private ShutdownSignal shutdownSignal;
    private RequestHandler handler;

    @BeforeEach
    void createHandler() {
        storage = new InMemoryStorage();
        shutdownSignal = new ShutdownSignal();
        Database database = new JsonDatabase(storage, new ReentrantReadWriteLock());
        handler = new RequestHandler(database, shutdownSignal);
    }

    @Test
    void handle_setRequest_answersThatItSucceeded() {
        assertEquals(Response.ok(), handler.handle(setRequest(NAME_KEY, SORABH)));
    }

    @Test
    void handle_setRequest_storesTheValue() {
        handler.handle(setRequest(NAME_KEY, SORABH));

        assertEquals(Response.ok(new JsonPrimitive(SORABH)), handler.handle(getRequest(NAME_KEY)));
    }

    @Test
    void handle_setRequestWithoutAValue_answersWithTheMissingValueReason() {
        Request requestWithoutValue = new Request(RequestType.SET, NAME_KEY, NO_VALUE);

        assertEquals(Response.error(RequestHandler.MISSING_VALUE_REASON), handler.handle(requestWithoutValue));
    }

    @Test
    void handle_setRequestWithANestedKey_storesTheValueInsideTheObject() {
        handler.handle(setRequest(PERSON_SURNAME_KEY, SORABH));

        assertEquals(Response.ok(new JsonPrimitive(SORABH)), handler.handle(getRequest(PERSON_SURNAME_KEY)));
    }

    @Test
    void handle_getRequestForAKeyThatWasNeverSet_answersWithTheNoSuchKeyReason() {
        assertEquals(Response.error(RequestHandler.NO_SUCH_KEY_REASON), handler.handle(getRequest(NAME_KEY)));
    }

    @Test
    void handle_getRequestForAKeyHoldingAnObject_answersWithTheWholeObject() {
        handler.handle(setRequest(PERSON_SURNAME_KEY, SORABH));

        assertEquals(Response.ok(JsonParser.parseString(PERSON_WITH_A_SURNAME)),
                handler.handle(getRequest(PERSON_KEY)));
    }

    @Test
    void handle_deleteRequestForAStoredKey_answersThatItSucceeded() {
        handler.handle(setRequest(NAME_KEY, SORABH));

        assertEquals(Response.ok(), handler.handle(deleteRequest(NAME_KEY)));
    }

    @Test
    void handle_deleteRequestForAKeyThatWasNeverSet_answersWithTheNoSuchKeyReason() {
        assertEquals(Response.error(RequestHandler.NO_SUCH_KEY_REASON), handler.handle(deleteRequest(NAME_KEY)));
    }

    @Test
    void handle_deleteRequestWithANestedKey_keepsTheObjectThatHeldTheRecord() {
        handler.handle(setRequest(PERSON_SURNAME_KEY, SORABH));

        handler.handle(deleteRequest(PERSON_SURNAME_KEY));

        assertEquals(PERSON_WITHOUT_A_SURNAME, storage.savedRecords());
    }

    @Test
    void handle_requestWithoutAKey_throwsInvalidRequestException() {
        Request requestWithoutKey = getRequest(NO_KEY);

        assertThrows(InvalidRequestException.class, () -> handler.handle(requestWithoutKey));
    }

    @Test
    void handle_exitRequest_answersThatItSucceeded() {
        assertEquals(Response.ok(), handler.handle(exitRequest()));
    }

    @Test
    void handle_exitRequest_raisesTheShutdownSignal() {
        handler.handle(exitRequest());

        assertTrue(shutdownSignal.isRaised());
    }

    @Test
    void handle_getRequest_leavesTheShutdownSignalDown() {
        handler.handle(getRequest(NAME_KEY));

        assertFalse(shutdownSignal.isRaised());
    }

    private Request setRequest(JsonElement key, String value) {
        return new Request(RequestType.SET, key, new JsonPrimitive(value));
    }

    private Request getRequest(JsonElement key) {
        return new Request(RequestType.GET, key, NO_VALUE);
    }

    private Request deleteRequest(JsonElement key) {
        return new Request(RequestType.DELETE, key, NO_VALUE);
    }

    private Request exitRequest() {
        return new Request(RequestType.EXIT, NO_KEY, NO_VALUE);
    }
}
