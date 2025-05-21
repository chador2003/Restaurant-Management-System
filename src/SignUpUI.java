import Restaurant.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpUI extends JFrame implements FormFieldObserver {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JTextField phoneNumberField;

    private FormFieldObservable formFieldObservable;

    public SignUpUI() {
        setTitle("Sign Up");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        // Create main panel with padding and grid bag layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize observable
        formFieldObservable = new FormFieldObservable();

        // Add Full Name label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(15);
        mainPanel.add(fullNameField, gbc);

        // Add Username label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        // Add Phone Number label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        phoneNumberField = new JTextField(15);
        mainPanel.add(phoneNumberField, gbc);

        // Add Email label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        mainPanel.add(emailField, gbc);

        // Add Password label and field
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Add Sign Up button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size for button
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });
        mainPanel.add(signUpButton, gbc);

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);

        // Register SignUpUI as an observer for each field
        formFieldObservable = new FormFieldObservable();
        formFieldObservable.addObserver(this);
        usernameField.addActionListener(e -> formFieldObservable.notifyObservers("username", usernameField.getText()));
        emailField.addActionListener(e -> formFieldObservable.notifyObservers("email", emailField.getText()));
        fullNameField.addActionListener(e -> formFieldObservable.notifyObservers("fullName", fullNameField.getText()));
        phoneNumberField.addActionListener(e -> formFieldObservable.notifyObservers("phoneNumber", phoneNumberField.getText()));
        passwordField.addActionListener(e -> formFieldObservable.notifyObservers("password", new String(passwordField.getPassword())));
    }

    @Override
    public void update(String fieldName, String value) {
        switch (fieldName) {
            case "phoneNumber":
                if (!isValidPhoneNumber(value)) {
                    JOptionPane.showMessageDialog(this, "Invalid phone number. Please enter exactly 8 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    phoneNumberField.setText("");
                }
                break;
            case "fullName":
                if (!isValidFullName(value)) {
                    JOptionPane.showMessageDialog(this, "Invalid full name. Please enter letters only.", "Error", JOptionPane.ERROR_MESSAGE);
                    fullNameField.setText("");
                }
                break;
            case "password":
                if (!isValidPassword(value)) {
                    JOptionPane.showMessageDialog(this, "Invalid password. Please enter 6 or more characters.", "Error", JOptionPane.ERROR_MESSAGE);
                    passwordField.setText("");
                }
                break;
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number consists of exactly 8 digits
        return phoneNumber.matches("\\d{8}");
    }

    private boolean isValidFullName(String fullName) {
        // Check if the full name contains only letters
        return fullName.matches("[a-zA-Z ]+");
    }

    private boolean isValidPassword(String password) {
        // Check if the password is at least 6 characters long
        return password.length() >= 6;
    }

    private boolean isValidEmail(String email) {
        // Simple email validation check
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidUsername(String username) {
        // Username validation check
        return username != null && !username.trim().isEmpty();
    }

    private void signUp() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();
        String fullName = fullNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String userType = "customer"; // Default user type

        // Perform validation checks
        if (!isValidFullName(fullName)) {
            JOptionPane.showMessageDialog(this, "Invalid full name. Please enter letters only.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidUsername(username)) {
            JOptionPane.showMessageDialog(this, "Invalid username. Please enter a valid username.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number. Please enter exactly 8 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email. Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "Invalid password. Please enter 6 or more characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Proceed with database insertion if all validations pass
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO users (full_name, username, phone_number, email, password, user_type) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fullName);
                stmt.setString(2, username);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, email);
                stmt.setString(5, password);
                stmt.setString(6, userType); // Set the default user type

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User signed up successfully!");
                    clearFields();

                    // Redirect to SignInUI after successful signup
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            new SignInUI().setVisible(true);
                        }
                    });
                    this.dispose(); // Close the signup window
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to sign up user.");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        fullNameField.setText("");
        usernameField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SignUpUI().setVisible(true);
            }
        });
    }
}
