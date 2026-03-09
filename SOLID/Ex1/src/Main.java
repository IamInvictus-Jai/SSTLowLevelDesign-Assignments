import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        Db db = new FakeDb();
        ISerializer serializer = new KeyValSerializer();
        List<String> allowedPrograms = List.of("CSE", "AI", "SWE", "DATA");
        List<Validator> validators = List.of(
            new NameValidator(),
            new EmailValidator(),
            new PhoneNumberValidator(),
            new CourseValidator(allowedPrograms)
        );
        Logger logger = new ConsoleLogger();
        IdGenerator idGenerator = new IdUtil();
        Repository studentRepository = new StudentRepository(db, idGenerator);
        OnboardingService svc = new OnboardingService(studentRepository, serializer, validators, logger);

        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);

        String badInput = "name=;email=riya#sst.edu;phone=abc;program=INVALID";
        svc.registerFromRawInput(badInput);


        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(db));
    }
}
