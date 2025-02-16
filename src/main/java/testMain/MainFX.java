package testMain;

import utils.MyDb;


import java.sql.Connection;

public class MainFX {
    public static void main(String[] args) {
        Connection connection = MyDb.getInstance().getConnection();
    }
}