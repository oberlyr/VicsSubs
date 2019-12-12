package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/VicsSubs?useSSL=false";
    private String user = "root";
    private String pass = "root";

    public Connection getConnection() throws SQLException
    {
        connection = DriverManager.getConnection(url, user, pass);

        return connection;

    }

}
