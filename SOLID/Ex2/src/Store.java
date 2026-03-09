public interface Store {
    void save(String name, String content);
    int countLines(String name);
}