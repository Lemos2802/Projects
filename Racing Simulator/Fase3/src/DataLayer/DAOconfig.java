package DataLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAOconfig {
    static final String DB_Piloto = "jdbc:mysql";

    static final String DB_HOST = "localhost";
    static final String DB_PORT = "3306";
    static final String DB_DATABASE = "RacingSimulator";

    static final String USERNAME = "guest";
    static final String PASSWORD = "guest123";

    static final String URL = DB_Piloto+"://"+DB_HOST+":"+DB_PORT+"/"+DB_DATABASE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL + DB_DATABASE, USERNAME, PASSWORD);
    }
    public static Connection getConnectionNoDatabase() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static String getDatabaseName() {
        return DB_DATABASE;
    }
}