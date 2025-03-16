package SQLDatabaseAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLDatabaseAccess {

    public static Connection connectionSMSModule;
    public static Connection getConnectionSMSModule () {

        try {
            if (connectionSMSModule == null || connectionSMSModule.isClosed()) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connectionSMSModule = DriverManager.getConnection("jdbc:sqlserver://10.195.105.247; encrypt=false ; trusteaservercertificate = false", "Training", "Aa123456");
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connectionSMSModule;
    }

}




