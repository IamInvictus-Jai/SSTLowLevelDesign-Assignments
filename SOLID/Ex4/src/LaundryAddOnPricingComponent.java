public class LaundryAddOnPricingComponent implements PricingComponent {
    private Money basePrice = new Money(500.0);

    public Money getPrice() { return this.basePrice; }
}