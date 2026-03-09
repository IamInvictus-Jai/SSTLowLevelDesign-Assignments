import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

/**
 * Starter demo that shows why mutability is risky.
 *
 * After refactor:
 * - direct mutation should not compile (no setters)
 * - external modifications to tags should not affect the ticket
 * - service "updates" should return a NEW ticket instance
 */
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        // Create a ticket using the service
        IncidentTicket original = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Original ticket created:");
        System.out.println(original);
        System.out.println("Priority: " + original.getPriority());
        System.out.println("Tags: " + original.getTags());

        // "Update" by assigning - returns a NEW ticket
        IncidentTicket assigned = service.assign(original, "agent@example.com");
        System.out.println("\n--- After assign (NEW ticket) ---");
        System.out.println("Original assignee: " + original.getAssigneeEmail());  // null
        System.out.println("New ticket assignee: " + assigned.getAssigneeEmail());  // agent@example.com

        // "Update" by escalating - returns a NEW ticket
        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\n--- After escalate (NEW ticket) ---");
        System.out.println("Original priority: " + original.getPriority());  // MEDIUM
        System.out.println("Assigned priority: " + assigned.getPriority());  // MEDIUM
        System.out.println("Escalated priority: " + escalated.getPriority());  // CRITICAL
        System.out.println("Escalated tags: " + escalated.getTags());  // [NEW, ESCALATED]

        // Try to modify tags from outside - should have no effect!
        System.out.println("\n--- Attempting external tag modification ---");
        List<String> tags = escalated.getTags();
        try {
            tags.add("HACKED");
            System.out.println("ERROR: Should have thrown exception!");
        } catch (UnsupportedOperationException e) {
            System.out.println("✓ Tags are protected! Cannot modify: " + e.getMessage());
        }
        System.out.println("Escalated tags unchanged: " + escalated.getTags());

        // Demonstrate builder usage directly
        System.out.println("\n--- Direct builder usage ---");
        IncidentTicket custom = IncidentTicket.builder()
                .id("TCK-2000")
                .reporterEmail("user@example.com")
                .title("Custom ticket")
                .priority("HIGH")
                .slaMinutes(120)
                .tags(List.of("URGENT", "CUSTOMER"))
                .build();
        System.out.println(custom);

        System.out.println("\n✓ All tickets are immutable!");
        System.out.println("✓ Updates create new instances!");
        System.out.println("✓ Internal state is protected!");
    }

}
