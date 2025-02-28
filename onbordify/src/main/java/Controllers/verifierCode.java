package Controllers;

import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;

public class verifierCode {

    @FXML
    private TextField codeField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private Button resetButton;

    UserService userService = new UserService();

    @FXML
    public void initialize() {

        resetButton.setDisable(true);

        codeField.textProperty().addListener((observable, oldValue, newValue) -> validateCode(newValue));
        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validatePassword(newValue));
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> validateConfirmPassword(newValue));
    }


    private void validateCode(String code) {
        if (code.isEmpty()) {
            messageLabel.setText("Veuillez entrer le code reçu par email.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        } else {
            messageLabel.setText("");
            resetButton.setDisable(newPasswordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty());
        }
    }

    // Méthode pour valider le nouveau mot de passe
    private void validatePassword(String password) {
        if (password.isEmpty()) {
            messageLabel.setText("Veuillez entrer un nouveau mot de passe.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            resetButton.setDisable(true);
        } else if (!isPasswordStrong(password)) {
            messageLabel.setText("Le mot de passe doit contenir au moins 8 caractères, une majuscule et un chiffre.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            resetButton.setDisable(true);
        } else {
            messageLabel.setText("");
            resetButton.setDisable(codeField.getText().isEmpty() || confirmPasswordField.getText().isEmpty());
        }
    }

    // Méthode pour valider la confirmation du mot de passe
    private void validateConfirmPassword(String confirmPassword) {
        String newPassword = newPasswordField.getText();

        if (confirmPassword.isEmpty()) {
            messageLabel.setText("Veuillez confirmer votre nouveau mot de passe.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            resetButton.setDisable(true);
        } else if (!confirmPassword.equals(newPassword)) {
            messageLabel.setText("Les mots de passe ne correspondent pas.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            resetButton.setDisable(true);
        } else {
            messageLabel.setText("");
            resetButton.setDisable(codeField.getText().isEmpty() || newPasswordField.getText().isEmpty());
        }
    }

    // Méthode pour vérifier la force du mot de passe
    private boolean isPasswordStrong(String password) {
        // Vérifier que le mot de passe a au moins 8 caractères
        if (password.length() < 8) {
            return false;
        }
        // Vérifier que le mot de passe contient au moins une majuscule
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        // Vérifier que le mot de passe contient au moins un chiffre
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }

    @FXML
    void handleResetPassword() {
        String code = codeField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Vérifier si les champs sont vides (au cas où le bouton est activé autrement)
        if (code.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }

        // Vérifier si le nouveau mot de passe et la confirmation correspondent
        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Les mots de passe ne correspondent pas.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }


        try {
            userService.updatePassword(code, newPassword);
            showAlert("Success", "Mot de passe réinitialisé avec succès.");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Parent root = loader.load();
            messageLabel.getScene().setRoot(root);
        } catch (SQLException e) {

            messageLabel.setText("code invalide ou problème lors de la réinitialisation.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            throw new RuntimeException(e);


        } catch (IOException e) {
            throw new RuntimeException(e);
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
