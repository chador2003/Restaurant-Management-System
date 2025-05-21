import Restaurant.*;
public class MenuFactory {
    public static Menu createMenuItem(int itemId, String itemName, String itemType, float price) {
        return new MenuItem(itemId, itemName, itemType, price);
    }
    
    public static Menu createMenuItem(String itemName, String itemType, float price) {
        return new MenuItem(0, itemName, itemType, price);
    }
}
