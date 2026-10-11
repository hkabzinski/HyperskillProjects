package tictactoe;

public class GameStateAnalyzer {
    public GameState analyze(Board board) {
        if (board.hasLineOf(Mark.X)) {
            return GameState.X_WINS;
        }
        if (board.hasLineOf(Mark.O)) {
            return GameState.O_WINS;
        }
        return board.isFull() ? GameState.DRAW : GameState.NOT_FINISHED;
    }
}
