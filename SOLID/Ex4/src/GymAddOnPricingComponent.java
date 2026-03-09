public class GymAddOnPricingComponent implements PricingComponent {
    private Money basePrice = new Money(300.0);

    public Money getPrice() { return this.basePrice; }
}