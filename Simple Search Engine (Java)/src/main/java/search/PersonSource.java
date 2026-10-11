package search;

import java.util.List;

/**
 * Where the people to search through come from, so the search engine does not depend on files.
 */
public interface PersonSource {
    List<Person> readPeople();
}
