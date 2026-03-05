/**
 * Base contract for all notification senders.
 * Subtypes MUST uphold: send() always completes without throwing.
 * Validation and failure handling are the sender's responsibility.
 */
public abstract class NotificationSender<T> {
    protected final AuditLog audit;
    protected NotificationSender(AuditLog audit) { this.audit = audit; }
    public abstract void send(T message);
}
