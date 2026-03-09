public interface TaxCalculator {
    double taxPercent();

    default double calculateTax(double taxPercent, double subtotal) {
        return subtotal * (taxPercent/100);
    }
}