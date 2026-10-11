package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MarkTest {

    @Test
    void opponent_ofX_isO() {
        assertEquals(Mark.O, Mark.X.opponent());
    }

    @Test
    void opponent_ofO_isX() {
        assertEquals(Mark.X, Mark.O.opponent());
    }

    @Test
    void opponent_ofEmpty_throwsIllegalStateException() {
        assertThrows(IllegalStateException.class, Mark.EMPTY::opponent);
    }

    @Test
    void symbol_ofEmpty_isSpace() {
        assertEquals(' ', Mark.EMPTY.symbol());
    }
}
