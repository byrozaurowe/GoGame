import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    private static String URL = "jdbc:mysql//localhost:3306/GoDatabase";
    private static String USER = "root";
    private static String PSSWD = "1704";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PSSWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void main(String []args) throws SQLException {
        try (Connection connect = DBConnector.connect()) {
            System.out.println("Connected to database");
        }
    }
}
