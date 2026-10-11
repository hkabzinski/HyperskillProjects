package protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecordPathTest {
    private static final String PERSON = "person";
    private static final String SURNAME = "surname";
    private static final String NAMES_OF_A_NESTED_RECORD = "[\"person\", \"surname\"]";

    @Test
    void fromKey_singleName_holdsThatOneName() {
        RecordPath path = RecordPath.fromKey(new JsonPrimitive(PERSON));

        assertEquals(List.of(PERSON), path.names());
    }

    @Test
    void fromKey_arrayOfNames_holdsEveryNameInOrder() {
        RecordPath path = RecordPath.fromKey(JsonParser.parseString(NAMES_OF_A_NESTED_RECORD));

        assertEquals(List.of(PERSON, SURNAME), path.names());
    }

    @Test
    void fromKey_noKeyAtAll_throwsInvalidRequestException() {
        assertThrows(InvalidRequestException.class, () -> RecordPath.fromKey(null));
    }

    @Test
    void fromKey_keyThatIsJsonNull_throwsInvalidRequestException() {
        JsonElement missingKey = JsonNull.INSTANCE;

        assertThrows(InvalidRequestException.class, () -> RecordPath.fromKey(missingKey));
    }

    @Test
    void fromKey_emptyArrayOfNames_throwsInvalidRequestException() {
        JsonElement emptyKey = new JsonArray();

        assertThrows(InvalidRequestException.class, () -> RecordPath.fromKey(emptyKey));
    }

    @Test
    void fromKey_keyThatIsNeitherNameNorArray_throwsInvalidRequestException() {
        JsonElement unsupportedKey = new JsonObject();

        assertThrows(InvalidRequestException.class, () -> RecordPath.fromKey(unsupportedKey));
    }

    @Test
    void parent_pathOfTwoNames_dropsTheLastName() {
        RecordPath path = new RecordPath(List.of(PERSON, SURNAME));

        assertEquals(List.of(PERSON), path.parent().names());
    }

    @Test
    void parent_pathOfOneName_leadsToTheDatabaseItself() {
        RecordPath path = new RecordPath(List.of(PERSON));

        assertEquals(List.of(), path.parent().names());
    }

    @Test
    void lastName_pathOfTwoNames_returnsTheNameOfTheRecord() {
        RecordPath path = new RecordPath(List.of(PERSON, SURNAME));

        assertEquals(SURNAME, path.lastName());
    }
}
