package tictactoe;

/**
 * The way the game talks to the players, so the game itself does not depend on the console.
 */
public interface GameIO {
    String readLine();

    void print(String text);

    void println(String text);
}
