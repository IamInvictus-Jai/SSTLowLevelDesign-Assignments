public class Player {
    private final String id;
    private int position;

    public Player(String id, int position) {
        this.id = id;
        this.position = position;
    }

    public String getId() { return id; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}