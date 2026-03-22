import java.util.Scanner;

public class ConsoleInput implements InputProvider {

    private final Scanner scanner = new Scanner(System.in);

    public void waitForNextTurn() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}