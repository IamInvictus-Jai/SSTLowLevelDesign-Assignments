import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Hostel Fee Calculator ===");
        BookingRequest req = new BookingRequest(LegacyRoomTypes.DOUBLE, List.of(AddOn.LAUNDRY, AddOn.MESS));
        PricingService pricing = new PricingService();

        // Get Pricing components
        List<PricingComponent> components = pricing.getComponents(req);
        // Add late fee without modifying calculator or pricing service!
        components.add(new LateFeeComponent());
        
        HostelFeeCalculator calc = new HostelFeeCalculator(components);
        BookingIdGenerator idGenerator = new RandomIdGenerator();

        // Calculate Fee
        Money monthlyFee = calc.calculateMonthly();
        Money deposit = new Money(5000.00);

        // Print the receipt
        ReceiptPrinter printer = new ConsoleReceiptPrinter();
        printer.print(req, monthlyFee, deposit);

        String bookingId = idGenerator.generate();

        BookingRepo repo = new FakeBookingRepo();
        repo.save(bookingId, req, monthlyFee, deposit);
    }
}
