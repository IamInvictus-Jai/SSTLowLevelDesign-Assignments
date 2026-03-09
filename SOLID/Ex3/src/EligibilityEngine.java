import java.util.*;

public class EligibilityEngine {
    private List<Rule> rules;

    public EligibilityEngine(List<Rule> rules) {
        this.rules = rules;
    }

    public EligibilityEngineResult evaluate(StudentProfile s) {
        List<String> reasons = new ArrayList<>();
        EligibilityStatus status = EligibilityStatus.ELIGIBLE;

        for (Rule rule:this.rules) {
            if (!rule.evaluate(s)) {
                status = EligibilityStatus.NOT_ELIGIBLE;
                reasons.add(rule.getFailedEvaluationReason());
                break;
            }
        }

        return new EligibilityEngineResult(status, reasons);
    }
}
