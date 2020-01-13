import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static String URL = "";
    private static String USER = "";
    private static String PSSWD = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PSSWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
