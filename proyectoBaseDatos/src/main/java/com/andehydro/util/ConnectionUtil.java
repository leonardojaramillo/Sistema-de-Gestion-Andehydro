package main.java.com.andehydro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

    private static String user;
    private static String pass;
    private static String host = "localhost";
    private static String port = "1521";
    private static String service = "xe";

    public static void setCredentials(String u, String p, String h, String po, String s) {
        user = u;
        pass = p;
        host = h;
        port = po;
        service = s;
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@" + host + ":" + port + "/" + service;
        return DriverManager.getConnection(url, user, pass);
    }
}
