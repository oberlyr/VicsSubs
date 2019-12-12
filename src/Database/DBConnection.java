package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    private Connection connection;
    private String url = "jdbc:mysql://johnny.heliohost.org:3306/vicssubs_VicsSubs?useSSL=false";
    private String user = "vicssubs";
    private String pass = "GoldTeamRules";

    public Connection getConnection() throws SQLException
    {
        connection = DriverManager.getConnection(url, user, pass);

        return connection;

    }

}
