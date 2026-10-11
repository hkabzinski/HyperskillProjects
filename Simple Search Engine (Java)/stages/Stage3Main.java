package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Stage 3: the same substring search, driven by a menu instead of a fixed number of queries.
 */
public class Main {
    private static final String MENU = """
            === Menu ===
            1. Find a person
            2. Print all people
            0. Exit""";
    private static final String NO_MATCHES_MESSAGE = "No matching people found.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        List<String> people = readPeople(scanner);
        runMenu(scanner, people);
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

    private static void runMenu(Scanner scanner, List<String> people) {
        while (true) {
            System.out.println("\n" + MENU);
            switch (scanner.nextLine().trim()) {
                case "1" -> findPeople(scanner, people);
                case "2" -> printAllPeople(people);
                case "0" -> {
                    System.out.println("\nBye!");
                    return;
                }
                default -> System.out.println("\nIncorrect option! Try again.");
            }
        }
    }

    private static void findPeople(Scanner scanner, List<String> people) {
        System.out.println("\nEnter a name or email to search all suitable people.");
        String query = scanner.nextLine();

        List<String> found = people.stream()
                .filter(person -> normalized(person).contains(normalized(query)))
                .toList();

        if (found.isEmpty()) {
            System.out.println(NO_MATCHES_MESSAGE);
            return;
        }
        found.forEach(System.out::println);
    }

    private static void printAllPeople(List<String> people) {
        System.out.println("\n=== List of people ===");
        people.forEach(System.out::println);
    }

    private static String normalized(String text) {
        return text.trim().replaceAll("\s+", " ").toLowerCase(Locale.ROOT);
    }
}
