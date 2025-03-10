package Controllers;

import Models.Statut;
import Models.User;
import Services.EmailService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import utils.UserSession;

import java.io.IOException;
import java.util.regex.Pattern;

public class login {
    UserService userService = new UserService();

    @FXML
    private TextField emailField;

    @FXML
    private Label errorMessage;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        // Contrôle de saisie pour l'Email
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                errorMessage.setText("Please enter a valid email address.");
                errorMessage.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            } else {
                this.errorMessage.setText("");
            }
        });
    }


    // Méthode pour valider le format de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }


    @FXML
    void resetPassword(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restPassword.fxml"));

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
            boolean isAuthenticated = userService.login(email, password);

            if (!isAuthenticated) {
                errorMessage.setText("Incorrect email or password!");
                return;
            }

            // Fetch the user only if login is successful
            User user = userService.findUserByEmail(email);
            System.out.println(user);
            // Check if the user is blocked
            if (user.getStatus() == Statut.BLOCKED) {  // assuming UserStatus is an enum
                showAlert("Error", "Your account is blocked!");
                return;  // Stop further execution
            }

            // Successful login
            showAlert("Success", "Connected successfully!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml"));
            Parent root = loader.load();
            emailField.getScene().setRoot(root);

        } catch (Exception e) {
            showAlert("Error", "Server connection problem!");
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
