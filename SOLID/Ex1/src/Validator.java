import java.util.Map;

interface Validator {
    boolean validate(Map<String,String> kv);
    String getErrorMsg();
}