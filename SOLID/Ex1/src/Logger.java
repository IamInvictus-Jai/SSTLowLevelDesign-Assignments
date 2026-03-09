import java.util.List;

interface Logger {
    void info(String msg);
    void err(List<String> errMsg);
    void studentSavedSuccessMsg(StudentRecord rec, int totalCountOfStudents);
}