package tictactoe;

public class Main {
    public static void main(String[] args) {
        new Game(new ConsoleIO(System.in, System.out)).play();
    }
}
