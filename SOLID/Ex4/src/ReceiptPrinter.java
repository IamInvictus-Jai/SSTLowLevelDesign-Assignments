import java.util.*;

public interface ReceiptPrinter {
    void print(BookingRequest req, Money monthly, Money deposit);
}
