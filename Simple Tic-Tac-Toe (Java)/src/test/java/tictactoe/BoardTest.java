package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tictactoe.TestBoards.boardOf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BoardTest {
    private static final Coordinates CENTER = new Coordinates(2, 2);

    @Test
    void markAt_onNewBoard_isEmpty() {
        Board board = new Board();

        assertEquals(Mark.EMPTY, board.markAt(CENTER));
    }

    @Test
    void place_onEmptyCell_storesTheMark() {
        Board board = new Board();

        board.place(CENTER, Mark.X);

        assertEquals(Mark.X, board.markAt(CENTER));
    }

    @Test
    void place_onOccupiedCell_throwsOccupiedMessage() {
        Board board = new Board();
        board.place(CENTER, Mark.X);

        InvalidMoveException exception =
                assertThrows(InvalidMoveException.class, () -> board.place(CENTER, Mark.O));

        assertEquals(Board.OCCUPIED_MESSAGE, exception.getMessage());
    }

    @Test
    void place_onOccupiedCell_keepsTheOriginalMark() {
        Board board = new Board();
        board.place(CENTER, Mark.X);

        assertThrows(InvalidMoveException.class, () -> board.place(CENTER, Mark.O));

        assertEquals(Mark.X, board.markAt(CENTER));
    }

    @ParameterizedTest
    @CsvSource({"0, 1", "4, 1", "1, 0", "1, 4", "-1, -1"})
    void place_outsideTheBoard_throwsOutOfRangeMessage(int row, int column) {
        Board board = new Board();

        InvalidMoveException exception = assertThrows(InvalidMoveException.class,
                () -> board.place(new Coordinates(row, column), Mark.X));

        assertEquals(Board.OUT_OF_RANGE_MESSAGE, exception.getMessage());
    }

    @Test
    void isFull_withEmptyCells_isFalse() {
        assertFalse(boardOf("XOXOXOXO_").isFull());
    }

    @Test
    void isFull_withNoEmptyCells_isTrue() {
        assertTrue(boardOf("XOXOOXXXO").isFull());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "XXX______", "___XXX___", "______XXX",
        "X__X__X__", "_X__X__X_", "__X__X__X",
        "X___X___X", "__X_X_X__"
    })
    void hasLineOf_withThreeInARow_isTrue(String cells) {
        assertTrue(boardOf(cells).hasLineOf(Mark.X));
    }

    @ParameterizedTest
    @ValueSource(strings = {"_________", "XX_______", "XOX______", "XOXOOXXXO"})
    void hasLineOf_withoutThreeInARow_isFalse(String cells) {
        assertFalse(boardOf(cells).hasLineOf(Mark.X));
    }

    @Test
    void hasLineOf_forTheOtherPlayersLine_isFalse() {
        assertFalse(boardOf("OOO______").hasLineOf(Mark.X));
    }
}
