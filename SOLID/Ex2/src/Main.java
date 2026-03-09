import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Cafeteria Billing ===");

        MenuService menuService = new InMemoryMenuService();
        menuService.addToMenu(new MenuItem("M1", "Veg Thali", 80.00));
        menuService.addToMenu(new MenuItem("C1", "Coffee", 30.00));
        menuService.addToMenu(new MenuItem("S1", "Sandwich", 60.00));

        InvoiceGenerator invoice = new PlainTextInvoiceGenerator();
        Store store = new FileStore();
        Logger logger = new ConsoleLogger();
        CafeteriaSystem sys = new CafeteriaSystem(store, menuService, invoice, logger);

        InvIdGenerator idGenerator = new SequentialIdGenerator();

        // Customer 1
        Customer studentCustomer = new Customer(
            new StudentDiscountCalculator(),
            new StudentTaxCalculator()
        );
        List<OrderLine> order1 = List.of(
                new OrderLine("M1", 2),
                new OrderLine("C1", 1)
        );

        sys.checkout(order1, idGenerator, studentCustomer);

        // Customer 2
        Customer staffCustomer = new Customer(
            new StaffDiscountCalculator(),
            new StaffTaxCalculator()
        );
        List<OrderLine> order2 = List.of(
                new OrderLine("M1", 2),
                new OrderLine("S1", 2)
        );

        sys.checkout(order2, idGenerator, staffCustomer);
    }
}