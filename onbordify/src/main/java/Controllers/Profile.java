package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

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
    private TextField numField;
    @FXML
    private Button saveButton; // Bouton Enregistrer

    @FXML
    private Button backButton; // Bouton Retour

    @FXML
    private Label messageLabel; // Message d'erreur ou de succès

    @FXML
    private ImageView image;

    @FXML
    private Label role;

    UserService userService = new UserService();
    UserSession session = UserSession.getInstance();
    User user = new User();

    @FXML
    public void initialize() {
        loadUserData();
        image.setOnMouseClicked(event -> handleImageClick());
    }

    private void loadUserData() {
        int userId = session.getUserId();
        Role roleSession = session.getRole();
        user = userService.findUserById(String.valueOf(userId));
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
        role.setText(roleSession.toString());
        numField.setText(String.valueOf( user.getNum_phone()));
        if (user.getImage_url() != null) {

            String imagePath = user.getImage_url();
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                image.setImage(new Image(imageUrl.toString()));
            } else {
                System.out.println("Image introuvable à l'emplacement : " + imagePath);
            }
        }

        if (user.getDateNaissance() != null) {
            Date utilDate = new Date(user.getDateNaissance().getTime());
            Instant instant = utilDate.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            dateNaissanceField.setValue(localDate);
        } else {
            dateNaissanceField.setValue(null);
        }
        cinField.setText(String.valueOf(user.getCin()));

    }
private String pathSelectedimage = user.getImage_url();


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
                // Définir le dossier de destination dans votre projet
                File directory = new File("src/main/resources/uploads/");
                if (!directory.exists()) {
                    directory.mkdirs(); // Créer le dossier s'il n'existe pas
                }

                // Nom de fichier unique pour éviter les conflits
                String newFileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                File destinationFile = new File(directory, newFileName);

                // Copier le fichier dans le dossier du projet
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Mettre à jour l'affichage de l'image
                String imagePath = destinationFile.toURI().toString();
                image.setImage(new Image(imagePath));

                // Enregistrer le chemin relatif dans l'objet utilisateur
                pathSelectedimage="/uploads/" + newFileName;
                System.out.println(pathSelectedimage);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur", "Impossible de sauvegarder l'image.");
            }
        }
    }


    @FXML
    void handleSave() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        LocalDate dateNaissance = dateNaissanceField.getValue();
        String cin = cinField.getText();
        String num_phone = numField.getText();

        // Vérifier si les champs sont vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || dateNaissance == null || cin.isEmpty() || num_phone.isEmpty()) {
            messageLabel.setText("Veuillez remplir tous les champs obligatoires.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            return;
        }


        boolean updateSuccessful = updateProfile(nom, prenom, email, dateNaissance, cin, pathSelectedimage, num_phone);

        if (updateSuccessful) {
            messageLabel.setText("Profil mis à jour avec succès.");
            messageLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 12px;");// Vert pour les succès
            loadUserData();
        } else {
            messageLabel.setText("Erreur lors de la mise à jour du profil.");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;"); // Rouge pour les erreurs
        }
    }


    private boolean updateProfile(String nom, String prenom, String email, LocalDate dateNaissance, String cin, String imageUrl,String num_phone) {
        try {
            user.setNom(nom);
            user.setPrenom(prenom);
            user.setEmail(email);
            user.setDateNaissance(java.sql.Date.valueOf(dateNaissance));
            user.setCin(Integer.parseInt(cin));
            user.setRole(session.getRole());
            user.setImage_url(imageUrl);
            user.setNum_phone(Integer.parseInt( num_phone));
            // Vérification si l'image a été changée
            if (user.getImage_url() != null && !user.getImage_url().isEmpty()) {
                userService.update(user);
                initialize();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.showAndWait();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

