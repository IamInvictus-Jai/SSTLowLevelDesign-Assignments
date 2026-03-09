import java.util.List;

interface Db {
    void save(StudentRecord r);

    int count();

    List<StudentRecord> all();
}