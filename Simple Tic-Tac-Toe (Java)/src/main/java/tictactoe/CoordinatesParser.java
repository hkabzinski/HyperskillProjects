package tictactoe;

public class CoordinatesParser {
    static final String NOT_NUMBERS_MESSAGE = "You should enter numbers!";
    private static final int COORDINATE_COUNT = 2;

    public Coordinates parse(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length != COORDINATE_COUNT) {
            throw new InvalidMoveException(NOT_NUMBERS_MESSAGE);
        }
        try {
            return new Coordinates(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } catch (NumberFormatException e) {
            throw new InvalidMoveException(NOT_NUMBERS_MESSAGE);
        }
    }
}
