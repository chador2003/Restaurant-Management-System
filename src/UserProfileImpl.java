import Restaurant.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileImpl implements UserProfile {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private String fullName;
    private String username;
    private String phoneNumber;
    private String email;

    public UserProfileImpl(String username) {
        this.username = username;
        fetchUserDetailsFromDatabase();
    }

    private void fetchUserDetailsFromDatabase() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT full_name, username, phone_number, email FROM users WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        fullName = rs.getString("full_name");
                        username = rs.getString("username");
                        phoneNumber = rs.getString("phone_number");
                        email = rs.getString("email");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
