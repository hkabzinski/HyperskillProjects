package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnyWordMatchingStrategyTest {
    private static final List<String> WORDS_OF_FOUR_PEOPLE =
            List.of("erick", "dwight", "webb@gmail.com");
    private static final List<String> WORD_OF_TWO_PEOPLE = List.of("erick");
    private static final List<String> UNKNOWN_WORD = List.of("qqq");

    private InvertedIndex index;
    private MatchingStrategy strategy;

    @BeforeEach
    void createStrategyOverSixPeople() {
        index = TestPeople.indexOf(TestPeople.SIX_PEOPLE);
        strategy = new AnyWordMatchingStrategy();
    }

    @Test
    void matchingPositions_wordsOfSeveralPeople_returnsEveryMatchedPositionOnce() {
        assertEquals(
                Set.of(TestPeople.DWIGHT_JOSEPH_POSITION, TestPeople.RENE_WEBB_POSITION,
                        TestPeople.ERICK_HARRINGTON_POSITION, TestPeople.ERICK_BURGESS_POSITION),
                strategy.matchingPositions(WORDS_OF_FOUR_PEOPLE, index));
    }

    @Test
    void matchingPositions_wordSharedByTwoPeople_returnsBothPositions() {
        assertEquals(
                Set.of(TestPeople.ERICK_HARRINGTON_POSITION, TestPeople.ERICK_BURGESS_POSITION),
                strategy.matchingPositions(WORD_OF_TWO_PEOPLE, index));
    }

    @Test
    void matchingPositions_unknownWord_returnsNoPositions() {
        assertEquals(Set.of(), strategy.matchingPositions(UNKNOWN_WORD, index));
    }

    @Test
    void matchingPositions_queryWithoutWords_returnsNoPositions() {
        assertEquals(Set.of(), strategy.matchingPositions(List.of(), index));
    }
}
