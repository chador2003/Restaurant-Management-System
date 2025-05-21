import Restaurant.TableState;
import Restaurant.Table;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database", e);
        }
    }

    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String sql = "SELECT table_no, state FROM `table`";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int tableNo = rs.getInt("table_no");
                String stateStr = rs.getString("state");
                TableState state;
                switch (stateStr) {
                    case "Vacant":
                        state = new VacantState();
                        break;
                    case "Reserved":
                        state = new ReservedState();
                        break;
                    case "Occupied":
                        state = new OccupiedState();
                        break;
                    default:
                        throw new IllegalStateException("Unknown state: " + stateStr);
                }
                Table table = new Table(tableNo, state);
                tables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public void updateTableState(int tableNo, String state) {
        String sql = "UPDATE `table` SET state = ? WHERE table_no = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, state);
            pstmt.setInt(2, tableNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Table> getVacantTables() {
        List<Table> vacantTables = new ArrayList<>();
        String sql = "SELECT table_no, state FROM `table` WHERE state = 'Vacant'";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int tableNo = rs.getInt("table_no");
                String stateStr = rs.getString("state");
                TableState state = new VacantState();
                Table table = new Table(tableNo, state);
                vacantTables.add(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vacantTables;
    }
}
