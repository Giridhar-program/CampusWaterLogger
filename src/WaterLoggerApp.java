import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class WaterLoggerApp extends JFrame {

    public static Map<String, Dispenser> database = new TreeMap<>();
    private AdminPanel adminPanel;

    public static void main(String[] args) {
        DatabaseHandler.loadFromDB(database);
        SwingUtilities.invokeLater(() -> new WaterLoggerApp().setVisible(true));
    }

    public WaterLoggerApp() {
        setTitle("Campus Water System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.SUBHEADER_FONT);
        
        tabbedPane.addTab("Staff Logs", new StaffPanel());
        
        adminPanel = new AdminPanel();
        tabbedPane.addTab("Admin Dashboard", adminPanel);
        
        tabbedPane.addTab("Manage Dispensers", createManagePanel());

        // Simple trigger to update text when the Admin tab is opened
        tabbedPane.addChangeListener(e -> adminPanel.refreshStats());

        add(tabbedPane);
    }

    // SIMPLIFIED ADD DISPENSER PANEL
    private JPanel createManagePanel() {
        // Simple Layout: 3 Rows, 2 Columns
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 20));
        Theme.stylePanel(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 350, 50)); // Keeps fields near the top
        
        JTextField txtId = new JTextField();
        JTextField txtLoc = new JTextField();
        JButton btnAdd = new JButton("Add Dispenser");
        Theme.styleButton(btnAdd);

        // Row 1
        panel.add(new JLabel("New Dispenser ID:"));
        panel.add(txtId);
        
        // Row 2
        panel.add(new JLabel("Location Name:"));
        panel.add(txtLoc);
        
        // Row 3
        panel.add(new JLabel("")); // Empty space to push button to the right side
        panel.add(btnAdd);

        // Logic
        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String loc = txtLoc.getText().trim();
            
            if(!id.isEmpty() && !loc.isEmpty()) {
                Dispenser newDispenser = new Dispenser(id, loc, 0, "08:00 AM");
                
                database.put(id, newDispenser);
                DatabaseHandler.insertDispenser(newDispenser);
                StaffPanel.refreshList();
                
                txtId.setText("");
                txtLoc.setText("");
                JOptionPane.showMessageDialog(this, "Added!");
            }
        });

        return panel;
    }
}