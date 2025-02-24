package Controllers;

import Services.EmailService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.regex.Pattern;

public class ResetPassword {
    @FXML
    private TextField emailReset; // Champ pour l'email

    @FXML
    private Button resetButton; // Bouton pour envoyer la demande

    @FXML
    private Label messageLabel; // Message d'erreur ou de succès

    EmailService emailService = new EmailService();

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
    void handleResetPassword() {
        String email = emailReset.getText();

        // Vérifier si l'email est valide
        if (email == null || email.isEmpty()) {
            messageLabel.setText("Veuillez entrer votre email.");
            messageLabel.setStyle("-fx-text-fill: #ff0000;"); // Rouge pour les erreurs
            return;
        }

        // Simuler l'envoi d'un email de réinitialisation
        boolean emailSent = sendResetEmail(email);

        if (emailSent) {
            messageLabel.setText("Un email de réinitialisation a été envoyé à " + email);
            messageLabel.setStyle("-fx-text-fill: #4CAF50;"); // Vert pour les succès
        } else {
            messageLabel.setText("Erreur : l'email n'a pas pu être envoyé.");
            messageLabel.setStyle("-fx-text-fill: #ff0000;"); // Rouge pour les erreurs
        }
    }


    private boolean sendResetEmail(String email) {
        emailService.sendEmail(emailReset.getText(),"hello", "rest password");
        return true;
    }




}
