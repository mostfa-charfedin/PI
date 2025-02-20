package controller;

import Model.Ressource;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetailResourceController {

    @FXML private Label labelTitre;
    @FXML private Label labelType;
    @FXML private Label labelDescription;
    @FXML private Label labelLien;

    private Ressource ressource;

    // Méthode pour passer la ressource à l'interface de détails
    public void setRessource(Ressource ressource) {
        this.ressource = ressource;

        // Afficher les détails de la ressource dans les labels
        labelTitre.setText(ressource.getTitre());
        labelType.setText(ressource.getType());
        labelDescription.setText(ressource.getDescription());
        labelLien.setText(ressource.getlien());
    }

    // Méthode pour fermer la fenêtre
    @FXML
    private void fermerFenetre() {
        // Récupérer la fenêtre (Stage) actuelle et la fermer
        Stage stage = (Stage) labelTitre.getScene().getWindow();
        stage.close();
    }
}
