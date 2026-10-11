package search;

import java.util.List;
import java.util.Set;

/**
 * Decides which people a query matches, given the words of that query.
 */
public interface MatchingStrategy {
    Set<Integer> matchingPositions(List<String> queryWords, InvertedIndex index);
}
