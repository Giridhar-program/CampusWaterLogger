import java.sql.*;
import java.util.Map;

public class DatabaseHandler {
    // UPDATE THESE WITH YOUR MYSQL CREDENTIALS
    private static final String URL = "jdbc:mysql://localhost:3306/campus_facilities";
    private static final String USER = "root";
    private static final String PASS = "your_mysql_password"; // Replace with your password

    // Establish Connection
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // ==========================================
    // 1. CREATE (Insert)
    // ==========================================
    public static boolean insertDispenser(Dispenser d) {
        String query = "INSERT INTO dispenser_logs (id, location, refills_today, last_refill_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, d.id);
            stmt.setString(2, d.location);
            stmt.setInt(3, d.refillsToday);
            stmt.setString(4, d.lastRefillTime);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 2. READ (Load all into memory)
    // ==========================================
    public static void loadFromDB(Map<String, Dispenser> database) {
        database.clear(); // Clear existing data before loading
        String query = "SELECT * FROM dispenser_logs";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String id = rs.getString("id");
                String location = rs.getString("location");
                int refillsToday = rs.getInt("refills_today");
                String lastRefillTime = rs.getString("last_refill_time");
                
                Dispenser d = new Dispenser(id, location, refillsToday, lastRefillTime);
                database.put(id, d);
            }
        } catch (SQLException e) {
            System.err.println("Database load failed. Ensure the table exists.");
            e.printStackTrace();
        }
    }

    // ==========================================
    // 3. UPDATE
    // ==========================================
    public static boolean updateDispenser(Dispenser d) {
        String query = "UPDATE dispenser_logs SET location = ?, refills_today = ?, last_refill_time = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, d.location);
            stmt.setInt(2, d.refillsToday);
            stmt.setString(3, d.lastRefillTime);
            stmt.setString(4, d.id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // 4. DELETE
    // ==========================================
    public static boolean deleteDispenser(String id) {
        String query = "DELETE FROM dispenser_logs WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}