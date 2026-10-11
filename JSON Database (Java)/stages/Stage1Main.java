package server;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Stage 1: a database of 1000 cells that stores, returns and deletes text, driven from the console.
 */
public class Main {
    private static final int CELLS = 1000;
    private static final String EMPTY_CELL = "";
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String SET = "set";
    private static final String GET = "get";
    private static final String DELETE = "delete";
    private static final String EXIT = "exit";
    private static final String SEPARATOR = " ";
    private static final int COMMAND_PARTS = 3;

    public static void main(String[] args) {
        String[] cells = new String[CELLS];
        Arrays.fill(cells, EMPTY_CELL);
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String[] command = scanner.nextLine().trim().split(SEPARATOR, COMMAND_PARTS);
            if (EXIT.equals(command[0])) {
                return;
            }
            System.out.println(answerTo(command, cells));
        }
    }

    private static String answerTo(String[] command, String[] cells) {
        int cell = Integer.parseInt(command[1]) - 1;
        if (cell < 0 || cell >= cells.length) {
            return ERROR;
        }
        return switch (command[0]) {
            case SET -> store(command[2], cell, cells);
            case GET -> read(cell, cells);
            case DELETE -> delete(cell, cells);
            default -> ERROR;
        };
    }

    private static String store(String text, int cell, String[] cells) {
        cells[cell] = text;
        return OK;
    }

    private static String read(int cell, String[] cells) {
        return cells[cell].isEmpty() ? ERROR : cells[cell];
    }

    private static String delete(int cell, String[] cells) {
        cells[cell] = EMPTY_CELL;
        return OK;
    }
}
