package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Stage 2: read the people from the standard input, then answer a fixed number of substring queries.
 */
public class Main {
    private static final String NO_MATCHES_MESSAGE = "No matching people found.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> people = readPeople(scanner);
        answerQueries(scanner, people);
    }

    private static List<String> readPeople(Scanner scanner) {
        System.out.println("Enter the number of people:");
        int count = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Enter all people:");
        List<String> people = new ArrayList<>();
        for (int person = 0; person < count; person++) {
            people.add(scanner.nextLine());
        }
        return people;
    }

    private static void answerQueries(Scanner scanner, List<String> people) {
        System.out.println("\nEnter the number of search queries:");
        int queryCount = Integer.parseInt(scanner.nextLine().trim());

        for (int query = 0; query < queryCount; query++) {
            System.out.println("\nEnter data to search people:");
            printMatches(people, scanner.nextLine());
        }
    }

    private static void printMatches(List<String> people, String query) {
        List<String> found = people.stream()
                .filter(person -> contains(person, query))
                .toList();

        if (found.isEmpty()) {
            System.out.println(NO_MATCHES_MESSAGE);
            return;
        }
        System.out.println("\nFound people:");
        found.forEach(System.out::println);
    }

    private static boolean contains(String person, String query) {
        return normalized(person).contains(normalized(query));
    }

    private static String normalized(String text) {
        return text.trim().replaceAll("\s+", " ").toLowerCase(Locale.ROOT);
    }
}
