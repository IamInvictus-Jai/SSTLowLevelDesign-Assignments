public class DefaultRoomPricingComponent implements PricingComponent {
    private Money basePrice = new Money(16000.0);

    public Money getPrice() { return this.basePrice; }
}