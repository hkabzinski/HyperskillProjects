package search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Matches the people that contain none of the words of the query.
 */
public class NoWordsMatchingStrategy implements MatchingStrategy {
    private final MatchingStrategy anyWordStrategy;

    public NoWordsMatchingStrategy(MatchingStrategy anyWordStrategy) {
        this.anyWordStrategy = anyWordStrategy;
    }

    @Override
    public Set<Integer> matchingPositions(List<String> queryWords, InvertedIndex index) {
        Set<Integer> remaining = new HashSet<>(index.allPositions());
        remaining.removeAll(anyWordStrategy.matchingPositions(queryWords, index));
        return remaining;
    }
}
