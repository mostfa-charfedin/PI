package TestMain;

import Services.UserService;
import utils.MyDb;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection connection = MyDb.getMydb().getConnection();

        UserService userService = new UserService();
        boolean isAuthenticated = userService.login("t@gmail.com", "@bQBz$b2w7");
        System.out.println("Connexion réussie ? " + isAuthenticated);
    }
}
