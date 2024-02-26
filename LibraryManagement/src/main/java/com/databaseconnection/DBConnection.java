package com.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    final static String url = "jdbc:oracle:thin:@localhost:1521:xe";
    final static String userName = "system";
    final static String password = "root";
    public static Connection con;
    public static Connection getDBConnection() throws SQLException, ClassNotFoundException {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, userName, password);
            return con;
    }

    public static void closeConnection() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
}
