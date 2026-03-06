package com.example.reports;

/**
 * Depends only on the Report interface — works with proxy or real subject transparently.
 */
public class ReportViewer {

    public void open(Report report, User user) {
        report.display(user);
    }
}
