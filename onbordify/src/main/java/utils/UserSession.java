package utils;

import Models.Role;
import javafx.scene.control.Alert;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private Role role;

    private UserSession(int userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    public static void createSession(int userId, Role role) {
        if (instance == null) {
            instance = new UserSession(userId, role);
        }
    }

    public static UserSession getInstance() {
        if (instance == null) {
            showAlert("Session Expired", "No active session. Please log in.");
            return null; // Return null instead of throwing an exception
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public static void destroySession() {
        instance = null;
    }

    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
