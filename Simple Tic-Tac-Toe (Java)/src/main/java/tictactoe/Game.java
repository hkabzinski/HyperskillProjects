package tictactoe;

public class Game {
    static final String PROMPT = "Enter the coordinates: ";

    private final GameIO io;
    private final Board board = new Board();
    private final BoardFormatter formatter = new BoardFormatter();
    private final CoordinatesParser parser = new CoordinatesParser();
    private final GameStateAnalyzer analyzer = new GameStateAnalyzer();

    public Game(GameIO io) {
        this.io = io;
    }

    public void play() {
        showBoard();

        Mark player = Mark.X;
        GameState state = GameState.NOT_FINISHED;
        while (state == GameState.NOT_FINISHED) {
            makeMove(player);
            showBoard();
            state = analyzer.analyze(board);
            player = player.opponent();
        }

        io.println(state.message());
    }

    private void makeMove(Mark player) {
        boolean moved = false;
        while (!moved) {
            io.print(PROMPT);
            try {
                board.place(parser.parse(io.readLine()), player);
                moved = true;
            } catch (InvalidMoveException e) {
                io.println(e.getMessage());
            }
        }
    }

    private void showBoard() {
        io.println(formatter.format(board));
    }
}
