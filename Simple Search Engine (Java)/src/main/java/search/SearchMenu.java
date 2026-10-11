package search;

import java.util.Arrays;
import java.util.Optional;

/**
 * Drives the dialogue with the user: shows the menu and carries out the chosen action.
 */
public class SearchMenu {
    static final String MENU_HEADER = "=== Menu ===";
    static final String INCORRECT_OPTION_MESSAGE = "Incorrect option! Try again.";
    static final String STRATEGY_PROMPT = "Select a matching strategy: ";
    static final String INCORRECT_STRATEGY_MESSAGE = "Incorrect strategy! Try again.";
    static final String QUERY_PROMPT = "Enter a name or email to search all suitable people.";
    static final String FAREWELL_MESSAGE = "Bye!";
    private static final String BLANK_LINE = "";

    private final TextIO io;
    private final PersonDirectory directory;
    private final PeoplePresenter presenter;
    private final MatchingStrategyCatalog strategies;

    public SearchMenu(TextIO io,
                      PersonDirectory directory,
                      PeoplePresenter presenter,
                      MatchingStrategyCatalog strategies) {
        this.io = io;
        this.directory = directory;
        this.presenter = presenter;
        this.strategies = strategies;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            running = executeChosenOption();
        }
        io.println(FAREWELL_MESSAGE);
    }

    private void showMenu() {
        io.println(BLANK_LINE);
        io.println(MENU_HEADER);
        Arrays.stream(MenuOption.values()).forEach(option -> io.println(option.menuLine()));
    }

    private boolean executeChosenOption() {
        Optional<MenuOption> chosen = MenuOption.forInput(io.readLine());
        io.println(BLANK_LINE);
        if (chosen.isEmpty()) {
            io.println(INCORRECT_OPTION_MESSAGE);
            return true;
        }
        switch (chosen.get()) {
            case FIND_PERSON -> findPeople();
            case PRINT_ALL_PEOPLE -> presenter.printAllPeople(directory.allPeople());
            case EXIT -> {
                return false;
            }
        }
        return true;
    }

    private void findPeople() {
        Optional<MatchingStrategy> strategy = askForStrategy();
        if (strategy.isEmpty()) {
            io.println(INCORRECT_STRATEGY_MESSAGE);
            return;
        }
        presenter.printSearchResults(directory.findPeople(askForQuery(), strategy.get()));
    }

    private Optional<MatchingStrategy> askForStrategy() {
        io.println(STRATEGY_PROMPT + strategies.availableNames());
        Optional<MatchingStrategy> strategy = strategies.findByName(io.readLine());
        io.println(BLANK_LINE);
        return strategy;
    }

    private String askForQuery() {
        io.println(QUERY_PROMPT);
        String query = io.readLine();
        io.println(BLANK_LINE);
        return query;
    }
}
