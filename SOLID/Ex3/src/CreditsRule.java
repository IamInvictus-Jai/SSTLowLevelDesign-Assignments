public class CreditsRule implements Rule {
    private final int threshold;
    private final String evaluationFailMsg;

    CreditsRule(int threshold, String evaluationFailMsg) {
        this.threshold = threshold;
        this.evaluationFailMsg = evaluationFailMsg;
    }

    public boolean evaluate(StudentProfile profile) {
        return profile.earnedCredits >= this.threshold;
    }

    public String getFailedEvaluationReason() {
        return this.evaluationFailMsg;
    }
}