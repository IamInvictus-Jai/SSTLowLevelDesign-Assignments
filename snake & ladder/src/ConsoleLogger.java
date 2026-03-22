public class ConsoleLogger implements GameObserver {
    
    public void onDiceRoll(Player player, int dice) {
        System.out.println(player.getId() + " rolled " + dice);
    }

    public void onMove(Player player, int from, int to) {
        System.out.println(player.getId() + " moved from " + from + " to " + to);
    }

    public void onWin(Player player) {
        System.out.println(player.getId() + " WON!");
    }
}