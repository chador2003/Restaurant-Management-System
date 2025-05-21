import Restaurant.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuImp {
    private static MenuImp instance;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private MenuImp() {
    }

    public static synchronized MenuImp getInstance() {
        if (instance == null) {
            instance = new MenuImp();
        }
        return instance;
    }

    public boolean addItem(Menu item) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO menu (name, type, price) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getItemType());
            pstmt.setFloat(3, item.getPrice());
            int rowsInserted = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted);
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void removeItem(Menu item) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM menu WHERE item_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, item.getItemId());
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rowsDeleted);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(Menu item) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE menu SET name = ?, type = ?, price = ? WHERE item_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getItemType());
            pstmt.setFloat(3, item.getPrice());
            pstmt.setInt(4, item.getItemId());
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Menu> getAllItems() {
        List<Menu> menuItems = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM menu";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("name");
                String itemType = rs.getString("type");
                float price = rs.getFloat("price");
                Menu item = new MenuItem(itemId, itemName, itemType, price);
                menuItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    // New method to get a menu item by name
    public Menu getItemByName(String itemName) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM menu WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, itemName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int itemId = rs.getInt("item_id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                float price = rs.getFloat("price");
                return new MenuItem(itemId, name, type, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
