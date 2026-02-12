import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StaffPanel extends JPanel {
    
    public static DefaultListModel<String> listModel; 
    private JList<String> dispenserList;
    private JLabel lblLocation, lblStatus, lblLastRefill;
    private String selectedId = null;

    public StaffPanel() {
        setLayout(new BorderLayout());
        Theme.stylePanel(this);

        // --- LEFT PANEL: List ---
        listModel = new DefaultListModel<>();
        refreshList(); 

        dispenserList = new JList<>(listModel);
        dispenserList.setFont(Theme.NORMAL_FONT);
        dispenserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane listScroll = new JScrollPane(dispenserList);
        listScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.PRIMARY), 
                "Select Dispenser", 
                0, 0, Theme.SUBHEADER_FONT, Theme.PRIMARY));

        // --- RIGHT PANEL: Details ---
        JPanel detailPanel = new JPanel();
        Theme.stylePanel(detailPanel);
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel header = new JLabel("Dispenser Details");
        header.setFont(Theme.HEADER_FONT);
        header.setForeground(Theme.PRIMARY);
        
        lblLocation = new JLabel("Location: --");
        lblLocation.setFont(Theme.SUBHEADER_FONT);
        lblStatus = new JLabel("Status: --");
        lblStatus.setFont(Theme.SUBHEADER_FONT);
        lblLastRefill = new JLabel("Last Refill: --");
        lblLastRefill.setFont(Theme.SUBHEADER_FONT);

        JButton btnLog = new JButton("LOG REFILL NOW");
        Theme.styleButton(btnLog);
        btnLog.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add components
        detailPanel.add(header);
        detailPanel.add(Box.createVerticalStrut(20));
        detailPanel.add(lblLocation);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(lblStatus);
        detailPanel.add(Box.createVerticalStrut(10));
        detailPanel.add(lblLastRefill);
        detailPanel.add(Box.createVerticalStrut(40));
        detailPanel.add(btnLog);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScroll, detailPanel);
        splitPane.setDividerLocation(220);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        // --- LOGIC ---
        dispenserList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && dispenserList.getSelectedValue() != null) {
                selectedId = dispenserList.getSelectedValue();
                updateDetails();
            }
        });

        btnLog.addActionListener(e -> {
            if (selectedId != null) {
                Dispenser d = WaterLoggerApp.database.get(selectedId);
                
                // 1. Update Memory
                String time = new SimpleDateFormat("hh:mm a").format(new Date());
                d.lastRefillTime = time;
                d.refillsToday++; 
                
                // 2. Update UI
                updateDetails();
                
                // 3. Update Database (New!)
                DatabaseHandler.updateDispenser(d); 
                
                JOptionPane.showMessageDialog(this, "Refill Logged & Saved to DB!");
            }
        });
    }

    private void updateDetails() {
        if(selectedId == null) return;
        Dispenser d = WaterLoggerApp.database.get(selectedId);
        lblLocation.setText("Location: " + d.location);
        lblStatus.setText("Status: Active");
        lblLastRefill.setText("Last Refill: " + d.lastRefillTime);
    }
    
    public static void refreshList() {
        listModel.clear();
        for(String id : WaterLoggerApp.database.keySet()) {
            listModel.addElement(id);
        }
    }
}