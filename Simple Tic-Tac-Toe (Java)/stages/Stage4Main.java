package tictactoe;

import java.util.Scanner;

public class Main {
    private static final int SIZE = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] grid = parseGrid(scanner.nextLine().trim());
        printGrid(grid);

        while (true) {
            String[] parts = scanner.nextLine().trim().split("\\s+");

            int row;
            int col;
            try {
                if (parts.length != 2) {
                    throw new NumberFormatException();
                }
                row = Integer.parseInt(parts[0]);
                col = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("You should enter numbers!");
                continue;
            }

            if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (grid[row - 1][col - 1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }

            grid[row - 1][col - 1] = 'X';
            break;
        }

        printGrid(grid);
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
}
