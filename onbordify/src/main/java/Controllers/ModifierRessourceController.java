package Controllers;

import Models.Ressource;
import Services.RessourceService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModifierRessourceController {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private Button btnEnregistrer;

    private RessourceService ressourceService;
    private Ressource ressource;

    // Method to set the ressource details from another controller or view
    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
        // Check if fields are correctly initialized before calling setText
        if (titreField != null && descriptionField != null ) {
            titreField.setText(ressource.getTitre());
            descriptionField.setText(ressource.getDescription());
        } else {
            System.err.println("Error: Fields are not initialized.");
        }
    }

    @FXML
    private void initialize() {
        // Initialize the service and the button click event
        ressourceService = new RessourceService();

        btnEnregistrer.setOnAction(event -> {
            try {
                // validation
                if (titreField.getText().isEmpty() || descriptionField.getText().isEmpty() ) {
                    System.err.println("Please fill in all fields before saving.");
                    return; // Exit if any field is empty
                }

                // Update the resource with the new values from the fields
                ressource.setTitre(titreField.getText());
                ressource.setDescription(descriptionField.getText());

                // Call the service to update the resource
                ressourceService.update(ressource);

                // Close the window after modification
                Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    private void handleSaveAction() {
        try {
            // Mise à jour des valeurs
            ressource.setTitre(titreField.getText());
            ressource.setDescription(descriptionField.getText());

            ressourceService.update(ressource);

            // Fermer la fenêtre après modification
            Stage stage = (Stage) btnEnregistrer.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
