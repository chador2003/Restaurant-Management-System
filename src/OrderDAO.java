

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public boolean placeOrder(Order order) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO `order` (customer_name, table_no, status, menu_items, quantity) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, order.getCustomerName());
            pstmt.setInt(2, order.getTableNo());
            pstmt.setString(3, order.getStatus());
            pstmt.setString(4, order.getMenuItems());
            pstmt.setInt(5, order.getQuantity());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM `order`";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                int tableNo = rs.getInt("table_no");
                String status = rs.getString("status");
                String menuItems = rs.getString("menu_items");
                int quantity = rs.getInt("quantity");
                Order order = new Order(id, customerName, tableNo, status, menuItems, quantity);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public Order getOrderById(int orderId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM `order` WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                int tableNo = rs.getInt("table_no");
                String status = rs.getString("status");
                String menuItems = rs.getString("menu_items");
                int quantity = rs.getInt("quantity");
                return new Order(id, customerName, tableNo, status, menuItems, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean deleteOrder(int orderId) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM `order` WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // public boolean updateOrderStatus(int orderId, String newStatus) {
    //     try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
    //         String sql = "UPDATE `order` SET status = ? WHERE id = ?";
    //         PreparedStatement pstmt = conn.prepareStatement(sql);
    //         pstmt.setString(1, newStatus);
    //         pstmt.setInt(2, orderId);
    //         int rowsUpdated = pstmt.executeUpdate();
    //         return rowsUpdated > 0;
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         return false;
    //     }
    // }
    public boolean updateOrderStatus(int orderId, String newStatus) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "UPDATE `order` SET status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
      // Define the method to get orders by username
    //   public List<Order> getOrdersByUsername(String username) {
    //     List<Order> orders = new ArrayList<>();
    //     try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
    //         String sql = "SELECT * FROM order WHERE username = ?";
    //         try (PreparedStatement stmt = conn.prepareStatement(sql)) {
    //             stmt.setString(1, username);
    //             try (ResultSet rs = stmt.executeQuery()) {
    //                 while (rs.next()) {
    //                     Order order = new Order();
    //                     order.setId(rs.getInt("id"));
    //                     order.setCustomerName(rs.getString("customer_name"));
    //                     order.setTableNo(rs.getInt("table_no"));
    //                     order.setStatus(rs.getString("status"));
    //                     order.setMenuItems(rs.getString("menu_items"));
    //                     order.setQuantity(rs.getInt("quantity"));
    //                     // Add the order to the list
    //                     orders.add(order);
    //                 }
    //             }
    //         }
    //     } catch (SQLException ex) {
    //         ex.printStackTrace();
    //     }
    //     return orders;
    // Method to get orders by username
    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = new ArrayList<>();
        if (username == null) {
            System.out.println("Username is null. Cannot fetch orders.");
            return orders;
        }
    
        String sql = "SELECT * FROM `order` WHERE customer_name = ?";
    
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String customerName = rs.getString("customer_name");
                    int tableNo = rs.getInt("table_no");
                    String status = rs.getString("status");
                    String menuItems = rs.getString("menu_items");
                    int quantity = rs.getInt("quantity");
    
                    Order order = new Order(id, customerName, tableNo, status, menuItems, quantity);
                    orders.add(order);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return orders;
    }
    
    }
