package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonDirectoryTest {
    private static final String ERICK_IN_CAPITALS = "ERICK";
    private static final String QUERY_WITH_EXTRA_SPACES = "   erick    harrington   ";
    private static final String QUERY_OF_DIFFERENT_PEOPLE = "katie erick";
    private static final String UNKNOWN_QUERY = "john";
    private static final String PART_OF_A_WORD = "rick";

    private PersonDirectory directory;
    private MatchingStrategy anyWordStrategy;
    private MatchingStrategy allWordsStrategy;

    @BeforeEach
    void createDirectoryOverSixPeople() {
        directory = TestPeople.directoryOf(TestPeople.SIX_PEOPLE);
        anyWordStrategy = new AnyWordMatchingStrategy();
        allWordsStrategy = new AllWordsMatchingStrategy();
    }

    @Test
    void allPeople_sixPeopleInTheDirectory_returnsThemInTheOriginalOrder() {
        assertEquals(TestPeople.SIX_PEOPLE, directory.allPeople());
    }

    @Test
    void allPeople_anyDirectory_returnsAnUnmodifiableList() {
        List<Person> people = directory.allPeople();

        assertThrows(UnsupportedOperationException.class, () -> people.add(TestPeople.KATIE_JACOBS));
    }

    @Test
    void findPeople_queryInCapitals_ignoresLetterCase() {
        assertEquals(List.of(TestPeople.ERICK_HARRINGTON, TestPeople.ERICK_BURGESS),
                directory.findPeople(ERICK_IN_CAPITALS, anyWordStrategy));
    }

    @Test
    void findPeople_queryWithExtraSpaces_ignoresThem() {
        assertEquals(List.of(TestPeople.ERICK_HARRINGTON),
                directory.findPeople(QUERY_WITH_EXTRA_SPACES, allWordsStrategy));
    }

    @Test
    void findPeople_severalMatches_returnsThemInTheOriginalOrder() {
        assertEquals(
                List.of(TestPeople.KATIE_JACOBS, TestPeople.ERICK_HARRINGTON, TestPeople.ERICK_BURGESS),
                directory.findPeople(QUERY_OF_DIFFERENT_PEOPLE, anyWordStrategy));
    }

    @Test
    void findPeople_queryNobodyMatches_returnsNoPeople() {
        assertEquals(List.of(), directory.findPeople(UNKNOWN_QUERY, anyWordStrategy));
    }

    @Test
    void findPeople_partOfAWord_returnsNoPeople() {
        assertEquals(List.of(), directory.findPeople(PART_OF_A_WORD, anyWordStrategy));
    }

    @Test
    void findPeople_emptyDirectory_returnsNoPeople() {
        PersonDirectory emptyDirectory = TestPeople.directoryOf(List.of());

        assertEquals(List.of(), emptyDirectory.findPeople(ERICK_IN_CAPITALS, anyWordStrategy));
    }
}
