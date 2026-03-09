public class CGRRule implements Rule {
    private final double threshold;
    private final String evaluationFailMsg;

    CGRRule(double threshold, String evaluationFailMsg) {
        this.threshold = threshold;
        this.evaluationFailMsg = evaluationFailMsg;
    }

    public boolean evaluate(StudentProfile profile) {
        return profile.cgr >= this.threshold;
    }

    public String getFailedEvaluationReason() {
        return this.evaluationFailMsg;
    }
}