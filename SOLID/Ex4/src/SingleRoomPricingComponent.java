public class SingleRoomPricingComponent implements PricingComponent {
    private Money basePrice = new Money(14000.0);

    public Money getPrice() { return this.basePrice; }
}