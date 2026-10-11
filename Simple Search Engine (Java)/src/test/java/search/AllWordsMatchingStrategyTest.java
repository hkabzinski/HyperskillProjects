package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllWordsMatchingStrategyTest {
    private static final List<String> WORDS_OF_ONE_PERSON = List.of("harrington", "erick");
    private static final List<String> WORDS_OF_DIFFERENT_PEOPLE = List.of("erick", "dwight");
    private static final List<String> KNOWN_AND_UNKNOWN_WORD = List.of("erick", "qqq");
    private static final List<String> WORD_OF_TWO_PEOPLE = List.of("erick");

    private InvertedIndex index;
    private MatchingStrategy strategy;

    @BeforeEach
    void createStrategyOverSixPeople() {
        index = TestPeople.indexOf(TestPeople.SIX_PEOPLE);
        strategy = new AllWordsMatchingStrategy();
    }

    @Test
    void matchingPositions_wordsOfTheSamePerson_returnsThatPerson() {
        assertEquals(Set.of(TestPeople.ERICK_HARRINGTON_POSITION),
                strategy.matchingPositions(WORDS_OF_ONE_PERSON, index));
    }

    @Test
    void matchingPositions_wordsOfDifferentPeople_returnsNoPositions() {
        assertEquals(Set.of(), strategy.matchingPositions(WORDS_OF_DIFFERENT_PEOPLE, index));
    }

    @Test
    void matchingPositions_queryContainingAnUnknownWord_returnsNoPositions() {
        assertEquals(Set.of(), strategy.matchingPositions(KNOWN_AND_UNKNOWN_WORD, index));
    }

    @Test
    void matchingPositions_singleWordOfTwoPeople_returnsBothPositions() {
        assertEquals(
                Set.of(TestPeople.ERICK_HARRINGTON_POSITION, TestPeople.ERICK_BURGESS_POSITION),
                strategy.matchingPositions(WORD_OF_TWO_PEOPLE, index));
    }

    @Test
    void matchingPositions_queryWithoutWords_returnsNoPositions() {
        assertEquals(Set.of(), strategy.matchingPositions(List.of(), index));
    }
}
