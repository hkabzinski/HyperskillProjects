package console;

/**
 * The way a program reports to the user, so neither the server nor the client depends on the console.
 */
public interface TextIO {
    void println(String text);
}
