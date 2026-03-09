import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");

        List<Rule> rules = List.of(
            new DisciplinaryRule("disciplinary flag present"),
            new CGRRule(8.0, "CGR below 8.0"),
            new AttendancePercentRule(75, "attendance below 75"),
            new CreditsRule(20, "credits below 20")
        );

        EligibilityEngine engine = new EligibilityEngine(rules);
        ReportPrinter printer = new ConsoleReportPrinter();
        EligibilityStore store = new FakeEligibilityStore();

        StudentProfile s = new StudentProfile("23BCS1001", "Ayaan", 8.10, 72, 18, LegacyFlags.NONE);
        EligibilityEngineResult result = engine.evaluate(s);
        printer.print(s, result);
        store.save(s.rollNo, result.status);

        // Test 1: All rules pass - ELIGIBLE
        StudentProfile eligible = new StudentProfile(
            "23BCS1002", "Priya", 8.5, 80, 25, LegacyFlags.NONE
        );
        EligibilityEngineResult result2 = engine.evaluate(eligible);
        printer.print(eligible, result2);
        store.save(eligible.rollNo, result2.status);

        // Test 2: CGR fails
        StudentProfile lowCgr = new StudentProfile(
            "23BCS1003", "Rahul", 7.5, 80, 25, LegacyFlags.NONE
        );
        EligibilityEngineResult result3 = engine.evaluate(lowCgr);
        printer.print(lowCgr, result3);
        store.save(lowCgr.rollNo, result3.status);

        // Test 3: Disciplinary flag fails (first rule)
        StudentProfile flagged = new StudentProfile(
            "23BCS1004", "Neha", 8.5, 80, 25, LegacyFlags.WARNING
        );
        EligibilityEngineResult result4 = engine.evaluate(flagged);
        printer.print(flagged, result4);
        store.save(flagged.rollNo, result4.status);

        // Test 4: Credits fail
        StudentProfile lowCredits = new StudentProfile(
            "23BCS1005", "Arjun", 8.5, 80, 15, LegacyFlags.NONE
        );
        EligibilityEngineResult result5 = engine.evaluate(lowCredits);
        printer.print(lowCredits, result5);
        store.save(lowCredits.rollNo, result5.status);

    }
}
