public interface GameObserver {
    void onDiceRoll(Player player, int dice);
    void onMove(Player player, int from, int to);
    void onWin(Player player);
}