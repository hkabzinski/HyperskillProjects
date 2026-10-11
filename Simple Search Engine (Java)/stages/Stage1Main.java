package search;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Stage 1: print the one-based index of a word inside a line of words.
 */
public class Main {
    private static final String NOT_FOUND_MESSAGE = "Not found";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> words = Arrays.asList(scanner.nextLine().trim().split("\s+"));
        String searchedWord = scanner.nextLine().trim();

        int position = words.indexOf(searchedWord);
        System.out.println(position < 0 ? NOT_FOUND_MESSAGE : position + 1);
    }
}
