import java.util.Map;

public class EmailValidator implements Validator {
    private String errorMsg;

    public EmailValidator() {
        this.errorMsg = "email is invalid";
    }
    
    public boolean validate(Map<String,String> kv) {
        String email = kv.getOrDefault("email", "");
        if (email.isBlank() || !email.contains("@")) return false;
        return true;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}