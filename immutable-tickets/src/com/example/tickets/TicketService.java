package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer that creates tickets.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - creates partially valid objects
 * - mutates after creation (bad for auditability)
 * - validation is scattered & incomplete
 *
 * TODO (student):
 * - After introducing immutable IncidentTicket + Builder, refactor this to stop mutating.
 */
public class TicketService {

    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        // No validation needed here - Builder handles it all!
        return IncidentTicket.builder()
                .id(id)
                .reporterEmail(reporterEmail)
                .title(title)
                .priority("MEDIUM")
                .source("CLI")
                .customerVisible(false)
                .tags(List.of("NEW"))
                .build();  // Validation happens here
    }


    public IncidentTicket escalateToCritical(IncidentTicket t) {
        // Create a NEW ticket with updated fields
        List<String> newTags = new ArrayList<>(t.getTags());
        newTags.add("ESCALATED");
        
        return IncidentTicket.Builder.from(t)
                .priority("CRITICAL")
                .tags(newTags)
                .build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        // No validation needed - Builder handles it
        return IncidentTicket.Builder.from(t)
                .assigneeEmail(assigneeEmail)
                .build();
    }

}
