import javax.swing.*;
import java.awt.*;

public class Theme {
    // --- COLOR PALETTE (Ocean Theme) ---
    public static final Color PRIMARY = new Color(0, 105, 148);     // Deep Sea Blue
    public static final Color SECONDARY = new Color(235, 245, 250); // Very Light Blue
    public static final Color ACCENT = new Color(255, 255, 255);    // White
    public static final Color TEXT_DARK = new Color(50, 50, 60);    // Dark Gray

    // --- FONTS ---
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBHEADER_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // --- HELPER: STYLE A BUTTON ---
    public static void styleButton(JButton btn) {
        btn.setBackground(PRIMARY);
        btn.setForeground(ACCENT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- HELPER: STYLE A PANEL ---
    public static void stylePanel(JPanel panel) {
        panel.setBackground(SECONDARY);
    }
    
    // --- HELPER: STYLE A TEXT FIELD ---
    public static void styleTextField(JTextField txt) {
        txt.setFont(NORMAL_FONT);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY, 1), 
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
}