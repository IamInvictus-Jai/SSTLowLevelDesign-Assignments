import java.util.Map;

public class PhoneNumberValidator implements Validator {
    private String errorMsg;

    public PhoneNumberValidator() {
        this.errorMsg = "phone is invalid";
    }

    public boolean validate(Map<String,String> kv) {
        String phone = kv.getOrDefault("phone", "");
        if (phone.isBlank() || !phone.chars().allMatch(Character::isDigit)) return false;
        return true;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}