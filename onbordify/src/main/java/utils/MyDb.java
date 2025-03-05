package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyDb {
    private Connection conn;
    private final String URL = "jdbc:mysql://localhost:3306/onboardify";
    private final String USER = "root";
    private final String PASSWORD = "";
    private static MyDb mydb;

    private MyDb() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to database");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static MyDb getMydb() {
        if (mydb == null) {

            mydb = new MyDb();
        }
        return mydb;
    }



    public  Connection getConnection() {
        return conn;

    }



}
