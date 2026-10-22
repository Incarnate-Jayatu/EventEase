import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EventManager {
    public ArrayList<Event> getAllEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY event_date";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(rs.getInt("id"), rs.getString("name"),
                        rs.getString("category"), rs.getDate("event_date").toLocalDate(),
                        rs.getString("venue"), rs.getInt("capacity")));
            }
        }
        return events;
    }

    public void addEvent(String name, String category, LocalDate date,
                         String venue, int capacity) throws SQLException {
        String sql = "INSERT INTO events(name, category, event_date, venue, capacity) VALUES(?,?,?,?,?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, venue);
            ps.setInt(5, capacity);
            ps.executeUpdate();
        }
    }

    public void updateEvent(int id, String name, String category, LocalDate date,
                            String venue, int capacity) throws SQLException {
        String sql = "UPDATE events SET name=?, category=?, event_date=?, venue=?, capacity=? WHERE id=?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDate(3, Date.valueOf(date));
            ps.setString(4, venue);
            ps.setInt(5, capacity);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void deleteEvent(int id) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM events WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
