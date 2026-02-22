import java.sql.*;
import java.util.Map;

public class DatabaseHandler {
    // 1. DATABASE CONFIGURATION
    private static final String URL = "jdbc:mysql://localhost:3306/campus_water_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // 2. LOAD DATA (SELECT)
    public static void loadFromDB(Map<String, Dispenser> database) {
        String query = "SELECT * FROM dispensers";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            database.clear(); 
            
            while (rs.next()) {
                String id = rs.getString("id");
                String loc = rs.getString("location");
                int refills = rs.getInt("refills_today");
                String time = rs.getString("last_refill_time");

                // Add to App Memory
                database.put(id, new Dispenser(id, loc, refills, time));
            }
            System.out.println("Data loaded from MySQL successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error: Could not connect to Database.");
        }
    }

    // 3. ADD NEW DISPENSER (INSERT)
    public static void insertDispenser(Dispenser d) {
        String query = "INSERT INTO dispensers (id, location, refills_today, last_refill_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, d.id);
            pstmt.setString(2, d.location);
            pstmt.setInt(3, d.refillsToday);
            pstmt.setString(4, d.lastRefillTime);
            
            pstmt.executeUpdate();
            System.out.println("Saved new dispenser to MySQL.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. UPDATE REFILLS (UPDATE)
    public static void updateDispenser(Dispenser d) {
        String query = "UPDATE dispensers SET refills_today = ?, last_refill_time = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, d.refillsToday);
            pstmt.setString(2, d.lastRefillTime);
            pstmt.setString(3, d.id);
            
            pstmt.executeUpdate();
            System.out.println("Updated refill stats in MySQL.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}