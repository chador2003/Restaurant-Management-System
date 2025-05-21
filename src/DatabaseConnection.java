
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/restaurant"; // Replace with your database URL
    private static final String USER = "root"; // Replace with your database username
    private static final String PASSWORD = ""; // Replace with your database password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Ensure you have the correct MySQL JDBC driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("Unable to load JDBC driver", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
