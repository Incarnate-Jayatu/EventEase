import java.sql.*;
import java.util.ArrayList;

public class RegistrationManager {
    public ArrayList<Registration> getAllRegistrations() throws SQLException {
        ArrayList<Registration> list = new ArrayList<>();
        String sql = "SELECT r.id, r.student_name, r.email, e.name AS event_name, r.status, r.attendance " +
                     "FROM registrations r JOIN events e ON r.event_id=e.id " +
                     "WHERE r.status <> 'CANCELLED' ORDER BY r.registered_at";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Registration(rs.getInt("id"), rs.getString("student_name"),
                        rs.getString("email"), rs.getString("event_name"),
                        rs.getString("status"), rs.getString("attendance")));
            }
        }
        return list;
    }

    public void addRegistration(String student, String email, int eventId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                int capacity;
                String capSql = "SELECT capacity FROM events WHERE id=? FOR UPDATE";
                try (PreparedStatement ps = con.prepareStatement(capSql)) {
                    ps.setInt(1, eventId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Please select a valid event.");
                        capacity = rs.getInt("capacity");
                    }
                }

                int confirmed = 0;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT COUNT(*) FROM registrations WHERE event_id=? AND status='CONFIRMED'")) {
                    ps.setInt(1, eventId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) confirmed = rs.getInt(1);
                    }
                }

                String status = confirmed < capacity ? "CONFIRMED" : "WAITLISTED";
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO registrations(student_name,email,event_id,status) VALUES(?,?,?,?)")) {
                    ps.setString(1, student);
                    ps.setString(2, email);
                    ps.setInt(3, eventId);
                    ps.setString(4, status);
                    ps.executeUpdate();
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void cancelRegistration(int registrationId) throws SQLException {
        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false);
            try {
                int eventId = -1;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT event_id FROM registrations WHERE id=? AND status='CONFIRMED' FOR UPDATE")) {
                    ps.setInt(1, registrationId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) eventId = rs.getInt("event_id");
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE registrations SET status='CANCELLED' WHERE id=?")) {
                    ps.setInt(1, registrationId);
                    ps.executeUpdate();
                }

                if (eventId != -1) {
                    String nextSql = "SELECT id FROM registrations WHERE event_id=? AND status='WAITLISTED' " +
                                     "ORDER BY registered_at, id LIMIT 1 FOR UPDATE";
                    try (PreparedStatement ps = con.prepareStatement(nextSql)) {
                        ps.setInt(1, eventId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                int nextId = rs.getInt("id");
                                try (PreparedStatement promote = con.prepareStatement(
                                        "UPDATE registrations SET status='CONFIRMED' WHERE id=?")) {
                                    promote.setInt(1, nextId);
                                    promote.executeUpdate();
                                }
                            }
                        }
                    }
                }
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public void markAttendance(int registrationId, String attendance) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE registrations SET attendance=? WHERE id=? AND status='CONFIRMED'")) {
            ps.setString(1, attendance);
            ps.setInt(2, registrationId);
            ps.executeUpdate();
        }
    }

    public int countByStatus(String status) throws SQLException {
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM registrations WHERE status=?")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}
