package search;

import java.util.List;

/**
 * The sample dataset the tests search through, in the order it appears in the data file.
 */
final class TestPeople {
    static final Person DWIGHT_JOSEPH = new Person("Dwight Joseph djo@gmail.com");
    static final Person RENE_WEBB = new Person("Rene Webb webb@gmail.com");
    static final Person KATIE_JACOBS = new Person("Katie Jacobs");
    static final Person ERICK_HARRINGTON = new Person("Erick Harrington harrington@gmail.com");
    static final Person MYRTLE_MEDINA = new Person("Myrtle Medina");
    static final Person ERICK_BURGESS = new Person("Erick Burgess");

    static final List<Person> SIX_PEOPLE = List.of(
            DWIGHT_JOSEPH, RENE_WEBB, KATIE_JACOBS, ERICK_HARRINGTON, MYRTLE_MEDINA, ERICK_BURGESS);

    static final int DWIGHT_JOSEPH_POSITION = 0;
    static final int RENE_WEBB_POSITION = 1;
    static final int KATIE_JACOBS_POSITION = 2;
    static final int ERICK_HARRINGTON_POSITION = 3;
    static final int MYRTLE_MEDINA_POSITION = 4;
    static final int ERICK_BURGESS_POSITION = 5;

    static InvertedIndex indexOf(List<Person> people) {
        return new InvertedIndex(people, new WordTokenizer());
    }

    static PersonDirectory directoryOf(List<Person> people) {
        WordTokenizer tokenizer = new WordTokenizer();
        return new PersonDirectory(people, new InvertedIndex(people, tokenizer), tokenizer);
    }

    private TestPeople() {
    }
}
