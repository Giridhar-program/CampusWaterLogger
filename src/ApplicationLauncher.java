import javax.swing.*;
import java.awt.*;

public class ApplicationLauncher {

    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(ApplicationLauncher::showLogin);
    }

    public static void showLogin() {
        JFrame frame = new JFrame("System Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(new GridLayout(3, 2, 5, 5));
        frame.setLocationRelativeTo(null); 

        JLabel userLabel = new JLabel(" Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel(" Password:");
        JPasswordField passField = new JPasswordField();
        
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(loginButton);
        frame.add(cancelButton);

        // Login Action
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            // Dummy Validation
            if ("admin".equals(username) && "password".equals(password)) {
                frame.dispose(); // Close login window
                
                // 1. Load data from the database into the memory map
                DatabaseHandler.loadFromDB(WaterLoggerApp.database);
                
                // 2. Open the main application window
                new WaterLoggerApp().setVisible(true); 
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}