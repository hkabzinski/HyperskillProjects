package search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Matches the people that contain every word of the query.
 */
public class AllWordsMatchingStrategy implements MatchingStrategy {
    @Override
    public Set<Integer> matchingPositions(List<String> queryWords, InvertedIndex index) {
        return queryWords.stream()
                .map(index::positionsOf)
                .reduce(AllWordsMatchingStrategy::intersectionOf)
                .orElseGet(Set::of);
    }

    private static Set<Integer> intersectionOf(Set<Integer> positions, Set<Integer> otherPositions) {
        Set<Integer> shared = new HashSet<>(positions);
        shared.retainAll(otherPositions);
        return shared;
    }
}
