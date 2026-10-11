package tictactoe;

/**
 * Thrown when a move cannot be made; the message is meant to be shown to the player.
 */
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}
