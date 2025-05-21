
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignInUI extends JFrame {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private JTextField usernameField;
    private JPasswordField passwordField;

    public SignInUI() {
        setTitle("Sign In");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Create main panel with padding and grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and add username label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        // Create and add password label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Create and add sign-in button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signInButton = new JButton("Sign In");
        signInButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size for button
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signIn();
            }
        });
        mainPanel.add(signInButton, gbc);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
    }

    private void signIn() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT user_type FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
           
                        String userType = rs.getString("user_type");
                        if ("customer".equalsIgnoreCase(userType)) {
                            JOptionPane.showMessageDialog(this, "Sign in successful! Redirecting to customer page...");
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    new CustomerPageUI(username).setVisible(true);
                                }
                            });
                        } else if ("admin".equalsIgnoreCase(userType)) {
                            JOptionPane.showMessageDialog(this, "Sign in successful! Redirecting to admin page...");
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    new AdminPageUI().setVisible(true);
                                }
                            });
                        }
                        dispose(); // Close the sign-in window
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid username or password.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateCredentials(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SignInUI().setVisible(true);
            }
        });
    }
}
