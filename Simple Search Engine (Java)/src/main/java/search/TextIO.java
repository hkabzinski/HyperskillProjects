package search;

/**
 * The way the program talks to the user, so the search engine itself does not depend on the console.
 */
public interface TextIO {
    String readLine();

    void print(String text);

    void println(String text);
}
