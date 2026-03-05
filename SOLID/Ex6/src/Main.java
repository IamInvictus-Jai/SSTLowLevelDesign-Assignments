public class Main {
    public static void main(String[] args) {
        System.out.println("=== Notification Demo ===");
        AuditLog audit = new AuditLog();

        EmailSender email = new EmailSender(audit);
        SmsSender sms = new SmsSender(audit);
        WhatsAppSender wa = new WhatsAppSender(audit);

        email.send(new EmailMessage("riya@sst.edu", "Welcome", "Hello and welcome to SST!"));
        sms.send(new SmsMessage("9876543210", "Hello and welcome to SST!"));
        wa.send(new WhatsAppMessage("9876543210", "Hello and welcome to SST!")); // no + → handled internally

        System.out.println("AUDIT entries=" + audit.size());
    }
}
