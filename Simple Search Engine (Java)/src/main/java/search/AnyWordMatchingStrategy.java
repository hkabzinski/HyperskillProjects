package search;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matches the people that contain at least one word of the query.
 */
public class AnyWordMatchingStrategy implements MatchingStrategy {
    @Override
    public Set<Integer> matchingPositions(List<String> queryWords, InvertedIndex index) {
        return queryWords.stream()
                .map(index::positionsOf)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
