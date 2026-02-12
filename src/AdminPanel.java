import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends JPanel {

    // Store map coordinates
    private Map<String, Point> mapCoordinates = new HashMap<>();

    public AdminPanel() {
        setLayout(new GridLayout(1, 2, 20, 0)); 
        Theme.stylePanel(this);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Setup Coordinates for the Map (X, Y)
        mapCoordinates.put("D-101", new Point(90, 110));  // Library
        mapCoordinates.put("D-102", new Point(220, 260)); // Gym
        mapCoordinates.put("D-103", new Point(250, 100)); // Science
        mapCoordinates.put("D-104", new Point(100, 250)); // Admin
        mapCoordinates.put("D-105", new Point(180, 180)); // Cafeteria

        add(createChartPanel());
        add(createMapPanel());
    }

    private JPanel createChartPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Title
                g2.setColor(Theme.TEXT_DARK);
                g2.setFont(Theme.HEADER_FONT);
                g2.drawString("Daily Refill Stats", 30, 40);

                // Axes
                int startX = 50;
                int startY = getHeight() - 50;
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(startX, startY, getWidth() - 30, startY);
                g2.drawLine(startX, startY, startX, 60);

                // Bars
                int x = startX + 30;
                int barWidth = 40;
                int maxBarHeight = startY - 80;

                g2.setFont(Theme.NORMAL_FONT);

                for (String key : WaterLoggerApp.database.keySet()) {
                    Dispenser d = WaterLoggerApp.database.get(key);
                    int height = d.refillsToday * 20;
                    if (height > maxBarHeight) height = maxBarHeight;

                    // Dynamic Color
                    if (d.refillsToday > 5) g2.setColor(new Color(220, 50, 50));
                    else g2.setColor(Theme.PRIMARY);
                    
                    g2.fillRoundRect(x, startY - height, barWidth, height, 10, 10);
                    g2.setColor(Theme.TEXT_DARK);
                    g2.drawString(d.id, x, startY + 20);
                    
                    x += 60;
                }
            }
        };
    }

    private JPanel createMapPanel() {
        JPanel mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Draw Map Background (Light Gray/Beige)
                g2.setColor(new Color(245, 245, 240)); 
                g2.fillRect(0, 0, getWidth(), getHeight());

                // 2. Draw "Grass" Areas (Green)
                g2.setColor(new Color(200, 230, 200)); 
                g2.fillOval(50, 60, 250, 180); // Park Area 1
                g2.fillOval(300, 200, 200, 150); // Park Area 2

                // 3. Draw "Buildings" (Darker Gray Rectangles)
                g2.setColor(new Color(220, 220, 220)); 
                g2.fillRoundRect(80, 100, 120, 80, 10, 10); // Library Building
                g2.fillRoundRect(220, 250, 100, 100, 10, 10); // Gym Building

                // 4. Draw Building Labels
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString("LIBRARY", 100, 145);
                g2.drawString("GYM", 250, 300);

                // 5. Draw Title
                g2.setColor(Theme.TEXT_DARK);
                g2.setFont(Theme.HEADER_FONT);
                g2.drawString("Live Campus Map", 20, 35);
                g2.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                g2.setColor(Color.GRAY);
                g2.drawString("(Click dots for details)", 20, 55);

                // 6. DRAW THE DOTS (From Database)
                for (String key : WaterLoggerApp.database.keySet()) {
                    if (mapCoordinates.containsKey(key)) {
                        Point p = mapCoordinates.get(key);
                        Dispenser d = WaterLoggerApp.database.get(key);
                        
                        // Logic: Gray if 0 refills, Blue if active
                        Color dotColor = (d.refillsToday == 0) ? Color.GRAY : Theme.PRIMARY;
                        
                        // Draw the Dot
                        g2.setColor(dotColor);
                        g2.fillOval(p.x, p.y, 24, 24); // Bigger dot
                        
                        // Draw "W" inside
                        g2.setColor(Color.WHITE);
                        g2.setFont(new Font("Arial", Font.BOLD, 14));
                        g2.drawString("W", p.x+5, p.y+17);
                        
                        // Draw Label above
                        g2.setColor(Theme.TEXT_DARK);
                        g2.setFont(new Font("Arial", Font.BOLD, 11));
                        g2.drawString(key, p.x, p.y - 5);
                    }
                }
            }
        };

        // ... (Keep your MouseListeners here exactly as they were) ...
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX(), e.getY());
            }
        });
        
        mapPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleHover(e.getX(), e.getY(), mapPanel);
            }
        });

        return mapPanel;
    }

    private void handleClick(int mouseX, int mouseY) {
        for (String key : WaterLoggerApp.database.keySet()) {
            if (mapCoordinates.containsKey(key)) {
                Point p = mapCoordinates.get(key);
                double distance = Math.sqrt(Math.pow(mouseX - (p.x + 10), 2) + Math.pow(mouseY - (p.y + 10), 2));
                
                if (distance < 15) {
                    Dispenser d = WaterLoggerApp.database.get(key);
                    String message = "ID: " + d.id + "\nLocation: " + d.location + "\nRefills Today: " + d.refillsToday + "\nLast Service: " + d.lastRefillTime;
                    JOptionPane.showMessageDialog(this, message, "Dispenser Status", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        }
    }
    
    private void handleHover(int mouseX, int mouseY, JPanel panel) {
        boolean hovering = false;
        for (Point p : mapCoordinates.values()) {
             double distance = Math.sqrt(Math.pow(mouseX - (p.x + 10), 2) + Math.pow(mouseY - (p.y + 10), 2));
             if (distance < 15) { hovering = true; break; }
        }
        panel.setCursor(hovering ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
    }
}