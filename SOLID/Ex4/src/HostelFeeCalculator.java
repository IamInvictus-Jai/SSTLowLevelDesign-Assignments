import java.util.*;

public class HostelFeeCalculator {
    private List<PricingComponent> components;

    public HostelFeeCalculator(List<PricingComponent> components) {
        this.components = components;
    }

    public Money calculateMonthly() {
        Money monthlyFee = new Money(0.0);
        for (PricingComponent component:this.components) {
            Money componentPrice = component.getPrice();
            monthlyFee = monthlyFee.plus(componentPrice);
        }
        return monthlyFee;
    }
}
