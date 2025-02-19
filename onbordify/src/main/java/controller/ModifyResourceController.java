package controller;

import Services.RessourceService;
import Model.Ressource;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModifyResourceController {

    @FXML
    private TextField titleField; // Titre de la ressource
    @FXML
    private ComboBox<String> typeComboBox; // Type de la ressource
    @FXML
    private TextArea descriptionField; // Description de la ressource
    @FXML
    private TextField linkField; // Lien de la ressource
    @FXML
    private Button btnSave; // Bouton pour sauvegarder
    @FXML
    private Button btnCancel; // Bouton pour annuler

    private Ressource ressourceToModify; // Ressource à modifier

    private RessourceService ressourceService; // Service de gestion des ressources

    public ModifyResourceController() {
        // Initialisation du service
        ressourceService = new RessourceService();
    }

    // Cette méthode est appelée pour remplir les champs avec les informations de la ressource à modifier
    public void setRessource(Ressource ressource) {
        if (ressource == null) {
            System.out.println("Erreur : ressource null");
            return;
        }
        this.ressourceToModify = ressource;

        // Vérification des données de la ressource
        System.out.println("Chargement de la ressource : " + ressource.getTitre());

        // Remplir les champs avec les données de la ressource
        titleField.setText(ressource.getTitre());
        typeComboBox.setValue(ressource.getType());
        descriptionField.setText(ressource.getDescription());
        linkField.setText(ressource.getlien());
    }


    // Méthode pour sauvegarder les changements dans la base de données
    @FXML

    private void saveChanges() {
        try {
            System.out.println("Tentative de mise à jour de la ressource : " + ressourceToModify.getTitre());

            // Mise à jour de l'objet
            ressourceToModify.setTitre(titleField.getText());
            ressourceToModify.setType(typeComboBox.getValue());
            ressourceToModify.setDescription(descriptionField.getText());
            ressourceToModify.setlien(linkField.getText());

            // Mise à jour en base de données
            ressourceService.update(ressourceToModify);

            System.out.println("Mise à jour réussie !");

            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de la sauvegarde des modifications.");
        }
    }


    // Méthode pour fermer la fenêtre sans enregistrer les modifications
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Méthode pour afficher un message d'erreur
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
