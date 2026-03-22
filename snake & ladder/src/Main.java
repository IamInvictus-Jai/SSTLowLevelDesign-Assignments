import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<String> players = Arrays.asList("A", "B", "C");

        GameObserver observer = new ConsoleLogger();
        InputProvider input = new ConsoleInput();

        Game game = new Game(
                10,
                players,
                Difficulty.HARD,
                observer,
                input
        );

        game.startGame();
    }
}