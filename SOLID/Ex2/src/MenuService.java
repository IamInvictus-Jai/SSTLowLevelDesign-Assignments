import java.util.Map;

public interface MenuService {
    void addToMenu(MenuItem item);

    Map<String, MenuItem> getMenu();
}