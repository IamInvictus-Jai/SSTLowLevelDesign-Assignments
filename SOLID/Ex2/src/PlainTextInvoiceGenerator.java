import java.util.Map;
import java.util.List;

public class PlainTextInvoiceGenerator implements InvoiceGenerator {

    public String generate(
        List<OrderLine> lines,
        Map<String, MenuItem> menu,
        BillingContext context
    ) {
        StringBuilder out = new StringBuilder();
        out.append("Invoice# ").append(context.getInvoiceId()).append("\n");

        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            out.append(String.format("- %s x%d = %.2f\n", item.name, l.qty, lineTotal));
        }

        out.append(String.format("Subtotal: %.2f\n", context.getSubtotal()));
        out.append(String.format("Tax(%.0f%%): %.2f\n", context.getTax().taxPercent, context.getTax().taxAmt));
        out.append(String.format("Discount: -%.2f\n", context.getDiscount()));
        out.append(String.format("TOTAL: %.2f\n", context.getTotal()));

        return out.toString();
    }
}