import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaffPanel extends JPanel {
    
    public static DefaultListModel<String> listModel = new DefaultListModel<>(); 
    private JList<String> dispenserList = new JList<>(listModel);
    
    private JLabel lblLocation = new JLabel("Location: --");
    private JLabel lblStatus = new JLabel("Status: --");
    private JLabel lblLastRefill = new JLabel("Last Refill: --");
    
    private String selectedId = null;

    public StaffPanel() {
        // Simple Layout: 1 Row, 2 Columns
        setLayout(new GridLayout(1, 2, 20, 20)); 
        Theme.stylePanel(this);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- LEFT SIDE: The List ---
        refreshList(); 
        JScrollPane listScroll = new JScrollPane(dispenserList);
        listScroll.setBorder(BorderFactory.createTitledBorder("Select Dispenser"));
        add(listScroll); // Add to the left column

        // --- RIGHT SIDE: The Details ---
        // Simple Layout: 5 Rows, 1 Column
        JPanel detailPanel = new JPanel(new GridLayout(5, 1, 10, 10)); 
        Theme.stylePanel(detailPanel);
        
        JButton btnLog = new JButton("LOG REFILL NOW");
        Theme.styleButton(btnLog);

        detailPanel.add(new JLabel("Dispenser Details"));
        detailPanel.add(lblLocation);
        detailPanel.add(lblStatus);
        detailPanel.add(lblLastRefill);
        detailPanel.add(btnLog);
        
        add(detailPanel); // Add to the right column

        // --- LOGIC ---
        // 1. When a user clicks a name in the list
        dispenserList.addListSelectionListener(e -> {
            selectedId = dispenserList.getSelectedValue();
            if (selectedId != null) {
                Dispenser d = WaterLoggerApp.database.get(selectedId);
                lblLocation.setText("Location: " + d.location);
                lblStatus.setText("Status: Active");
                lblLastRefill.setText("Last Refill: " + d.lastRefillTime);
            }
        });

        // 2. When a user clicks the Log Button
        btnLog.addActionListener(e -> {
            if (selectedId != null) {
                Dispenser d = WaterLoggerApp.database.get(selectedId);
                
                // Update Time and Count
                d.lastRefillTime = new SimpleDateFormat("hh:mm a").format(new Date());
                d.refillsToday++; 
                
                // Update Screen and Database
                lblLastRefill.setText("Last Refill: " + d.lastRefillTime); 
                DatabaseHandler.updateDispenser(d); 
                
                JOptionPane.showMessageDialog(this, "Refill Logged!");
            }
        });
    }

    public static void refreshList() {
        listModel.clear();
        for(String id : WaterLoggerApp.database.keySet()) {
            listModel.addElement(id);
        }
    }
}