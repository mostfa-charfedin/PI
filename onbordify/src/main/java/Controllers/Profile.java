package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import utils.UserSession;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class Profile {
    @FXML
    private TextField nomField; // Champ pour le nom

    @FXML
    private TextField prenomField; // Champ pour le prénom

    @FXML
    private TextField emailField; // Champ pour l'email

    @FXML
    private DatePicker dateNaissanceField; // Champ pour la date de naissance

    @FXML
    private TextField cinField; // Champ pour le CIN

    @FXML
    private Button saveButton; // Bouton Enregistrer

    @FXML
    private Button backButton; // Bouton Retour

    @FXML
    private Label messageLabel; // Message d'erreur ou de succès

    UserService userService = new UserService();
    UserSession session = UserSession.getInstance();
    User user = new User();

    @FXML
    public void initialize() {

        loadUserData();
    }

    private void loadUserData() {
        int userId = session.getUserId();
        Role role = session.getRole();
        user = userService.findUserById(String.valueOf(userId));
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        if (user.getDateNaissance() != null) {
            java.util.Date utilDate = new java.util.Date(user.getDateNaissance().getTime());
            Instant instant = utilDate.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            dateNaissanceField.setValue(localDate);
        } else {
            dateNaissanceField.setValue(null);
        }
        cinField.setText(String.valueOf(user.getCin()));

    }

    @FXML
    void handleSave() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String cin = cinField.getText();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || dateNaissance == null || cin.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs obligatoires.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }


        boolean updateSuccessful = updateProfile(nom, prenom, email, dateNaissance, cin);

        if (updateSuccessful) {
            messageLabel.setText("Profil mis à jour avec succès.");
            messageLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 12px;"); // Vert pour les succès
        } else {
            messageLabel.setText("Erreur lors de la mise à jour du profil.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;"); // Rouge pour les erreurs
        }
    }

    @FXML
    void handleBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GestionUser.fxml"));
        Parent root = loader.load();
        messageLabel.getScene().setRoot(root);
    }


    private boolean updateProfile(String nom, String prenom, String email, LocalDate dateNaissance, String cin) {
        try {
            // Créer un nouvel utilisateur avec les données du formulaire
            User Newuser = new User();
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
            user.setCin(Integer.parseInt(cin));
            user.setRole(session.getRole());
            this.userService.update(Newuser);
            return true;
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        return false;
    }
}

