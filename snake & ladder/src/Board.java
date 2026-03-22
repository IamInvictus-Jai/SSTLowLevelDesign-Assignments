import java.util.*;

public class Board {

    private final int lastCell;
    private final Map<Integer, Snake> snakes;
    private final Map<Integer, Ladder> ladders;

    public Board(int size) {
        this.lastCell = size * size;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
    }

    public int getLastCell() {
        return lastCell;
    }

    public void initialize(int numberOfSnakes, int numberOfLadders) {

        Random random = new Random();

        // Snakes
        while (snakes.size() < numberOfSnakes) {
            int head = random.nextInt(lastCell - 1) + 2;
            int tail = random.nextInt(head - 1) + 1;

            if (isValidSnake(head, tail)) {
                snakes.put(head, new Snake(head, tail));
            }
        }

        // Ladders
        while (ladders.size() < numberOfLadders) {
            int start = random.nextInt(lastCell - 1) + 1;
            int end = random.nextInt(lastCell - start) + start + 1;

            if (isValidLadder(start, end)) {
                ladders.put(start, new Ladder(start, end));
            }
        }
    }

    public int getFinalPosition(int pos) {
        while (true) {
            if (snakes.containsKey(pos)) {
                pos = snakes.get(pos).getTail();
            } else if (ladders.containsKey(pos)) {
                pos = ladders.get(pos).getEnd();
            } else break;
        }
        return pos;
    }

    private boolean isValidSnake(int head, int tail) {
        if (head <= tail) return false;
        if (head <= 1 || head >= lastCell) return false;
        if (tail <= 0 || tail >= lastCell) return false;
        if (snakes.containsKey(head) || ladders.containsKey(head)) return false;
        return !createsCycle(head, tail);
    }

    private boolean isValidLadder(int start, int end) {
        if (start >= end) return false;
        if (start <= 1 || start >= lastCell) return false;
        if (end <= 1 || end > lastCell) return false;
        if (ladders.containsKey(start) || snakes.containsKey(start)) return false;
        return !createsCycle(start, end);
    }

    private boolean createsCycle(int start, int end) {
        int current = end;
        while (true) {
            if (current == start) return true;
            if (snakes.containsKey(current)) {
                current = snakes.get(current).getTail();
            } else if (ladders.containsKey(current)) {
                current = ladders.get(current).getEnd();
            } else break;
        }
        return false;
    }
}