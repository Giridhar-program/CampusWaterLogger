import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public class WaterLoggerApp extends JFrame {

    // Using TreeMap to keep IDs sorted (D-101, D-102...)
    public static Map<String, Dispenser> database = new TreeMap<>();
    private AdminPanel adminPanel;

    public static void main(String[] args) {
        // 1. Load Data from MySQL Database
        DatabaseHandler.loadFromDB(database);

        // 2. Set Modern "Nimbus" Look and Feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    UIManager.getLookAndFeelDefaults().put("defaultFont", Theme.NORMAL_FONT);
                    break;
                }
            }
        } catch (Exception e) {}

        // 3. Launch App
        SwingUtilities.invokeLater(() -> new WaterLoggerApp().setVisible(true));
    }

    public WaterLoggerApp() {
        setTitle("Campus Water System (Unified View)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.SUBHEADER_FONT);
        
        // 1. Staff Tab
        tabbedPane.addTab("Staff Logs", new StaffPanel());
        
        // 2. Admin Tab
        adminPanel = new AdminPanel();
        tabbedPane.addTab("Admin Dashboard", adminPanel);
        
        // 3. Manage Tab
        tabbedPane.addTab("Manage Dispensers", createManagePanel());

        // Refresh Admin charts when clicking that tab
        tabbedPane.addChangeListener(e -> adminPanel.repaint());

        add(tabbedPane);
    }

    private JPanel createManagePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        Theme.stylePanel(panel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel lblId = new JLabel("New Dispenser ID:");
        lblId.setFont(Theme.SUBHEADER_FONT);
        JTextField txtId = new JTextField(15);
        Theme.styleTextField(txtId);
        
        JLabel lblLoc = new JLabel("Location Name:");
        lblLoc.setFont(Theme.SUBHEADER_FONT);
        JTextField txtLoc = new JTextField(15);
        Theme.styleTextField(txtLoc);
        
        JButton btnAdd = new JButton("Add Dispenser");
        Theme.styleButton(btnAdd);

        gbc.gridx=0; gbc.gridy=0; panel.add(lblId, gbc);
        gbc.gridx=1; gbc.gridy=0; panel.add(txtId, gbc);
        gbc.gridx=0; gbc.gridy=1; panel.add(lblLoc, gbc);
        gbc.gridx=1; gbc.gridy=1; panel.add(txtLoc, gbc);
        gbc.gridx=1; gbc.gridy=2; panel.add(btnAdd, gbc);

        btnAdd.addActionListener(e -> {
            String id = txtId.getText().trim();
            String loc = txtLoc.getText().trim();
            
            if(id.isEmpty() || loc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
            
            // 1. Create Object
            Dispenser newDispenser = new Dispenser(id, loc, 0, "08:00 AM");
            
            // 2. Update Memory
            database.put(id, newDispenser);
            
            // 3. Update Database
            DatabaseHandler.insertDispenser(newDispenser);
            
            StaffPanel.refreshList();
            txtId.setText("");
            txtLoc.setText("");
            JOptionPane.showMessageDialog(this, "Dispenser " + id + " Added!");
        });

        return panel;
    }
}