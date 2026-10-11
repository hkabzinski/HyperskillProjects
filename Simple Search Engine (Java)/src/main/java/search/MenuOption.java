package search;

import java.util.Arrays;
import java.util.Optional;

/**
 * The actions the user can pick from the menu, each with the number that selects it.
 */
public enum MenuOption {
    FIND_PERSON("1", "Find a person"),
    PRINT_ALL_PEOPLE("2", "Print all persons"),
    EXIT("0", "Exit");

    private static final String NUMBER_SEPARATOR = ". ";

    private final String userInput;
    private final String label;

    MenuOption(String userInput, String label) {
        this.userInput = userInput;
        this.label = label;
    }

    public static Optional<MenuOption> forInput(String input) {
        return Arrays.stream(values())
                .filter(option -> option.userInput.equals(input.trim()))
                .findFirst();
    }

    public String menuLine() {
        return userInput + NUMBER_SEPARATOR + label;
    }
}
