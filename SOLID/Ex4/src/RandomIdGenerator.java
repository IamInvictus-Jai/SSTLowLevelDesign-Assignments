import java.util.Random;

public class RandomIdGenerator implements BookingIdGenerator {
    private Random random = new Random(1);
    public String generate() {
        return "H-" + (7000 + this.random.nextInt(1000));
    }
}