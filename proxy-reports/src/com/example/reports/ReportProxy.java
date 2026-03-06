package com.example.reports;

/**
 * Proxy — intercepts display() to:
 * 1. Check access before doing anything.
 * 2. Lazy-load RealReport only on first authorized access.
 * 3. Cache the loaded RealReport for subsequent calls.
 */
public class ReportProxy implements Report {

    private final String reportId;
    private final String title;
    private final String classification;
    private final AccessControl accessControl = new AccessControl();

    private RealReport realReport; // null until first authorized access

    public ReportProxy(String reportId, String title, String classification) {
        this.reportId = reportId;
        this.title = title;
        this.classification = classification;
    }

    @Override
    public void display(User user) {
        if (!accessControl.canAccess(user, classification)) {
            System.out.println("[proxy] ACCESS DENIED: " + user.getName()
                    + " (" + user.getRole() + ") cannot access " + classification + " report " + reportId);
            return;
        }

        if (realReport == null) {
            System.out.println("[proxy] Loading report " + reportId + " for first time...");
            realReport = new RealReport(reportId, title, classification);
        } else {
            System.out.println("[proxy] Serving cached report " + reportId);
        }

        realReport.display(user);
    }
}
