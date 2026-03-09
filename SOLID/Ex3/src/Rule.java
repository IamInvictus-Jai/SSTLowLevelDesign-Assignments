public interface Rule {
    boolean evaluate(StudentProfile profile);
    String getFailedEvaluationReason();
}