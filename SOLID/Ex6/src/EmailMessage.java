public class EmailMessage {
    public final String to;
    public final String subject;
    public final String body;

    public EmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }
}
