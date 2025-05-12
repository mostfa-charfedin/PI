package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import utils.UserSession;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Date;

public class Profile {
    // Champs du formulaire
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private DatePicker dateNaissanceField;
    @FXML private TextField cinField;
    @FXML private TextField numField;

    // Boutons
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button cancelButton;

    // Labels pour les messages d'erreur
    @FXML private Label nomErrorLabel;
    @FXML private Label prenomErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label dateErrorLabel;
    @FXML private Label cinErrorLabel;
    @FXML private Label phoneErrorLabel;
    @FXML private Label messageLabel;

    // Autres éléments
    @FXML private ImageView image;
    @FXML private Label role;

    private UserService userService = new UserService();
    private UserSession session = UserSession.getInstance();
    private User user;
    private String originalImagePath;
    private boolean isEditing = false;

    @FXML
    public void initialize() {
        try {
            loadUserData();
            setupValidationListeners();
            setupFieldRestrictions();

            image.setOnMouseClicked(event -> {
                if (isEditing) {
                    handleImageClick();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données du profil.");
        }
    }

    private void setupFieldRestrictions() {
        // Restreindre la saisie aux lettres pour nom et prénom
        nomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÀ-ÿ\\-\\s]*")) {
                nomField.setText(oldVal);
            }
        });

        prenomField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("[a-zA-ZÀ-ÿ\\-\\s]*")) {
                prenomField.setText(oldVal);
            }
        });

        // Restreindre la saisie aux chiffres pour CIN et téléphone
        cinField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                cinField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        numField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                numField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void setupValidationListeners() {
        // Validation en temps réel
        nomField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateName();
        });

        prenomField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePrenom();
        });

        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateEmail();
        });

        dateNaissanceField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateBirthDate();
        });

        cinField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validateCin();
        });

        numField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) validatePhone();
        });
    }

    private void loadUserData() {
        try {
            int userId = session.getUserId();
            Role roleSession = session.getRole();
            user = userService.findUserById(String.valueOf(userId));

            if (user == null) {
                showAlert("Erreur", "Utilisateur non trouvé.");
                return;
            }

            // Mise à jour des champs
            nomField.setText(user.getNom());
            prenomField.setText(user.getPrenom());
            emailField.setText(user.getEmail());
            role.setText(roleSession != null ? roleSession.toString() : "N/A");
            numField.setText(String.valueOf(user.getNum_phone()));

            if (user.getDateNaissance() != null) {
                dateNaissanceField.setValue(new java.sql.Date(user.getDateNaissance().getTime()).toLocalDate());
            }

            cinField.setText(String.valueOf(user.getCin()));

            // Chargement de l'image
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                originalImagePath = user.getImage_url();
                try {
                    URL imageUrl = getClass().getResource(user.getImage_url());
                    if (imageUrl != null) {
                        Image newImage = new Image(imageUrl.toString(), true);
                        newImage.progressProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal.doubleValue() == 1.0) {
                                image.setImage(newImage);
                            }
                        });
                    }
                } catch (Exception e) {
                    System.err.println("Erreur de chargement d'image: " + e.getMessage());
                }
            } else {
                image.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des données utilisateur.");
        }
    }

    @FXML
    void handleEdit() {
        isEditing = true;
        enableFields(true);
        editButton.setVisible(false);
        saveButton.setVisible(true);
        cancelButton.setVisible(true);
        clearAllErrors();
    }

    @FXML
    void handleCancel() {
        isEditing = false;
        enableFields(false);
        editButton.setVisible(true);
        saveButton.setVisible(false);
        cancelButton.setVisible(false);
        loadUserData();
        clearAllErrors();
    }

    @FXML
    void handleSave() {
        clearAllErrors();

        if (!validateFields()) {
            messageLabel.setText("Veuillez corriger les erreurs dans le formulaire");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean updateSuccessful = updateProfile(
                nomField.getText(),
                prenomField.getText(),
                emailField.getText(),
                dateNaissanceField.getValue(),
                cinField.getText(),
                originalImagePath,
                numField.getText()
        );

        if (updateSuccessful) {
            isEditing = false;
            enableFields(false);
            editButton.setVisible(true);
            saveButton.setVisible(false);
            cancelButton.setVisible(false);
            messageLabel.setText("Profil mis à jour avec succès.");
            messageLabel.setStyle("-fx-text-fill: #4CAF50;");
            loadUserData();
        } else {
            messageLabel.setText("Erreur lors de la mise à jour du profil.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void enableFields(boolean enable) {
        nomField.setDisable(!enable);
        prenomField.setDisable(!enable);
        emailField.setDisable(!enable);
        dateNaissanceField.setDisable(!enable);
        cinField.setDisable(!enable);
        numField.setDisable(!enable);
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (!validateName()) isValid = false;
        if (!validatePrenom()) isValid = false;
        if (!validateEmail()) isValid = false;
        if (!validateBirthDate()) isValid = false;
        if (!validateCin()) isValid = false;
        if (!validatePhone()) isValid = false;

        return isValid;
    }

    private boolean validateName() {
        String text = nomField.getText().trim();

        if (text.isEmpty()) {
            showError(nomField, nomErrorLabel, "Le nom est obligatoire");
            return false;
        }

        if (!text.matches("[a-zA-ZÀ-ÿ\\-\\s]+")) {
            showError(nomField, nomErrorLabel, "Caractères non valides");
            return false;
        }

        if (text.length() < 2 || text.length() > 50) {
            showError(nomField, nomErrorLabel, "2-50 caractères requis");
            return false;
        }

        clearError(nomField, nomErrorLabel);
        return true;
    }

    private boolean validatePrenom() {
        String text = prenomField.getText().trim();

        if (text.isEmpty()) {
            showError(prenomField, prenomErrorLabel, "Le prénom est obligatoire");
            return false;
        }

        if (!text.matches("[a-zA-ZÀ-ÿ\\-\\s]+")) {
            showError(prenomField, prenomErrorLabel, "Caractères non valides");
            return false;
        }

        if (text.length() < 2 || text.length() > 50) {
            showError(prenomField, prenomErrorLabel, "2-50 caractères requis");
            return false;
        }

        clearError(prenomField, prenomErrorLabel);
        return true;
    }

    private boolean validateEmail() {
        String text = emailField.getText().trim();

        if (text.isEmpty()) {
            showError(emailField, emailErrorLabel, "L'email est obligatoire");
            return false;
        }

        if (!text.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showError(emailField, emailErrorLabel, "Format email invalide");
            return false;
        }

        clearError(emailField, emailErrorLabel);
        return true;
    }

    private boolean validateBirthDate() {
        LocalDate date = dateNaissanceField.getValue();

        if (date == null) {
            showError(dateNaissanceField, dateErrorLabel, "Date obligatoire");
            return false;
        }

        if (date.isAfter(LocalDate.now().minusYears(18))) {
            showError(dateNaissanceField, dateErrorLabel, "Âge minimum: 18 ans");
            return false;
        }

        if (date.isBefore(LocalDate.now().minusYears(120))) {
            showError(dateNaissanceField, dateErrorLabel, "Date invalide");
            return false;
        }

        clearError(dateNaissanceField, dateErrorLabel);
        return true;
    }

    private boolean validateCin() {
        String text = cinField.getText().trim();

        if (text.isEmpty()) {
            showError(cinField, cinErrorLabel, "CIN obligatoire");
            return false;
        }

        if (!text.matches("\\d{8}")) {
            showError(cinField, cinErrorLabel, "8 chiffres requis");
            return false;
        }

        clearError(cinField, cinErrorLabel);
        return true;
    }

    private boolean validatePhone() {
        String text = numField.getText().trim();

        if (text.isEmpty()) {
            showError(numField, phoneErrorLabel, "Téléphone obligatoire");
            return false;
        }

        if (!text.matches("\\d{8}")) {
            showError(numField, phoneErrorLabel, "8 chiffres requis");
            return false;
        }

        clearError(numField, phoneErrorLabel);
        return true;
    }

    private void showError(Control field, Label errorLabel, String message) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 1.5px;");
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void clearError(Control field, Label errorLabel) {
        field.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    private void clearAllErrors() {
        clearError(nomField, nomErrorLabel);
        clearError(prenomField, prenomErrorLabel);
        clearError(emailField, emailErrorLabel);
        clearError(dateNaissanceField, dateErrorLabel);
        clearError(cinField, cinErrorLabel);
        clearError(numField, phoneErrorLabel);
        messageLabel.setText("");
    }

    @FXML
    void handleImageClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(image.getScene().getWindow());

        if (selectedFile != null) {
            try {
                File directory = new File("src/main/resources/uploads/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String newFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(directory, newFileName);

                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                String imagePath = destinationFile.toURI().toString();
                image.setImage(new Image(imagePath));

                originalImagePath = "/uploads/" + newFileName;
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de sauvegarder l'image.");
            }
        }
    }

    private boolean updateProfile(String nom, String prenom, String email, LocalDate dateNaissance,
                                  String cin, String imageUrl, String num_phone) {
        try {
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
            user.setCin(Integer.parseInt(cin));
            user.setImage_url(imageUrl);
            user.setNum_phone(Integer.parseInt(num_phone));

            userService.update(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", e.getMessage());
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}