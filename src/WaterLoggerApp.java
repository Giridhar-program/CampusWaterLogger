import javax.swing.*;
import java.util.Map;
import java.util.TreeMap;

public class WaterLoggerApp extends JFrame {

    // This is populated by ApplicationLauncher after a successful login
    public static Map<String, Dispenser> database = new TreeMap<>();
    private AdminPanel adminPanel;
    private ManagePanel managePanel;

    public WaterLoggerApp() {
        setTitle("Campus Water System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.SUBHEADER_FONT);
        
        // Add Tabs
        tabbedPane.addTab("Staff Logs", new StaffPanel());
        
        adminPanel = new AdminPanel();
        tabbedPane.addTab("Admin Dashboard", adminPanel);
        
        managePanel = new ManagePanel();
        tabbedPane.addTab("Manage Dispensers", managePanel);

        // Simple trigger to update text when the Admin tab is opened
        tabbedPane.addChangeListener(e -> adminPanel.refreshStats());

        add(tabbedPane);
    }
}