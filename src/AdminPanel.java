import javax.swing.*;
import java.awt.*;

public class AdminPanel extends JPanel {

    private JTextArea statsText;

    public AdminPanel() {
        // Simple Layout: We use BorderLayout so the text box fills the whole screen
        setLayout(new BorderLayout(20, 20)); 
        Theme.stylePanel(this);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- 1. The Title ---
        JLabel title = new JLabel("Daily Refill Statistics");
        title.setFont(Theme.HEADER_FONT);
        title.setForeground(Theme.PRIMARY);

        // --- 2. The Text Area (Where stats will be printed) ---
        statsText = new JTextArea();
        statsText.setFont(Theme.SUBHEADER_FONT);
        statsText.setEditable(false); // Make it read-only so users can't type in it
        statsText.setMargin(new Insets(15, 15, 15, 15)); // Add some inner padding

        // Add a scrollbar just in case there are many dispensers
        JScrollPane scrollPane = new JScrollPane(statsText);

        // --- 3. Add to Screen ---
        add(title, BorderLayout.NORTH);  // Title goes at the top
        add(scrollPane, BorderLayout.CENTER); // Text box fills the rest of the space
    }

    // This method is called by WaterLoggerApp every time you click the Admin tab
    public void refreshStats() {
        statsText.setText(""); // Wipe the old text clean
        
        statsText.append("=== LIVE CAMPUS DISPENSER STATUS ===\n\n");
        
        // Loop through our memory and print each dispenser's details
        for (Dispenser d : WaterLoggerApp.database.values()) {
            statsText.append("ID: " + d.id + "\n");
            statsText.append("Location: " + d.location + "\n");
            statsText.append("Refills Today: " + d.refillsToday + "\n");
            statsText.append("Last Service: " + d.lastRefillTime + "\n");
            statsText.append("--------------------------------------------------\n");
        }
    }
}