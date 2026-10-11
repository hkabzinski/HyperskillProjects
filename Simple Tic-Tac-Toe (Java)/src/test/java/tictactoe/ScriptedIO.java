package tictactoe;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * Feeds prepared input lines to the game and records everything it prints.
 */
class ScriptedIO implements GameIO {
    private final Queue<String> inputLines;
    private final StringBuilder output = new StringBuilder();

    ScriptedIO(String... inputLines) {
        this.inputLines = new ArrayDeque<>(List.of(inputLines));
    }

    @Override
    public String readLine() {
        return inputLines.remove();
    }

    @Override
    public void print(String text) {
        output.append(text);
    }

    @Override
    public void println(String text) {
        output.append(text).append('\n');
    }

    String output() {
        return output.toString();
    }

    boolean hasUnreadInput() {
        return !inputLines.isEmpty();
    }
}
