package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tictactoe.TestBoards.boardOf;

import org.junit.jupiter.api.Test;

class BoardFormatterTest {
    private static final String BORDER = "---------";

    private final BoardFormatter formatter = new BoardFormatter();

    @Test
    void format_emptyBoard_printsBlankCellsInsideBorders() {
        String expected = String.join(System.lineSeparator(),
                BORDER,
                "|       |",
                "|       |",
                "|       |",
                BORDER);

        assertEquals(expected, formatter.format(new Board()));
    }

    @Test
    void format_boardWithMarks_printsEachMarkInItsCell() {
        String expected = String.join(System.lineSeparator(),
                BORDER,
                "| X O   |",
                "| O O X |",
                "|   X   |",
                BORDER);

        assertEquals(expected, formatter.format(boardOf("XO_OOX_X_")));
    }
}
