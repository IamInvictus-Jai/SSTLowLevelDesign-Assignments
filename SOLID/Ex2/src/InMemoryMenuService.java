import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryMenuService implements MenuService {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();

    public void addToMenu(MenuItem item) {
        this.menu.put(item.id, item);
    };

    public Map<String, MenuItem> getMenu() {
        return this.menu;
    }
}