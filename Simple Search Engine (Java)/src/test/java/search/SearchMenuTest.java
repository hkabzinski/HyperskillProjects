package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SearchMenuTest {
    private static final String FIND_PERSON = "1";
    private static final String PRINT_ALL_PEOPLE = "2";
    private static final String EXIT = "0";
    private static final String UNLISTED_OPTION = "3";
    private static final String ALL_STRATEGY = "ALL";
    private static final String ANY_STRATEGY = "ANY";
    private static final String NONE_STRATEGY = "NONE";
    private static final String UNKNOWN_STRATEGY = "SOME";
    private static final String LEFTOVER_INPUT = "1";

    private static final String WORDS_OF_THREE_PEOPLE = "Katie Erick QQQ";
    private static final String WORDS_OF_ONE_PERSON = "Harrington Erick";
    private static final String WORDS_TO_EXCLUDE = "djo@gmail.com ERICK";
    private static final String QUERY_NOBODY_MATCHES = "john";

    private static final String MENU_THEN_FAREWELL = """

            === Menu ===
            1. Find a person
            2. Print all persons
            0. Exit

            Bye!
            """;
    private static final String MENU_COMPLAINT_AND_MENU_AGAIN = """

            === Menu ===
            1. Find a person
            2. Print all persons
            0. Exit

            Incorrect option! Try again.

            === Menu ===
            1. Find a person
            2. Print all persons
            0. Exit

            Bye!
            """;
    private static final String THREE_PEOPLE_FOUND = """
            3 persons found:
            Katie Jacobs
            Erick Harrington harrington@gmail.com
            Erick Burgess
            """;
    private static final String ONE_PERSON_FOUND = """
            1 persons found:
            Erick Harrington harrington@gmail.com
            """;
    private static final String PEOPLE_WITHOUT_THE_QUERY_WORDS = """
            3 persons found:
            Rene Webb webb@gmail.com
            Katie Jacobs
            Myrtle Medina
            """;
    private static final String ALL_SIX_PEOPLE = """
            === List of people ===
            Dwight Joseph djo@gmail.com
            Rene Webb webb@gmail.com
            Katie Jacobs
            Erick Harrington harrington@gmail.com
            Myrtle Medina
            Erick Burgess
            """;
    private static final String STRATEGY_QUESTION = "Select a matching strategy: ALL, ANY, NONE";

    private ScriptedIO io;

    @Test
    void run_exitChosen_printsTheMenuAndSaysGoodbye() {
        assertEquals(MENU_THEN_FAREWELL, runMenuWith(EXIT));
    }

    @Test
    void run_optionThatIsNotOnTheMenu_complainsAndShowsTheMenuAgain() {
        assertEquals(MENU_COMPLAINT_AND_MENU_AGAIN, runMenuWith(UNLISTED_OPTION, EXIT));
    }

    @Test
    void run_exitChosen_stopsReadingFurtherInput() {
        runMenuWith(EXIT, LEFTOVER_INPUT);

        assertTrue(io.hasUnreadInput());
    }

    @Test
    void run_findWithAnyStrategy_printsEveryPersonMatchingAtLeastOneWord() {
        String output = runMenuWith(FIND_PERSON, ANY_STRATEGY, WORDS_OF_THREE_PEOPLE, EXIT);

        assertTrue(output.contains(THREE_PEOPLE_FOUND));
    }

    @Test
    void run_findWithAllStrategy_printsOnlyThePeopleMatchingEveryWord() {
        String output = runMenuWith(FIND_PERSON, ALL_STRATEGY, WORDS_OF_ONE_PERSON, EXIT);

        assertTrue(output.contains(ONE_PERSON_FOUND));
    }

    @Test
    void run_findWithNoneStrategy_printsThePeopleMatchingNoWord() {
        String output = runMenuWith(FIND_PERSON, NONE_STRATEGY, WORDS_TO_EXCLUDE, EXIT);

        assertTrue(output.contains(PEOPLE_WITHOUT_THE_QUERY_WORDS));
    }

    @Test
    void run_findChosen_asksWhichStrategyToUse() {
        String output = runMenuWith(FIND_PERSON, ANY_STRATEGY, WORDS_OF_THREE_PEOPLE, EXIT);

        assertTrue(output.contains(STRATEGY_QUESTION));
    }

    @Test
    void run_findChosen_asksForTheSearchQuery() {
        String output = runMenuWith(FIND_PERSON, ANY_STRATEGY, WORDS_OF_THREE_PEOPLE, EXIT);

        assertTrue(output.contains(SearchMenu.QUERY_PROMPT));
    }

    @Test
    void run_strategyThatDoesNotExist_complainsWithoutAskingForAQuery() {
        String output = runMenuWith(FIND_PERSON, UNKNOWN_STRATEGY, EXIT);

        assertTrue(output.contains(SearchMenu.INCORRECT_STRATEGY_MESSAGE));
    }

    @Test
    void run_queryNobodyMatches_printsTheNoMatchesMessage() {
        String output = runMenuWith(FIND_PERSON, ANY_STRATEGY, QUERY_NOBODY_MATCHES, EXIT);

        assertTrue(output.contains(PeoplePresenter.NO_MATCHES_MESSAGE));
    }

    @Test
    void run_printAllPeopleChosen_printsEveryPersonInTheDirectory() {
        String output = runMenuWith(PRINT_ALL_PEOPLE, EXIT);

        assertTrue(output.contains(ALL_SIX_PEOPLE));
    }

    private String runMenuWith(String... inputLines) {
        io = new ScriptedIO(inputLines);
        new SearchMenu(io,
                TestPeople.directoryOf(TestPeople.SIX_PEOPLE),
                new PeoplePresenter(io),
                MatchingStrategyCatalog.withDefaultStrategies()).run();
        return io.output();
    }
}
