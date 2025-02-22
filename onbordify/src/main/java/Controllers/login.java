package Controllers;

import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class login {
    UserService userService = new UserService();
    @FXML
    private TextField emailField;

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    void resetPassword(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(""));

        try {
            Parent root = loader.load();
            emailField.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            boolean isAuthenticated = userService.login(emailField.getText(), passwordField.getText());

            if (isAuthenticated) {
                showAlert("Success", "Connected successfuly !");

                // Rediriger vers une autre fenêtre si besoin
            } else {
                errorMessage.setText("Incorrect email or password!");
            }
        } catch (Exception e) {
            showAlert("Erreur", "Server connection problem!");
            System.out.println(e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
