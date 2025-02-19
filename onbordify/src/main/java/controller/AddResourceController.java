package controller;

import Model.Ressource;
import Services.RessourceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        if (titleField.getText().isEmpty() || typeComboBox.getValue() == null || linkField.getText().isEmpty()) {
            showAlert("Veuillez remplir tous les champs obligatoires !");
            return;
        }

        try {
            Ressource newRessource = new Ressource();
            newRessource.setTitre(titleField.getText());
            newRessource.setType(typeComboBox.getValue());
            newRessource.setDescription(descriptionField.getText());
            newRessource.setlien(linkField.getText());

            ressourceService.create(newRessource);
            /*

            // Refresh the list in ListeRessources
            if (parentController != null) {
                parentController.loadRessources();
            } */

            // Close the popup
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout !");
        }
    }

    private void closeWindow() {
        ((Stage) btnCancel.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.showAndWait();
    }
}
