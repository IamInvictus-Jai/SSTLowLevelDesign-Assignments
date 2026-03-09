public class MessAddOnPricingComponent implements PricingComponent {
    private Money basePrice = new Money(1000.0);

    public Money getPrice() { return this.basePrice; }
}