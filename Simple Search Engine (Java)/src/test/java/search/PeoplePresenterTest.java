package search;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PeoplePresenterTest {
    private static final String TWO_PEOPLE_REPORT = """
            2 persons found:
            Erick Harrington harrington@gmail.com
            Erick Burgess
            """;
    private static final String ALL_PEOPLE_REPORT = """
            === List of people ===
            Katie Jacobs
            Myrtle Medina
            """;
    private static final String NO_MATCHES_REPORT = "No matching people found.\n";
    private static final String EMPTY_LIST_REPORT = "=== List of people ===\n";

    private ScriptedIO io;
    private PeoplePresenter presenter;

    @BeforeEach
    void createPresenter() {
        io = new ScriptedIO();
        presenter = new PeoplePresenter(io);
    }

    @Test
    void printSearchResults_twoPeople_printsTheirCountThenTheirDetails() {
        presenter.printSearchResults(List.of(TestPeople.ERICK_HARRINGTON, TestPeople.ERICK_BURGESS));

        assertEquals(TWO_PEOPLE_REPORT, io.output());
    }

    @Test
    void printSearchResults_noPeople_printsOnlyTheNoMatchesMessage() {
        presenter.printSearchResults(List.of());

        assertEquals(NO_MATCHES_REPORT, io.output());
    }

    @Test
    void printAllPeople_twoPeople_printsTheHeaderThenTheirDetails() {
        presenter.printAllPeople(List.of(TestPeople.KATIE_JACOBS, TestPeople.MYRTLE_MEDINA));

        assertEquals(ALL_PEOPLE_REPORT, io.output());
    }

    @Test
    void printAllPeople_noPeople_printsOnlyTheHeader() {
        presenter.printAllPeople(List.of());

        assertEquals(EMPTY_LIST_REPORT, io.output());
    }
}
