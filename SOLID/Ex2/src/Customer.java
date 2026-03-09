public class Customer {
    private DiscountCalculator discountCalculator;
    private TaxCalculator taxCalculator;

    Customer(DiscountCalculator discountCalculator, TaxCalculator taxCalculator) {
        this.discountCalculator = discountCalculator;
        this.taxCalculator = taxCalculator;
    }

    public double calculateDiscount(BillingContext context) {
        return discountCalculator.discountAmount(context);
    }

    public Tax calculateTax(double subtotal) {
        double taxPercent = this.taxCalculator.taxPercent();
        double taxAmt = this.taxCalculator.calculateTax(taxPercent, subtotal);

        return new Tax(taxPercent, taxAmt);
    }
}