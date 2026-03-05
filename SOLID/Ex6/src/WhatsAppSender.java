public class WhatsAppSender extends NotificationSender<WhatsAppMessage> {
    public WhatsAppSender(AuditLog audit) { super(audit); }

    @Override
    public void send(WhatsAppMessage m) {
        // Validation is the sender's responsibility; failure is handled internally,
        // never leaking a surprise exception to the caller.
        if (m.to == null || !m.to.startsWith("+")) {
            System.out.println("WA ERROR: phone must start with + and country code");
            audit.add("WA failed");
            return;
        }
        System.out.println("WA -> to=" + m.to + " body=" + m.body);
        audit.add("wa sent");
    }
}
