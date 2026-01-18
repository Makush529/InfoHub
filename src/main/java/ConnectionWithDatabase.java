import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionWithDatabase {
    public ConnectionWithDatabase() throws SQLException {
        Connection connection = DatabaseConfig.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQLCommands.CREATE_USER);
        ResultSet resultSet;
    }
}
