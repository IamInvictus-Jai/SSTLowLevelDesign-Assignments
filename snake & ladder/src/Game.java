import java.util.*;

public class Game {

    private final Difficulty difficulty;
    private final Board board;
    private final Dice dice;
    private final Queue<Player> players;
    private final List<Player> winners;
    private final GameObserver observer;
    private final InputProvider input;

    public Game(int size, List<String> playerNames,
                Difficulty difficulty,
                GameObserver observer,
                InputProvider input) {

        this.difficulty = difficulty;
        this.observer = observer;
        this.input = input;

        this.players = new LinkedList<>();
        for (String name : playerNames) {
            players.add(new Player(name, 0));
        }

        this.board = new Board(size);
        this.board.initialize(5, 5);

        this.dice = new Dice(6);
        this.winners = new ArrayList<>();
    }

    public void startGame() {

        while (players.size() > 1) {

            Player player = players.poll();
            int initialPos = player.getPosition();

            int consecutiveSix = 0;
            boolean turnOver = false;

            while (!turnOver) {

                input.waitForNextTurn();

                int diceVal = dice.roll();
                observer.onDiceRoll(player, diceVal);

                if (diceVal == 6) {
                    consecutiveSix++;

                    if (difficulty == Difficulty.HARD && consecutiveSix == 3) {
                        player.setPosition(initialPos);
                        break;
                    }
                } else {
                    consecutiveSix = 0;
                    turnOver = true;
                }

                int oldPos = player.getPosition();
                int tentative = oldPos + diceVal;

                if (tentative <= board.getLastCell()) {
                    int finalPos = board.getFinalPosition(tentative);
                    player.setPosition(finalPos);
                    observer.onMove(player, oldPos, finalPos);
                }

                if (diceVal != 6) {
                    turnOver = true;
                }
            }

            if (player.getPosition() == board.getLastCell()) {
                winners.add(player);
                observer.onWin(player);
            } else {
                players.offer(player);
            }
        }
    }
}