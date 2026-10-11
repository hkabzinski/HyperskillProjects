package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tictactoe.TestBoards.boardOf;

import org.junit.jupiter.api.Test;

class GameStateAnalyzerTest {
    private final GameStateAnalyzer analyzer = new GameStateAnalyzer();

    @Test
    void analyze_emptyBoard_isNotFinished() {
        assertEquals(GameState.NOT_FINISHED, analyzer.analyze(new Board()));
    }

    @Test
    void analyze_noLineAndEmptyCells_isNotFinished() {
        assertEquals(GameState.NOT_FINISHED, analyzer.analyze(boardOf("XO_OOX_X_")));
    }

    @Test
    void analyze_noLineAndFullBoard_isDraw() {
        assertEquals(GameState.DRAW, analyzer.analyze(boardOf("XOXOOXXXO")));
    }

    @Test
    void analyze_lineOfXWithEmptyCells_isXWins() {
        assertEquals(GameState.X_WINS, analyzer.analyze(boardOf("XXXOO__O_")));
    }

    @Test
    void analyze_lineOfXOnFullBoard_isXWins() {
        assertEquals(GameState.X_WINS, analyzer.analyze(boardOf("XOXOXOXXO")));
    }

    @Test
    void analyze_lineOfO_isOWins() {
        assertEquals(GameState.O_WINS, analyzer.analyze(boardOf("XOOOXOXXO")));
    }
}
