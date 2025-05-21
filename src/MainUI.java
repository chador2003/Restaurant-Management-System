import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame {

    public MainUI() {
        setTitle("Welcome");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding

        // Create a panel for buttons with GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Rows, Columns, Horizontal Gap, Vertical Gap

        // Sign Up Button
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignUpUI().setVisible(true);
            }
        });
        buttonPanel.add(signUpButton);

        // Sign In Button
        JButton signInButton = new JButton("Sign In");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SignInUI().setVisible(true);
            }
        });
        buttonPanel.add(signInButton);

        // Add the button panel to the main panel
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        add(mainPanel);
    }

          
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }
}
