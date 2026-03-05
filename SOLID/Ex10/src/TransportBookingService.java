public class TransportBookingService {
    private final IDistanceCalculator distCalc;
    private final IDriverAllocator driverAlloc;
    private final IPaymentGateway payGateway;

    public TransportBookingService(IDistanceCalculator distCalc, IDriverAllocator driverAlloc, IPaymentGateway payGateway) {
        this.distCalc = distCalc;
        this.driverAlloc = driverAlloc;
        this.payGateway = payGateway;
    }

    public void book(TripRequest req) {
        double km = distCalc.km(req.from, req.to);
        System.out.println("DistanceKm=" + km);

        String driver = driverAlloc.allocate(req.studentId);
        System.out.println("Driver=" + driver);

        double fare = 50.0 + km * 6.6666666667;
        fare = Math.round(fare * 100.0) / 100.0;

        String txn = payGateway.charge(req.studentId, fare);
        System.out.println("Payment=PAID txn=" + txn);

        BookingReceipt r = new BookingReceipt("R-501", fare);
        System.out.println("RECEIPT: " + r.id + " | fare=" + String.format("%.2f", r.fare));
    }
}
