import java.util.Map;
import java.util.List;

public class CourseValidator implements Validator {
    private String errorMsg;
    private List<String> supportedPrograms;

    public CourseValidator(List<String> programs) {
        this.errorMsg = "program is invalid";
        this.supportedPrograms = programs;
    }

    public boolean validate(Map<String,String> kv) {
        String program = kv.getOrDefault("program", "");
        if (!this.supportedPrograms.contains(program)) return false;
        return true;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}