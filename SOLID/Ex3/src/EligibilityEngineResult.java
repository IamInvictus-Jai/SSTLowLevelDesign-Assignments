import java.util.List;

public class EligibilityEngineResult {
    public final EligibilityStatus status;
    public final List<String> reasons;
    public EligibilityEngineResult(EligibilityStatus status, List<String> reasons) {
        this.status = status;
        this.reasons = reasons;
    }
}