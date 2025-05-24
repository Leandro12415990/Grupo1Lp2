package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static final String URL = "ctespbd.dei.isep.ipp.pt";
    private static final String USER = " 2025_LP2_G1_FEIRA";
    private static final String PASSWORD = "grupo1Lp2";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
