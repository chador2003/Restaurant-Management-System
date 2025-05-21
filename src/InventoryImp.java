import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryImp {
    private static volatile InventoryImp instance;
    private static Connection connection;

    private InventoryImp() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurant", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static InventoryImp getInstance() {
        if (instance == null) {
            synchronized (InventoryImp.class) {
                if (instance == null) {
                    instance = new InventoryImp();
                }
            }
        }
        return instance;
    }

    public List<Inventory> getAllItems() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM inventory";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("item_id");
                String itemName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                float price = rs.getFloat("price");
                Date expireDate = rs.getDate("expire_date");
                Inventory item = new Inventory(id, itemName, quantity, price, expireDate);
                inventoryList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inventoryList;
    }

    public void addItem(Inventory item) {
        String sql = "INSERT INTO inventory (name, quantity, price, expire_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getItemName());
            stmt.setInt(2, item.getQuantity());
            stmt.setFloat(3, item.getPrice());
            stmt.setDate(4, new java.sql.Date(item.getExpireDate().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeItem(int itemId) {
        String sql = "DELETE FROM inventory WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, itemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(Inventory item) {
        String sql = "UPDATE inventory SET name = ?, quantity = ?, price = ?, expire_date = ? WHERE item_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, item.getItemName());
            stmt.setInt(2, item.getQuantity());
            stmt.setFloat(3, item.getPrice());
            stmt.setDate(4, new java.sql.Date(item.getExpireDate().getTime()));
            stmt.setInt(5, item.getItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
