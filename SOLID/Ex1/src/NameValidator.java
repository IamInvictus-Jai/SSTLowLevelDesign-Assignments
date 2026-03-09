import java.util.Map;

public class NameValidator implements Validator {
    private String errorMsg;

    public NameValidator() {
        this.errorMsg = "name is required";
    }
    
    public boolean validate(Map<String,String> kv) {
        String name = kv.getOrDefault("name", "");
        if (name.isBlank()) return false;
        return true;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}