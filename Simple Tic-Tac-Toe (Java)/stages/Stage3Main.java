package tictactoe;

import java.util.Scanner;

public class Main {
    private static final int SIZE = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] grid = parseGrid(scanner.nextLine().trim());
        printGrid(grid);
        System.out.println(gameState(grid));
    }

    private static char[][] parseGrid(String cells) {
        char[][] grid = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE * SIZE; i++) {
            char c = i < cells.length() ? cells.charAt(i) : '_';
            grid[i / SIZE][i % SIZE] = c == 'X' || c == 'O' ? c : ' ';
        }
        return grid;
    }

    private static void printGrid(char[][] grid) {
        System.out.println("---------");
        for (char[] row : grid) {
            System.out.println("| " + row[0] + " " + row[1] + " " + row[2] + " |");
        }
        System.out.println("---------");
    }

    private static String gameState(char[][] grid) {
        int xs = 0;
        int os = 0;
        for (char[] row : grid) {
            for (char c : row) {
                if (c == 'X') {
                    xs++;
                } else if (c == 'O') {
                    os++;
                }
            }
        }

        boolean xWins = wins(grid, 'X');
        boolean oWins = wins(grid, 'O');

        if (xWins && oWins || Math.abs(xs - os) > 1) {
            return "Impossible";
        }
        if (xWins) {
            return "X wins";
        }
        if (oWins) {
            return "O wins";
        }
        return xs + os == SIZE * SIZE ? "Draw" : "Game not finished";
    }

    private static boolean wins(char[][] grid, char p) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[i][0] == p && grid[i][1] == p && grid[i][2] == p) {
                return true;
            }
            if (grid[0][i] == p && grid[1][i] == p && grid[2][i] == p) {
                return true;
            }
        }
        return grid[0][0] == p && grid[1][1] == p && grid[2][2] == p
                || grid[0][2] == p && grid[1][1] == p && grid[2][0] == p;
    }
}
