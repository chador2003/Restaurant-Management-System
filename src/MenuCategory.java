import Restaurant.*;
import java.util.ArrayList;
import java.util.List;

public class MenuCategory implements MenuComponent {
    private String name;
    List<MenuComponent> menuComponents = new ArrayList<>();

    public MenuCategory(String name) {
        this.name = name;
    }

    public void add(MenuComponent menuComponent) {
        menuComponents.add(menuComponent);
    }

    public void remove(MenuComponent menuComponent) {
        menuComponents.remove(menuComponent);
    }

    public MenuComponent getChild(int i) {
        return menuComponents.get(i);
    }

    @Override
    public void print() {
        System.out.println("Category: " + name);
        for (MenuComponent menuComponent : menuComponents) {
            menuComponent.print();
        }
    }

    public String getName() {
        return name;
    }
}
