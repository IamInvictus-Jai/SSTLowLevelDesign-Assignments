public class AttendancePercentRule implements Rule {
    private final int threshold;
    private final String evaluationFailMsg;

    AttendancePercentRule(int threshold, String evaluationFailMsg) {
        this.threshold = threshold;
        this.evaluationFailMsg = evaluationFailMsg;
    }

    public boolean evaluate(StudentProfile profile) {
        return profile.attendancePct >= this.threshold;
    }

    public String getFailedEvaluationReason() {
        return this.evaluationFailMsg;
    }
}