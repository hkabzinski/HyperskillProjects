package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import protocol.RecordPath;

class RecordTreeTest {
    private static final String PERSON = "person";
    private static final String NAME = "name";
    private static final String SURNAME = "surname";
    private static final String AGE = "age";
    private static final String TEXT = "text";
    private static final String ADAM = "Adam";
    private static final String SMITH = "Smith";
    private static final String GREETING = "Hello World!";
    private static final String AGE_OF_THE_PERSON = "25";
    private static final String NO_RECORDS = "{}";

    private static final RecordPath TEXT_PATH = new RecordPath(List.of(TEXT));
    private static final RecordPath PERSON_PATH = new RecordPath(List.of(PERSON));
    private static final RecordPath PERSON_NAME_PATH = new RecordPath(List.of(PERSON, NAME));
    private static final RecordPath PERSON_SURNAME_PATH = new RecordPath(List.of(PERSON, SURNAME));
    private static final RecordPath PERSON_AGE_PATH = new RecordPath(List.of(PERSON, AGE));
    private static final RecordPath PATH_THROUGH_A_PLAIN_VALUE = new RecordPath(List.of(TEXT, NAME));
    private static final RecordPath DEEPLY_NESTED_PATH = new RecordPath(List.of("person1", "inside1", "inside2"));

    private static final String TREE_WITH_A_PERSON = """
            {"text": "Hello World!", "person": {"name": "Adam", "surname": "Smith"}}""";
    private static final String PERSON_WITHOUT_A_SURNAME = """
            {"text": "Hello World!", "person": {"name": "Adam"}}""";
    private static final String PERSON_WITH_ANOTHER_NAME = """
            {"text": "Hello World!", "person": {"name": "Eve"}}""";
    private static final String PERSON_WITH_AN_AGE = """
            {"text": "Hello World!", "person": {"name": "Adam", "surname": "Smith", "age": "25"}}""";
    private static final String GREETING_STORED_ALONE = """
            {"text": "Hello World!"}""";
    private static final String OBJECTS_CREATED_ALONG_A_DEEP_PATH = """
            {"person1": {"inside1": {"inside2": "Smith"}}}""";

    @Test
    void find_nameOfARecordAtTheTop_returnsItsValue() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertEquals(Optional.of(new JsonPrimitive(GREETING)), tree.find(TEXT_PATH));
    }

    @Test
    void find_pathIntoANestedObject_returnsTheNestedValue() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertEquals(Optional.of(new JsonPrimitive(SMITH)), tree.find(PERSON_SURNAME_PATH));
    }

    @Test
    void find_pathToAWholeObject_returnsThatObject() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertEquals(Optional.of(jsonOf(TREE_WITH_A_PERSON).get(PERSON)), tree.find(PERSON_PATH));
    }

    @Test
    void find_nameThatIsNotStored_returnsNothing() {
        RecordTree tree = treeOf(PERSON_WITHOUT_A_SURNAME);

        assertEquals(Optional.empty(), tree.find(PERSON_SURNAME_PATH));
    }

    @Test
    void find_pathLeadingThroughAPlainValue_returnsNothing() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertEquals(Optional.empty(), tree.find(PATH_THROUGH_A_PLAIN_VALUE));
    }

    @Test
    void put_nameThatIsNotStoredYet_storesTheValueAtTheTop() {
        RecordTree tree = treeOf(NO_RECORDS);

        tree.put(TEXT_PATH, new JsonPrimitive(GREETING));

        assertEquals(jsonOf(GREETING_STORED_ALONE), tree.asJsonObject());
    }

    @Test
    void put_nameThatAlreadyHasAValue_replacesThatValue() {
        RecordTree tree = treeOf(PERSON_WITH_ANOTHER_NAME);

        tree.put(PERSON_NAME_PATH, new JsonPrimitive(ADAM));

        assertEquals(jsonOf(PERSON_WITHOUT_A_SURNAME), tree.asJsonObject());
    }

    @Test
    void put_nameInsideAnExistingObject_keepsTheOtherRecordsOfThatObject() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        tree.put(PERSON_AGE_PATH, new JsonPrimitive(AGE_OF_THE_PERSON));

        assertEquals(jsonOf(PERSON_WITH_AN_AGE), tree.asJsonObject());
    }

    @Test
    void put_pathWhoseObjectsAreMissing_createsEveryObjectOnTheWay() {
        RecordTree tree = treeOf(NO_RECORDS);

        tree.put(DEEPLY_NESTED_PATH, new JsonPrimitive(SMITH));

        assertEquals(jsonOf(OBJECTS_CREATED_ALONG_A_DEEP_PATH), tree.asJsonObject());
    }

    @Test
    void put_objectAsTheValue_storesTheWholeObject() {
        RecordTree tree = treeOf(NO_RECORDS);

        tree.put(PERSON_PATH, jsonOf(TREE_WITH_A_PERSON).get(PERSON));

        assertEquals(Optional.of(jsonOf(TREE_WITH_A_PERSON).get(PERSON)), tree.find(PERSON_PATH));
    }

    @Test
    void remove_nestedName_removesOnlyThatRecord() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        tree.remove(PERSON_SURNAME_PATH);

        assertEquals(jsonOf(PERSON_WITHOUT_A_SURNAME), tree.asJsonObject());
    }

    @Test
    void remove_nameOfAStoredRecord_reportsThatItWasRemoved() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertTrue(tree.remove(PERSON_SURNAME_PATH));
    }

    @Test
    void remove_nameThatIsNotStored_reportsThatNothingWasRemoved() {
        RecordTree tree = treeOf(PERSON_WITHOUT_A_SURNAME);

        assertFalse(tree.remove(PERSON_SURNAME_PATH));
    }

    @Test
    void remove_pathLeadingThroughAPlainValue_reportsThatNothingWasRemoved() {
        RecordTree tree = treeOf(TREE_WITH_A_PERSON);

        assertFalse(tree.remove(PATH_THROUGH_A_PLAIN_VALUE));
    }

    private RecordTree treeOf(String json) {
        return new RecordTree(jsonOf(json));
    }

    private JsonObject jsonOf(String json) {
        JsonElement parsed = JsonParser.parseString(json);
        return parsed.getAsJsonObject();
    }
}
