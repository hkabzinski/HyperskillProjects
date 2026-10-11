package tictactoe;

public enum Mark {
    X('X'),
    O('O'),
    EMPTY(' ');

    private final char symbol;

    Mark(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    public Mark opponent() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> throw new IllegalStateException("An empty mark has no opponent");
        };
    }
}
