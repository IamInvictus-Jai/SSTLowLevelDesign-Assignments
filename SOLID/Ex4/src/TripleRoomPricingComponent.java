public class TripleRoomPricingComponent implements PricingComponent {
    private Money basePrice = new Money(12000.0);

    public Money getPrice() { return this.basePrice; }
}