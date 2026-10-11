package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NoWordsMatchingStrategyTest {
    private static final List<String> WORDS_OF_THREE_PEOPLE = List.of("djo@gmail.com", "erick");
    private static final List<String> UNKNOWN_WORD = List.of("qqq");
    private static final Set<Integer> ALL_SIX_POSITIONS = Set.of(0, 1, 2, 3, 4, 5);

    private InvertedIndex index;
    private MatchingStrategy strategy;

    @BeforeEach
    void createStrategyOverSixPeople() {
        index = TestPeople.indexOf(TestPeople.SIX_PEOPLE);
        strategy = new NoWordsMatchingStrategy(new AnyWordMatchingStrategy());
    }

    @Test
    void matchingPositions_wordsOfThreePeople_returnsTheRemainingPeople() {
        assertEquals(
                Set.of(TestPeople.RENE_WEBB_POSITION, TestPeople.KATIE_JACOBS_POSITION,
                        TestPeople.MYRTLE_MEDINA_POSITION),
                strategy.matchingPositions(WORDS_OF_THREE_PEOPLE, index));
    }

    @Test
    void matchingPositions_unknownWord_returnsEveryPosition() {
        assertEquals(ALL_SIX_POSITIONS, strategy.matchingPositions(UNKNOWN_WORD, index));
    }

    @Test
    void matchingPositions_queryWithoutWords_returnsEveryPosition() {
        assertEquals(ALL_SIX_POSITIONS, strategy.matchingPositions(List.of(), index));
    }

    @Test
    void matchingPositions_noIndexedPeople_returnsNoPositions() {
        InvertedIndex emptyIndex = TestPeople.indexOf(List.of());

        assertEquals(Set.of(), strategy.matchingPositions(UNKNOWN_WORD, emptyIndex));
    }
}
