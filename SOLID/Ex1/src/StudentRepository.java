import java.util.Map;

public class StudentRepository implements Repository {
    private final Db db;

    private IdGenerator idGenerator;

    public StudentRepository(Db db, IdGenerator idGenerator) {
        this.db = db;
        this.idGenerator = idGenerator;
    }

    public StudentRecord save(Map<String,String> kv) {
        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        String id = this.idGenerator.nextStudentId(db.count());
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);

        this.db.save(rec);
        return rec;
    }

    public int getTotalCountOfStudents() {
        return this.db.count();
    }
}