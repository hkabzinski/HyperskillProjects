package tictactoe;

public class BoardFormatter {
    private static final String BORDER = "-".repeat(Board.SIZE * 2 + 3);

    public String format(Board board) {
        StringBuilder text = new StringBuilder(BORDER).append(System.lineSeparator());
        for (int row = 1; row <= Board.SIZE; row++) {
            text.append(formatRow(board, row)).append(System.lineSeparator());
        }
        return text.append(BORDER).toString();
    }

    private String formatRow(Board board, int row) {
        StringBuilder text = new StringBuilder("|");
        for (int column = 1; column <= Board.SIZE; column++) {
            text.append(' ').append(board.markAt(new Coordinates(row, column)).symbol());
        }
        return text.append(" |").toString();
    }
}
