public class FakeEligibilityStore implements EligibilityStore {
    public void save(String roll, EligibilityStatus status) {
        System.out.println("Saved evaluation for roll=" + roll);
    }
}
