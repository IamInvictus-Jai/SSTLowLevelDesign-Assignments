public class BillingContext {
    private String invoiceId;
    private int totalDistinctLines;
    private double subtotal;
    private Tax tax;
    private double discount;
    private double total;

    public BillingContext(String invoiceId) {
        this.invoiceId = invoiceId;
        this.totalDistinctLines = 0;
        this.subtotal = 0;
        this.discount = 0.0;
        this.total = 0.0;
    }

    public String getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public double getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getTotalDistinctLines() {
        return this.totalDistinctLines;
    }

    public void setTotalDistinctLines(int totalDistinctLines) {
        this.totalDistinctLines = totalDistinctLines;
    }
    
    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public Tax getTax() {
        return this.tax;
    }

    public double getDiscount() {
        return this.discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotal() {
        return this.total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}