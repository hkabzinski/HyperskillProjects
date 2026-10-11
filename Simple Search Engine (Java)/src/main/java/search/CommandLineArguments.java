package search;

import java.util.List;
import java.util.Optional;

/**
 * Reads the options the program was started with.
 */
public class CommandLineArguments {
    static final String DATA_OPTION = "--data";

    private final List<String> arguments;

    public CommandLineArguments(String... arguments) {
        this.arguments = List.of(arguments);
    }

    public Optional<String> dataFileName() {
        int optionPosition = arguments.indexOf(DATA_OPTION);
        int valuePosition = optionPosition + 1;
        if (optionPosition < 0 || valuePosition >= arguments.size()) {
            return Optional.empty();
        }
        return Optional.of(arguments.get(valuePosition));
    }
}
