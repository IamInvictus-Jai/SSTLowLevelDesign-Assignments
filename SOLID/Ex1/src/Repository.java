import java.util.Map;

interface Repository {
    StudentRecord save(Map<String,String> kv);

    int getTotalCountOfStudents();
}