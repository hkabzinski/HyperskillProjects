package search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Maps every word to the positions of the people it occurs in, so a whole word can be looked up
 * without comparing it against every person.
 *
 * <p>Words are expected to be normalized by the same {@link WordTokenizer} that built the index.
 */
public class InvertedIndex {
    private final Map<String, Set<Integer>> positionsByWord = new HashMap<>();
    private final int indexedPersonCount;

    public InvertedIndex(List<Person> people, WordTokenizer tokenizer) {
        for (int position = 0; position < people.size(); position++) {
            indexWordsOf(people.get(position), position, tokenizer);
        }
        this.indexedPersonCount = people.size();
    }

    public Set<Integer> positionsOf(String word) {
        return Set.copyOf(positionsByWord.getOrDefault(word, Set.of()));
    }

    public Set<Integer> allPositions() {
        return IntStream.range(0, indexedPersonCount).boxed().collect(Collectors.toSet());
    }

    private void indexWordsOf(Person person, int position, WordTokenizer tokenizer) {
        for (String word : tokenizer.wordsIn(person.details())) {
            positionsByWord.computeIfAbsent(word, indexedWord -> new HashSet<>()).add(position);
        }
    }
}
