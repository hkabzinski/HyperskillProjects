package tictactoe;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MainTest {
    private static final String X_WINS_ON_TOP_ROW = "1 1\n2 1\n1 2\n2 2\n1 3\n";

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreStandardStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void main_withWinningMovesOnStandardInput_printsXWins() {
        ByteArrayOutputStream written = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(X_WINS_ON_TOP_ROW.getBytes()));
        System.setOut(new PrintStream(written));

        Main.main(new String[0]);

        assertTrue(written.toString().trim().endsWith(GameState.X_WINS.message()));
    }
}
