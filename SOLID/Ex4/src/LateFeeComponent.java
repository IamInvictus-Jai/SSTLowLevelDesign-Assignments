public class LateFeeComponent implements PricingComponent {
    private Money fee = new Money(500.0);
    
    public Money getPrice() {
        return this.fee; // Late fee amount
    }
}
