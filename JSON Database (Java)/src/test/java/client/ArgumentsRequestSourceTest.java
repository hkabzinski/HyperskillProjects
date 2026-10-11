package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import protocol.InvalidRequestException;
import protocol.JsonCodec;

class ArgumentsRequestSourceTest {
    private static final String[] SET_A_VALUE = {"-t", "set", "-k", "name", "-v", "Sorabh Tomar"};
    private static final String[] GET_A_VALUE = {"-t", "get", "-k", "name"};
    private static final String[] DELETE_A_VALUE = {"-t", "delete", "-k", "name"};
    private static final String[] EXIT = {"-t", "exit"};
    private static final String[] GET_WITHOUT_A_KEY = {"-t", "get"};
    private static final String[] VALUE_WITHOUT_A_TYPE = {"-k", "name", "-v", "Sorabh"};
    private static final String SET_REQUEST = "{\"type\":\"set\",\"key\":\"name\",\"value\":\"Sorabh Tomar\"}";
    private static final String GET_REQUEST = "{\"type\":\"get\",\"key\":\"name\"}";
    private static final String DELETE_REQUEST = "{\"type\":\"delete\",\"key\":\"name\"}";
    private static final String EXIT_REQUEST = "{\"type\":\"exit\"}";

    @Test
    void readRequest_typeKeyAndValue_buildsASetRequest() {
        assertEquals(SET_REQUEST, requestSourceFor(SET_A_VALUE).readRequest());
    }

    @Test
    void readRequest_typeAndKeyOfAGet_buildsAGetRequestWithoutAValue() {
        assertEquals(GET_REQUEST, requestSourceFor(GET_A_VALUE).readRequest());
    }

    @Test
    void readRequest_typeAndKeyOfADelete_buildsADeleteRequest() {
        assertEquals(DELETE_REQUEST, requestSourceFor(DELETE_A_VALUE).readRequest());
    }

    @Test
    void readRequest_exitType_buildsAnExitRequestWithoutAKey() {
        assertEquals(EXIT_REQUEST, requestSourceFor(EXIT).readRequest());
    }

    @Test
    void readRequest_requestWithoutAType_throwsInvalidRequestException() {
        RequestSource source = requestSourceFor(VALUE_WITHOUT_A_TYPE);

        assertThrows(InvalidRequestException.class, source::readRequest);
    }

    @Test
    void readRequest_getWithoutAKey_throwsInvalidRequestException() {
        RequestSource source = requestSourceFor(GET_WITHOUT_A_KEY);

        assertThrows(InvalidRequestException.class, source::readRequest);
    }

    private RequestSource requestSourceFor(String... arguments) {
        return new ArgumentsRequestSource(ClientArguments.parsedFrom(arguments), new JsonCodec());
    }
}
