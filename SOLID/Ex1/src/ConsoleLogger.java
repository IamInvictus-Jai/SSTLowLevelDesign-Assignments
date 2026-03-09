import java.util.List;

public class ConsoleLogger implements Logger {
    public void info(String msg) {
        System.out.println(msg);
    }

    public void err(List<String> errMsg) {
        for (String err:errMsg) {
            System.out.println("- " + err);
        }
    }

    public void studentSavedSuccessMsg(StudentRecord rec, int totalCountOfStudents) {
        System.out.println("OK: created student " + rec.id);
        System.out.println("Saved. Total students: " + totalCountOfStudents);
        System.out.println("CONFIRMATION:");
        System.out.println(rec);
    }
}