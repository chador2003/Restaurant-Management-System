
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountStrategyDAO {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public void setDiscountState(float discountPercentage) {
        String query = "UPDATE discountstate SET discountPercentage = ?, lastUpdated = CURRENT_TIMESTAMP WHERE id = 1";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setFloat(1, discountPercentage);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Discount updated successfully.");
            } else {
                System.out.println("No rows affected. Please check if the table and columns exist and the id=1 row is present.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public float getDiscountState() {
        String query = "SELECT discountPercentage FROM discountstate WHERE id = 1";
        float discountPercentage = 0.0f;

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                discountPercentage = rs.getFloat("discountPercentage");
                System.out.println("Current discount: " + discountPercentage);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return discountPercentage;
    }
}
