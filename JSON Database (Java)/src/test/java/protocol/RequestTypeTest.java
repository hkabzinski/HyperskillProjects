package protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class RequestTypeTest {
    private static final String SET_NAME = "set";
    private static final String DELETE_IN_CAPITALS = "DELETE";
    private static final String NAME_OF_NO_REQUEST = "store";

    @Test
    void forName_nameOfAKnownRequest_returnsThatType() {
        assertEquals(Optional.of(RequestType.SET), RequestType.forName(SET_NAME));
    }

    @Test
    void forName_nameInCapitals_returnsThatType() {
        assertEquals(Optional.of(RequestType.DELETE), RequestType.forName(DELETE_IN_CAPITALS));
    }

    @Test
    void forName_nameOfNoKnownRequest_returnsNothing() {
        assertTrue(RequestType.forName(NAME_OF_NO_REQUEST).isEmpty());
    }
}
