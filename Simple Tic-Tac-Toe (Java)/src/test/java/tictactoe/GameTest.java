package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GameTest {
    private static final String EMPTY_BOARD = """
            ---------
            |       |
            |       |
            |       |
            ---------
            """;
    private static final String BOARD_WITH_X_IN_CENTER = """
            ---------
            |       |
            |   X   |
            |       |
            ---------
            """;
    private static final String BOARD_WITH_O_IN_CORNER = """
            ---------
            | O     |
            |   X   |
            |       |
            ---------
            """;
    private static final String[] X_WINS_ON_TOP_ROW = {"1 1", "2 1", "1 2", "2 2", "1 3"};
    private static final String[] O_WINS_ON_MIDDLE_ROW = {"1 1", "2 1", "1 2", "2 2", "3 3", "2 3"};
    private static final String[] DRAW = {"1 1", "1 2", "1 3", "2 2", "2 1", "2 3", "3 2", "3 1", "3 3"};

    @Test
    void play_atStart_printsEmptyBoardThenPrompt() {
        String output = playGame(X_WINS_ON_TOP_ROW);

        assertTrue(output.startsWith(normalized(EMPTY_BOARD) + Game.PROMPT));
    }

    @Test
    void play_firstMove_placesX() {
        String output = playGame("2 2", "1 1", "1 2", "2 1", "3 2");

        assertTrue(output.contains(normalized(BOARD_WITH_X_IN_CENTER)));
    }

    @Test
    void play_secondMove_placesO() {
        String output = playGame("2 2", "1 1", "1 2", "2 1", "3 2");

        assertTrue(output.contains(normalized(BOARD_WITH_O_IN_CORNER)));
    }

    @Test
    void play_xCompletesALine_endsWithXWins() {
        String output = playGame(X_WINS_ON_TOP_ROW);

        assertTrue(output.endsWith(GameState.X_WINS.message() + "\n"));
    }

    @Test
    void play_oCompletesALine_endsWithOWins() {
        String output = playGame(O_WINS_ON_MIDDLE_ROW);

        assertTrue(output.endsWith(GameState.O_WINS.message() + "\n"));
    }

    @Test
    void play_boardFillsWithoutALine_endsWithDraw() {
        String output = playGame(DRAW);

        assertTrue(output.endsWith(GameState.DRAW.message() + "\n"));
    }

    @Test
    void play_gameIsWon_stopsReadingInput() {
        ScriptedIO io = new ScriptedIO("1 1", "2 1", "1 2", "2 2", "1 3", "3 3");

        new Game(io).play();

        assertTrue(io.hasUnreadInput());
    }

    @Test
    void play_occupiedCell_printsOccupiedMessageAndAsksAgain() {
        String output = playGame("1 1", "1 1", "2 1", "1 2", "2 2", "1 3");

        assertTrue(output.contains(Board.OCCUPIED_MESSAGE + "\n" + Game.PROMPT));
    }

    @Test
    void play_nonNumericInput_printsNotNumbersMessageAndAsksAgain() {
        String output = playGame("two two", "1 1", "2 1", "1 2", "2 2", "1 3");

        assertTrue(output.contains(CoordinatesParser.NOT_NUMBERS_MESSAGE + "\n" + Game.PROMPT));
    }

    @Test
    void play_coordinatesOutsideTheBoard_printsOutOfRangeMessageAndAsksAgain() {
        String output = playGame("1 4", "1 1", "2 1", "1 2", "2 2", "1 3");

        assertTrue(output.contains(Board.OUT_OF_RANGE_MESSAGE + "\n" + Game.PROMPT));
    }

    @Test
    void play_invalidMove_doesNotPassTheTurnToTheOpponent() {
        String output = playGame("2 2", "2 2", "1 1", "1 2", "2 1", "3 2");

        assertTrue(output.contains(normalized(BOARD_WITH_O_IN_CORNER)));
    }

    @Test
    void play_invalidMove_doesNotReprintTheBoard() {
        String output = playGame("9 9", "1 1", "2 1", "1 2", "2 2", "1 3");

        assertFalse(output.contains(Board.OUT_OF_RANGE_MESSAGE + "\n-"));
    }

    @Test
    void play_exampleFromTheTask_endsWithXWins() {
        String output = playGame(
                "2 2", "2 2", "two two", "1 4", "1 1", "3 3", "2 1", "3 1", "2 3", "3 2");

        String finalBoard = """
                ---------
                | O     |
                | O X O |
                | X X X |
                ---------
                """;
        assertEquals(normalized(finalBoard) + GameState.X_WINS.message() + "\n",
                output.substring(output.lastIndexOf(Game.PROMPT) + Game.PROMPT.length()));
    }

    private static String playGame(String... inputLines) {
        ScriptedIO io = new ScriptedIO(inputLines);
        new Game(io).play();
        return io.output().replace(System.lineSeparator(), "\n");
    }

    private static String normalized(String board) {
        return board.replace("\r\n", "\n");
    }
}
