import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static String URL = "jdbc:postgresql://localhost:5432/storage_database";
    private static String USER = "user529";
    private static String PASSWORD = "root";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            throw new RuntimeException(e);
        }
    }
}
