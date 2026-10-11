package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import protocol.RequestType;

class ClientArgumentsTest {
    private static final String NAME_KEY = "name";
    private static final String STORED_VALUE = "Sorabh Tomar";
    private static final String REQUEST_FILE_NAME = "testSet.json";
    private static final String[] SET_A_VALUE = {"-t", "set", "-k", "name", "-v", "Sorabh Tomar"};
    private static final String[] GET_A_VALUE = {"-t", "get", "-k", "name"};
    private static final String[] READ_A_REQUEST_FROM_A_FILE = {"-in", "testSet.json"};
    private static final String[] UNKNOWN_TYPE = {"-t", "store"};
    private static final String[] NO_OPTIONS = {};

    @Test
    void parsedFrom_typeKeyAndValue_readsAllThreeOptions() {
        ClientArguments arguments = ClientArguments.parsedFrom(SET_A_VALUE);

        assertEquals(Optional.of(STORED_VALUE), arguments.value());
    }

    @Test
    void parsedFrom_typeOfAKnownRequest_readsThatRequestType() {
        ClientArguments arguments = ClientArguments.parsedFrom(SET_A_VALUE);

        assertEquals(Optional.of(RequestType.SET), arguments.requestType());
    }

    @Test
    void parsedFrom_keyOption_readsTheKey() {
        ClientArguments arguments = ClientArguments.parsedFrom(GET_A_VALUE);

        assertEquals(Optional.of(NAME_KEY), arguments.key());
    }

    @Test
    void parsedFrom_requestWithoutAValue_readsNoValue() {
        ClientArguments arguments = ClientArguments.parsedFrom(GET_A_VALUE);

        assertTrue(arguments.value().isEmpty());
    }

    @Test
    void parsedFrom_inputFileOption_readsTheFileName() {
        ClientArguments arguments = ClientArguments.parsedFrom(READ_A_REQUEST_FROM_A_FILE);

        assertEquals(Optional.of(REQUEST_FILE_NAME), arguments.inputFileName());
    }

    @Test
    void parsedFrom_typeOfNoKnownRequest_readsNoRequestType() {
        ClientArguments arguments = ClientArguments.parsedFrom(UNKNOWN_TYPE);

        assertTrue(arguments.requestType().isEmpty());
    }

    @Test
    void parsedFrom_noOptionsAtAll_readsNeitherTypeNorKeyNorFileName() {
        ClientArguments arguments = ClientArguments.parsedFrom(NO_OPTIONS);

        assertEquals(Optional.empty(), arguments.requestType());
        assertEquals(Optional.empty(), arguments.key());
        assertEquals(Optional.empty(), arguments.inputFileName());
    }
}
