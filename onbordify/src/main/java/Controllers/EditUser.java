package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class EditUser {

    @FXML
    private TextField cinMod;

    @FXML
    private DatePicker dateMod;

    @FXML
    private TextField emailMod;

    @FXML
    private Label messageMod;

    @FXML
    private TextField nomMod;

    @FXML
    private TextField prenomMod;

    @FXML
    private ComboBox<Role> roleMod;

    @FXML
    private VBox cinErrorBox;

    @FXML
    private VBox nomErrorBox;

    @FXML
    private VBox prenomErrorBox;

    @FXML
    private VBox dateNaissanceErrorBox;

    @FXML
    private VBox emailErrorBox;

    private User selectedUser;

    UserService userservice = new UserService();

    @FXML
    public void initialize() {
        // Contrôle de saisie pour le CIn (uniquement des nombres)
        cinMod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Seuls les chiffres sont autorisés
                cinMod.setText(oldValue); // Rejeter la nouvelle valeur
                showError(cinErrorBox, "Le CIn doit contenir uniquement des chiffres.");
            } else {
                clearError(cinErrorBox);
            }
        });

        // Contrôle de saisie pour la Date de Naissance
        dateMod.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDate today = LocalDate.now();
                int age = Period.between(newValue, today).getYears();

                if (age < 18) {
                    dateMod.setValue(oldValue); // Rétablir l'ancienne valeur
                    showError(dateNaissanceErrorBox, "The employee must be at least 18 years old.");
                } else {
                    clearError(dateNaissanceErrorBox);
                }
            }
        });

        // Contrôle de saisie pour le Nom (uniquement des lettres)
        nomMod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) { // Seules les lettres et espaces sont autorisés
                nomMod.setText(oldValue); // Rejeter la nouvelle valeur
                showError(nomErrorBox, "Le nom ne doit contenir que des lettres.");
            } else {
                clearError(nomErrorBox);
            }
        });

        // Contrôle de saisie pour le Prénom (uniquement des lettres)
        prenomMod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) { // Seules les lettres et espaces sont autorisés
                prenomMod.setText(oldValue); // Rejeter la nouvelle valeur
                showError(prenomErrorBox, "Le prénom ne doit contenir que des lettres.");
            } else {
                clearError(prenomErrorBox);
            }
        });

        // Contrôle de saisie pour l'Email (format valide)
        emailMod.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) { // Vérifier le format de l'email
                showError(emailErrorBox, "Veuillez entrer une adresse email valide.");
            } else {
                clearError(emailErrorBox);
            }
        });
    }

    // Méthode pour valider le format de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Méthode pour afficher un message d'erreur sous un champ
    private void showError(VBox errorBox, String message) {
        errorBox.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorBox.getChildren().add(errorLabel);
    }

    // Méthode pour effacer un message d'erreur
    private void clearError(VBox errorBox) {
        errorBox.getChildren().clear();
    }


    @FXML
    void updateUser(ActionEvent event) {
        try {
            // Vérifier que tous les champs sont valides avant de mettre à jour
            if (cinMod.getText().isEmpty() || nomMod.getText().isEmpty() || prenomMod.getText().isEmpty() || emailMod.getText().isEmpty() || dateMod.getValue() == null || roleMod.getValue() == null) {
                messageMod.setText("Please fill in all fields.");
                return;
            }

            if (!isValidEmail(emailMod.getText())) {
                showError(emailErrorBox, "Please enter a valid email address.");
                return;
            }

            // Créer un nouvel utilisateur avec les données du formulaire
            User Newuser = new User(
                    this.nomMod.getText(),
                    this.prenomMod.getText(),
                    this.emailMod.getText(),
                    Integer.parseInt(this.cinMod.getText()),
                    java.sql.Date.valueOf(this.dateMod.getValue()), // LocalDate
                    this.roleMod.getValue()

            );
            Newuser.setId(selectedUser.getId()); // Définir l'ID de l'utilisateur sélectionné
            this.userservice.update(Newuser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("User updated successfully");
            alert.showAndWait();
            reset();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml"));
            Parent root = loader.load();
            nomMod.getScene().setRoot(root);

        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setUserData(User user) {
        this.selectedUser = user;
        nomMod.setText(user.getNom());
        prenomMod.setText(user.getPrenom());
        emailMod.setText(user.getEmail());
        cinMod.setText(String.valueOf(user.getCin()));


        if (user.getDateNaissance() != null) {
            java.util.Date utilDate = new java.util.Date(user.getDateNaissance().getTime());
            Instant instant = utilDate.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            dateMod.setValue(localDate);
        } else {
            dateMod.setValue(null);
        }

        // Initialiser la ComboBox des rôles
        if (roleMod.getItems().isEmpty()) {
            roleMod.setItems(FXCollections.observableArrayList(Role.values()));
        }
        roleMod.setValue(user.getRole());
    }

    public LocalDate convertSqlDateToLocalDate(Date sqlDate) {
        Instant instant = sqlDate.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    void reset() {
        this.nomMod.clear();
        this.prenomMod.clear();
        this.emailMod.clear();
        this.cinMod.clear();
        this.dateMod.setValue(null);
        this.roleMod.setItems(null);
        this.selectedUser= null;
    }
}