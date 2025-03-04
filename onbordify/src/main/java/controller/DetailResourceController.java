package controller;

import Model.Ressource;
import Services.EvaluationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DetailResourceController {

    @FXML private Label labelTitre;
    @FXML private Label labelType;
    @FXML private Label labelDescription;
    @FXML private Label labelLien;
    @FXML private Slider ratingSlider; // Composant pour saisir la note

    private Ressource ressource;
    private EvaluationService evaluationService;

    // Constructeur pour initialiser EvaluationService
    public DetailResourceController() {
        this.evaluationService = new EvaluationService();
    }

    // Méthode pour afficher les détails de la ressource
    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
        labelTitre.setText(ressource.getTitre());
        labelType.setText(ressource.getType());
        labelDescription.setText(ressource.getDescription());
        labelLien.setText(ressource.getlien());
    }

    // Méthode pour fermer la fenêtre
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) labelTitre.getScene().getWindow();
        stage.close();
    }

    // Méthode appelée lorsque l'utilisateur clique sur le bouton "Noter"
    @FXML
    private void noterRessource() {
        double note = ratingSlider.getValue();
        int idResource = ressource.getIdResource();
        int idUser   = 1; // Remplacer par l'ID de l'utilisateur connecté

        // On peut aussi vérifier ici que la note est dans l'intervalle autorisé
        if (note < 0 || note > 5) {
            showAlert("Erreur", "La note doit être comprise entre 0 et 5.");
            return;
        }

        // Appel de la méthode upsert pour insérer ou mettre à jour l'évaluation
        evaluationService.ajouterOuMettreAJourEvaluation(idResource, idUser  , note);
        showAlert("Succès", "Votre évaluation a été enregistrée avec succès.");
    }

    // Méthode utilitaire pour afficher un message à l'utilisateur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
