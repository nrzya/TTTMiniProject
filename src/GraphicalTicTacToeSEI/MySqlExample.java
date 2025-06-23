package GraphicalTicTacToeSEI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class MySqlExample {
    public static void main(String[] args) throws ClassNotFoundException {
        String host, port, databaseName, userName, password;
        host = port = databaseName = userName = password = null;

                host = "mysql-156473cc-tictactoeproject.f.aivencloud.com";
                userName = "avnadmin";
                password = "AVNS_eLPau0fYxH3P0wSTUQG";
                databaseName = "tictactoedb";
                port = "16206";


        // JDBC allows to have nullable username and password
        if (host == null || port == null || databaseName == null) {
            System.out.println("Host, port, database information is required");
            return;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (final Connection connection =
                     DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?sslmode=require", userName, password);
             final Statement statement = connection.createStatement();
             final ResultSet resultSet = statement.executeQuery("SELECT version() AS version")) {

            while (resultSet.next()) {
                System.out.println("Version: " + resultSet.getString("version"));
            }
        } catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
    }
}