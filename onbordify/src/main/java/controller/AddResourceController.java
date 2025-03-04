package controller;

import Model.Ressource;
import Services.RessourceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class AddResourceController {
    @FXML private TextField titleField, linkField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private Button btnSave, btnCancel;

    private RessourceService ressourceService;
    private ListeRessources parentController; // Reference to refresh the list

    public void setParentController(ListeRessources parent) {
        this.parentController = parent;
    }

    @FXML
    public void initialize() {
        ressourceService = new RessourceService();

        // Populate the ComboBox dynamically
        typeComboBox.setItems(FXCollections.observableArrayList("Vidéo", "Article", "Document PDF"));

        // Event: Save resource
        btnSave.setOnAction(event -> saveResource());

        // Event: Close window
        btnCancel.setOnAction(event -> closeWindow());
    }

    private void saveResource() {
        // Validate fields
        if (titleField.getText().isEmpty() || typeComboBox.getValue() == null || linkField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires !");
            return;
        }

        // Additional validations
        if (!isValidTitle(titleField.getText())) {
            showAlert("Le titre doit contenir au moins 5 caractères et ne peut contenir que des lettres, des chiffres et des espaces.");
            return;
        }

        if (!isValidURL(linkField.getText())) {
            showAlert("Le lien doit commencer uniquement par 'http'.");
            return;
        }

        if (!isValidDescription(descriptionField.getText())) {
            showAlert("La description doit contenir au moins 10 caractères.");
            return;
        }

        try {
            Ressource newRessource = new Ressource();
            newRessource.setTitre(titleField.getText());
            newRessource.setType(typeComboBox.getValue());
            newRessource.setDescription(descriptionField.getText());
            newRessource.setlien(linkField.getText());

            ressourceService.create(newRessource);



            // Close the popup
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout !");
        }
    }

    private boolean isValidTitle(String title) {
        return title.length() >= 3 && title.matches("[A-Za-z0-9 ]+");
    }

    private boolean isValidURL(String url) {
        return url.startsWith("http");

    }

    private boolean isValidDescription(String description) {
        return description.length() >= 10;
    }

    private void closeWindow() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
}
