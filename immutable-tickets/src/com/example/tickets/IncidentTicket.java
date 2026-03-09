package com.example.tickets;

import java.util.ArrayList;
import java.util.List;

/**
 * INTENTION: A ticket should be an immutable record-like object.
 *
 * CURRENT STATE (BROKEN ON PURPOSE):
 * - mutable fields
 * - multiple constructors
 * - public setters
 * - tags list can be modified from outside
 * - validation is scattered elsewhere
 *
 * TODO (student): refactor to immutable + Builder.
 */
public class IncidentTicket {

    private final String id;
    private final String reporterEmail;
    private final String title;
    private final String description;
    private final String priority;       // LOW, MEDIUM, HIGH, CRITICAL
    private final List<String> tags;     // mutable leak
    private final String assigneeEmail;
    private final boolean customerVisible;
    private final Integer slaMinutes;    // optional
    private final String source;         // e.g. "CLI", "WEBHOOK", "EMAIL"

    private IncidentTicket(Builder builder) {
        this.id = builder.id;
        this.reporterEmail = builder.reporterEmail;
        this.title = builder.title;
        this.description = builder.description;
        this.priority = builder.priority;
        this.tags = builder.tags == null ? null : new ArrayList<>(builder.tags);
        this.assigneeEmail = builder.assigneeEmail;
        this.customerVisible = builder.customerVisible;
        this.slaMinutes = builder.slaMinutes;
        this.source = builder.source;
    }

    // Builder inner class
    public static class Builder {
        // Mutable fields - temporary during building
        private String id;
        private String reporterEmail;
        private String title;
        private String description;
        private String priority;
        private List<String> tags;
        private String assigneeEmail;
        private boolean customerVisible;
        private Integer slaMinutes;
        private String source;

        // Private constructor - use IncidentTicket.builder() instead
        private Builder() {
        }

        // Fluent setter methods - return 'this' for chaining
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder reporterEmail(String reporterEmail) {
            this.reporterEmail = reporterEmail;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = tags == null ? null : new ArrayList<>(tags);
            return this;
        }

        public Builder assigneeEmail(String assigneeEmail) {
            this.assigneeEmail = assigneeEmail;
            return this;
        }

        public Builder customerVisible(boolean customerVisible) {
            this.customerVisible = customerVisible;
            return this;
        }

        public Builder slaMinutes(Integer slaMinutes) {
            this.slaMinutes = slaMinutes;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public IncidentTicket build() {
            // Validate required fields
            Validation.requireTicketId(id);
            Validation.requireEmail(reporterEmail, "reporterEmail");
            Validation.requireNonBlank(title, "title");
            Validation.requireMaxLen(title, 80, "title");

            // Validate optional fields (if provided)
            if (assigneeEmail != null && !assigneeEmail.trim().isEmpty()) {
                Validation.requireEmail(assigneeEmail, "assigneeEmail");
            }
            
            Validation.requireOneOf(priority, "priority", "LOW", "MEDIUM", "HIGH", "CRITICAL");
            Validation.requireRange(slaMinutes, 5, 7200, "slaMinutes");

            // All validation passed - create the immutable ticket
            return new IncidentTicket(this);
        }

        // Create a builder from an existing ticket (for "updates")
        public static Builder from(IncidentTicket ticket) {
            Builder builder = new Builder();
            builder.id = ticket.id;
            builder.reporterEmail = ticket.reporterEmail;
            builder.title = ticket.title;
            builder.description = ticket.description;
            builder.priority = ticket.priority;
            builder.tags = ticket.tags == null ? null : new ArrayList<>(ticket.tags);
            builder.assigneeEmail = ticket.assigneeEmail;
            builder.customerVisible = ticket.customerVisible;
            builder.slaMinutes = ticket.slaMinutes;
            builder.source = ticket.source;
            return builder;
        }

    }

    // Static factory method to get a new Builder
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getId() { return id; }
    public String getReporterEmail() { return reporterEmail; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public List<String> getTags() { 
        return tags == null ? List.of() : List.copyOf(tags);
    }
    public String getAssigneeEmail() { return assigneeEmail; }
    public boolean isCustomerVisible() { return customerVisible; }
    public Integer getSlaMinutes() { return slaMinutes; }
    public String getSource() { return source; }

    @Override
    public String toString() {
        return "IncidentTicket{" +
                "id='" + id + '\'' +
                ", reporterEmail='" + reporterEmail + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", tags=" + tags +
                ", assigneeEmail='" + assigneeEmail + '\'' +
                ", customerVisible=" + customerVisible +
                ", slaMinutes=" + slaMinutes +
                ", source='" + source + '\'' +
                '}';
    }
}
