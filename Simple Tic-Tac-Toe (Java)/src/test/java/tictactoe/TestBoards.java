package tictactoe;

/**
 * Builds boards from the nine-character notation used in the task ('X', 'O', '_' for empty).
 */
final class TestBoards {
    private TestBoards() {
    }

    static Board boardOf(String cells) {
        Board board = new Board();
        for (int i = 0; i < cells.length(); i++) {
            char cell = cells.charAt(i);
            if (cell != '_') {
                Coordinates coordinates = new Coordinates(i / Board.SIZE + 1, i % Board.SIZE + 1);
                board.place(coordinates, Mark.valueOf(String.valueOf(cell)));
            }
        }
        return board;
    }
}
