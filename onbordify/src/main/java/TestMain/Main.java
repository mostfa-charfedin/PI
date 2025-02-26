package TestMain;

import Services.UserService;
import utils.MyDb;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args)  {
        Connection connection = MyDb.getMydb().getConnection();

    }
}
