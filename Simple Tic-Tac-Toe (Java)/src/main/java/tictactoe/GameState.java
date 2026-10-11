package tictactoe;

public enum GameState {
    NOT_FINISHED("Game not finished"),
    DRAW("Draw"),
    X_WINS("X wins"),
    O_WINS("O wins");

    private final String message;

    GameState(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
