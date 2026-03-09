import java.util.*;

public class OnboardingService {
    private final ISerializer serializer;

    private List<Validator> validators;

    private Logger logger;

    private Repository repo;

    public OnboardingService(
        Repository studentRepository,
        ISerializer serializer,
        List<Validator> validators,
        Logger logger
    ) {
        this.repo = studentRepository;
        this.serializer = serializer;
        this.validators = validators;
        this.logger = logger;
    }

    private List<String> runValidation(Map<String,String> kv) {
        if (this.validators == null || this.validators.size() == 0) return null;
        List<String> errMsg = new ArrayList<>();

        for (Validator v:this.validators) {
            if (!v.validate(kv)) errMsg.add(v.getErrorMsg());
        }

        return errMsg;
    }

    private void printSuccessMsgs(StudentRecord rec, int totalCountOfStudents) {
        this.logger.info("OK: created student " + rec.id);
        this.logger.info("Saved. Total students: " + totalCountOfStudents);
        this.logger.info("CONFIRMATION:");
        this.logger.info(rec.toString());
    }

    // Intentionally violates SRP: parses + validates + creates ID + saves + prints.
    public void registerFromRawInput(String raw) {
        this.logger.info("INPUT: " + raw);

        // Serialize raw input
        Map<String,String> kv = this.serializer.parse(raw);

        // Run validations
        List<String> errMsg = this.runValidation(kv);

        // Print Error Msgs
        if (errMsg.size() != 0) {
            this.logger.err(errMsg);
            return;
        }

        // Save in db
        StudentRecord rec = this.repo.save(kv);

        // Print success msgs
        printSuccessMsgs(rec, this.repo.getTotalCountOfStudents());
    }
}
