import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // Change these values to match your local MySQL setup.
    private static final String URL =
            System.getenv().getOrDefault("EVENTEASE_DB_URL",
                    "jdbc:mysql://localhost:3306/eventease_db?serverTimezone=UTC");
    private static final String USER =
            System.getenv().getOrDefault("EVENTEASE_DB_USER", "root");
    private static final String PASSWORD =
            System.getenv().getOrDefault("EVENTEASE_DB_PASSWORD", "password");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initialize() throws SQLException {
        try (Connection connection = getConnection()) {
            // Opening a connection here checks that the settings work.
        }
    }
}
