import java.util.Map;
import java.util.List;

public interface InvoiceGenerator {
    String generate(List<OrderLine> lines, Map<String, MenuItem> menu, BillingContext context);
}