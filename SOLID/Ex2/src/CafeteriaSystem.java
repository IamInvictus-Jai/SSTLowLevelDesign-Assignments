import java.util.*;

public class CafeteriaSystem {
    private final MenuService menuService;
    private InvoiceGenerator invoice;
    private final Store store;
    private final Logger logger;
    

    public CafeteriaSystem(Store store, MenuService menuService, InvoiceGenerator invoice, Logger logger) {
        this.store = store;
        this.menuService = menuService;
        this.invoice = invoice;
        this.logger = logger;
    }

    // Intentionally SRP-violating: menu mgmt + tax + discount + format + persistence.
    public void checkout(
        List<OrderLine> lines,
        InvIdGenerator idGenerator,
        Customer customer
    ) {
        BillingContext context = new BillingContext(idGenerator.generate());
        context.setTotalDistinctLines(lines.size());
        
        double subtotal = CalculateSubtotalUtil.calculateSubtotal(lines, this.menuService.getMenu());
        context.setSubtotal(subtotal);

        Tax tax = customer.calculateTax(subtotal);
        double discount = customer.calculateDiscount(context);
        double total = subtotal + tax.taxAmt - discount;

        context.setTax(tax);
        context.setDiscount(discount);
        context.setTotal(total);

        String invoice = this.invoice.generate(lines, this.menuService.getMenu(), context);
        this.logger.infoSameLine(invoice);

        store.save(context.getInvoiceId(), invoice);
        this.logger.info("Saved invoice: " + context.getInvoiceId() + " (lines=" + store.countLines(context.getInvoiceId()) + ")");
    }
}
