public class StaffDiscountCalculator implements DiscountCalculator {
    public double discountAmount(BillingContext context) {
        int totalDistinctLines = context.getTotalDistinctLines();
        if (totalDistinctLines >= 3) return 15.0;
        return 5.0;
    }
}