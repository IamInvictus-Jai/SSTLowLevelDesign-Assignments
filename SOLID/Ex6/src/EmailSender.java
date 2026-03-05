public class EmailSender extends NotificationSender<EmailMessage> {
    public EmailSender(AuditLog audit) { super(audit); }

    @Override
    public void send(EmailMessage m) {
        System.out.println("EMAIL -> to=" + m.to + " subject=" + m.subject + " body=" + m.body);
        audit.add("email sent");
    }
}
