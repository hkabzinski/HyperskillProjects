package search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class CommandLineArgumentsTest {
    private static final String DATA_FILE_NAME = "people.txt";
    private static final String UNRELATED_OPTION = "--verbose";

    @Test
    void dataFileName_dataOptionFollowedByName_returnsThatName() {
        CommandLineArguments arguments =
                new CommandLineArguments(CommandLineArguments.DATA_OPTION, DATA_FILE_NAME);

        assertEquals(Optional.of(DATA_FILE_NAME), arguments.dataFileName());
    }

    @Test
    void dataFileName_dataOptionAfterAnotherOption_returnsTheName() {
        CommandLineArguments arguments = new CommandLineArguments(
                UNRELATED_OPTION, CommandLineArguments.DATA_OPTION, DATA_FILE_NAME);

        assertEquals(Optional.of(DATA_FILE_NAME), arguments.dataFileName());
    }

    @Test
    void dataFileName_noArguments_returnsNothing() {
        CommandLineArguments arguments = new CommandLineArguments();

        assertTrue(arguments.dataFileName().isEmpty());
    }

    @Test
    void dataFileName_dataOptionWithoutName_returnsNothing() {
        CommandLineArguments arguments = new CommandLineArguments(CommandLineArguments.DATA_OPTION);

        assertTrue(arguments.dataFileName().isEmpty());
    }

    @Test
    void dataFileName_otherOptionOnly_returnsNothing() {
        CommandLineArguments arguments = new CommandLineArguments(UNRELATED_OPTION, DATA_FILE_NAME);

        assertTrue(arguments.dataFileName().isEmpty());
    }
}
