package Java.Util;

import java.sql.*;

public class DataAccess {
    private static final String url = "jdbc:mysql://localhost:3306?autoReconnect=true&useSSL=false";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public void getInfoBySerial(String serial) {
        String query = "select * from bank.cards WHERE serial = " + serial;

        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                ConsoleWriter.writeMessage(rs.getInt("pin"));
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void close(){
        try {
            con.close();
        } catch (SQLException se) { }
        try {
            stmt.close();
        } catch (SQLException se) {  }
        try {
            rs.close();
        } catch (SQLException se) { }
    }
}
