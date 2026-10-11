package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InvertedIndexTest {
    private static final String WORD_SHARED_BY_TWO_PEOPLE = "erick";
    private static final String WORD_OF_ONE_PERSON = "jacobs";
    private static final String EMAIL_OF_ONE_PERSON = "harrington@gmail.com";
    private static final String UNKNOWN_WORD = "qqq";
    private static final Set<Integer> ALL_SIX_POSITIONS = Set.of(0, 1, 2, 3, 4, 5);

    private InvertedIndex index;

    @BeforeEach
    void indexSixPeople() {
        index = TestPeople.indexOf(TestPeople.SIX_PEOPLE);
    }

    @Test
    void positionsOf_wordOfTwoPeople_returnsBothPositions() {
        assertEquals(
                Set.of(TestPeople.ERICK_HARRINGTON_POSITION, TestPeople.ERICK_BURGESS_POSITION),
                index.positionsOf(WORD_SHARED_BY_TWO_PEOPLE));
    }

    @Test
    void positionsOf_wordOfOnePerson_returnsThatPosition() {
        assertEquals(Set.of(TestPeople.KATIE_JACOBS_POSITION), index.positionsOf(WORD_OF_ONE_PERSON));
    }

    @Test
    void positionsOf_emailOfOnePerson_returnsThatPosition() {
        assertEquals(
                Set.of(TestPeople.ERICK_HARRINGTON_POSITION), index.positionsOf(EMAIL_OF_ONE_PERSON));
    }

    @Test
    void positionsOf_unknownWord_returnsNoPositions() {
        assertEquals(Set.of(), index.positionsOf(UNKNOWN_WORD));
    }

    @Test
    void positionsOf_knownWord_returnsAnUnmodifiableSet() {
        Set<Integer> positions = index.positionsOf(WORD_SHARED_BY_TWO_PEOPLE);

        assertThrows(UnsupportedOperationException.class,
                () -> positions.add(TestPeople.KATIE_JACOBS_POSITION));
    }

    @Test
    void allPositions_sixIndexedPeople_returnsEveryPosition() {
        assertEquals(ALL_SIX_POSITIONS, index.allPositions());
    }

    @Test
    void allPositions_noIndexedPeople_returnsNoPositions() {
        InvertedIndex emptyIndex = TestPeople.indexOf(List.of());

        assertEquals(Set.of(), emptyIndex.allPositions());
    }
}
