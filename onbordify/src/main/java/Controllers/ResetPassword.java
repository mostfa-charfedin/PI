package Controllers;

import Services.EmailService;
import Services.SMSService;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ResetPassword {

    @FXML
    private TextField emailReset;

    @FXML
    private Label messageLabel;

    @FXML
    private Button resetButton;




    EmailService emailService = new EmailService();
    UserService userService = new UserService();
    SMSService smsService = new SMSService();
    @FXML
    public void initialize() {

        emailReset.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                messageLabel.setText("Please enter a valid email address.");
                messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            } else {
                this.messageLabel.setText("");
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
    void SendEmail() throws SQLException {
        String email = emailReset.getText();
        if (email == null || email.isEmpty()) {
            messageLabel.setText("Veuillez entrer votre email.");
            messageLabel.setStyle("-fx-text-fill: #ff0000;"); // Rouge pour les erreurs
            return;
        }
        try {
            boolean emailSent = sendResetEmail(email);
            if (emailSent) {
                smsService.sendSMS("+21652148247", "you are changing your password");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/verifierCode.fxml"));
                Parent root = loader.load();
                messageLabel.getScene().setRoot(root);

            } else {
                messageLabel.setText("Erreur : l'email n'a pas pu être envoyé.");
                messageLabel.setStyle("-fx-text-fill: #ff0000;"); // Rouge pour les erreurs
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private boolean sendResetEmail(String email) throws SQLException {
        String code = userService.generateCode(email);
        if (code != null) {
            String message = "Bonjour, " + "\n" + "Voila votre code : " + " " + code;
            emailService.sendEmail(emailReset.getText(), message, "Rest password");
        } else {
            return false;
        }
        return true;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Backtologin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        emailReset.getScene().setRoot(root);
    }

}
