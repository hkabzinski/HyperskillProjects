package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CoordinatesParserTest {
    private final CoordinatesParser parser = new CoordinatesParser();

    @Test
    void parse_twoNumbers_returnsRowThenColumn() {
        assertEquals(new Coordinates(3, 1), parser.parse("3 1"));
    }

    @Test
    void parse_numbersWithExtraWhitespace_ignoresTheWhitespace() {
        assertEquals(new Coordinates(1, 2), parser.parse("  1   2 "));
    }

    @Test
    void parse_numbersOutsideTheBoard_stillReturnsThem() {
        assertEquals(new Coordinates(4, 1), parser.parse("4 1"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"one", "one one", "1 one", "1", "1 2 3", "", "1.5 2"})
    void parse_notTwoWholeNumbers_throwsNotNumbersMessage(String input) {
        InvalidMoveException exception =
                assertThrows(InvalidMoveException.class, () -> parser.parse(input));

        assertEquals(CoordinatesParser.NOT_NUMBERS_MESSAGE, exception.getMessage());
    }
}
