package search;

import java.util.List;

/**
 * Writes people out for the user to read.
 */
public class PeoplePresenter {
    static final String NO_MATCHES_MESSAGE = "No matching people found.";
    static final String ALL_PEOPLE_HEADER = "=== List of people ===";
    static final String FOUND_SUFFIX = " persons found:";

    private final TextIO io;

    public PeoplePresenter(TextIO io) {
        this.io = io;
    }

    public void printSearchResults(List<Person> found) {
        if (found.isEmpty()) {
            io.println(NO_MATCHES_MESSAGE);
            return;
        }
        io.println(found.size() + FOUND_SUFFIX);
        printDetails(found);
    }

    public void printAllPeople(List<Person> people) {
        io.println(ALL_PEOPLE_HEADER);
        printDetails(people);
    }

    private void printDetails(List<Person> people) {
        people.forEach(person -> io.println(person.details()));
    }
}
