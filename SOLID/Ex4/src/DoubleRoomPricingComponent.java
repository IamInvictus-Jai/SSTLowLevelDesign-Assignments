public class DoubleRoomPricingComponent implements PricingComponent {
    private Money basePrice = new Money(15000.0);

    public Money getPrice() { return this.basePrice; }
}