public class SequentialIdGenerator implements InvIdGenerator {
    private int invoiceSeq = 1000;

    public String generate() {
        return "INV-" + (++this.invoiceSeq);
    }
}