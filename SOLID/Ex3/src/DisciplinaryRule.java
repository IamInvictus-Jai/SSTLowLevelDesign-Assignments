public class DisciplinaryRule implements Rule {
    private final int flag = LegacyFlags.NONE;
    private final String evaluationFailMsg;

    DisciplinaryRule(String evaluationFailMsg) {
        this.evaluationFailMsg = evaluationFailMsg;
    }

    public boolean evaluate(StudentProfile profile) {
        return profile.disciplinaryFlag == this.flag;
    }

    public String getFailedEvaluationReason() {
        return this.evaluationFailMsg;
    }
}