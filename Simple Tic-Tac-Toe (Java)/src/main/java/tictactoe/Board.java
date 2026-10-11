package tictactoe;

import java.util.Arrays;

public class Board {
    public static final int SIZE = 3;
    static final String OUT_OF_RANGE_MESSAGE = "Coordinates should be from 1 to " + SIZE + "!";
    static final String OCCUPIED_MESSAGE = "This cell is occupied! Choose another one!";

    private final Mark[][] cells = new Mark[SIZE][SIZE];

    public Board() {
        for (Mark[] row : cells) {
            Arrays.fill(row, Mark.EMPTY);
        }
    }

    public void place(Coordinates coordinates, Mark mark) {
        if (!contains(coordinates)) {
            throw new InvalidMoveException(OUT_OF_RANGE_MESSAGE);
        }
        if (markAt(coordinates) != Mark.EMPTY) {
            throw new InvalidMoveException(OCCUPIED_MESSAGE);
        }
        cells[coordinates.row() - 1][coordinates.column() - 1] = mark;
    }

    public Mark markAt(Coordinates coordinates) {
        return cells[coordinates.row() - 1][coordinates.column() - 1];
    }

    public boolean isFull() {
        return Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .noneMatch(mark -> mark == Mark.EMPTY);
    }

    public boolean hasLineOf(Mark mark) {
        boolean mainDiagonal = true;
        boolean antiDiagonal = true;
        for (int i = 0; i < SIZE; i++) {
            if (isRowOf(mark, i) || isColumnOf(mark, i)) {
                return true;
            }
            mainDiagonal &= cells[i][i] == mark;
            antiDiagonal &= cells[i][SIZE - 1 - i] == mark;
        }
        return mainDiagonal || antiDiagonal;
    }

    private boolean contains(Coordinates coordinates) {
        return isInRange(coordinates.row()) && isInRange(coordinates.column());
    }

    private boolean isInRange(int coordinate) {
        return coordinate >= 1 && coordinate <= SIZE;
    }

    private boolean isRowOf(Mark mark, int row) {
        for (int column = 0; column < SIZE; column++) {
            if (cells[row][column] != mark) {
                return false;
            }
        }
        return true;
    }

    private boolean isColumnOf(Mark mark, int column) {
        for (int row = 0; row < SIZE; row++) {
            if (cells[row][column] != mark) {
                return false;
            }
        }
        return true;
    }
}
