public class StudentDiscountCalculator implements DiscountCalculator {
    public double discountAmount(BillingContext context) {
        double subtotal = context.getSubtotal();
        if (subtotal >= 180.0) return 10.0;
        return 0.0;
    }
}