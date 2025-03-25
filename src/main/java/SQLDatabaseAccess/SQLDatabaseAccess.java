package SQLDatabaseAccess;

import java.sql.*;


public class SQLDatabaseAccess {

    public static Connection connectionUserLogin244;
    public static Connection connectionCardInfo247;


    public static Connection getConnectionUserLogin244 () {

        try {
            if (connectionUserLogin244 == null || connectionUserLogin244.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connectionUserLogin244 = DriverManager.getConnection("jdbc:sqlserver://10.195.105.244; encrypt=false ; trusteaservercertificate = false", "AppDB", "rKC61m20");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connectionUserLogin244;
    }


    public static Connection getConnectionCardInfo247 () {

        try {
            if (connectionCardInfo247 == null || connectionCardInfo247.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connectionCardInfo247 = DriverManager.getConnection("jdbc:sqlserver://10.195.105.247; encrypt=false ; trusteaservercertificate = false", "AppDB", "rKC61m20");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connectionCardInfo247;
    }

}




