package search;

import java.util.List;
import java.util.Set;

/**
 * The people the program searches through, looked up through an inverted index.
 */
public class PersonDirectory {
    private final List<Person> people;
    private final InvertedIndex index;
    private final WordTokenizer tokenizer;

    public PersonDirectory(List<Person> people, InvertedIndex index, WordTokenizer tokenizer) {
        this.people = List.copyOf(people);
        this.index = index;
        this.tokenizer = tokenizer;
    }

    public List<Person> allPeople() {
        return people;
    }

    public List<Person> findPeople(String query, MatchingStrategy strategy) {
        Set<Integer> positions = strategy.matchingPositions(tokenizer.wordsIn(query), index);
        return positions.stream()
                .sorted()
                .map(people::get)
                .toList();
    }
}
