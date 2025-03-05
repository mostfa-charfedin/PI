package controller;

import Model.Ressource;
import Services.EvaluationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class DetailResourceController {

    @FXML private Label labelTitre;
    @FXML private Label labelType;
    @FXML private Label labelDescription;
    @FXML private Label labelLien;
    @FXML private Slider ratingSlider;

    private Ressource ressource;
    private EvaluationService evaluationService;

    // Constructeur pour initialiser EvaluationService
    public DetailResourceController() {
        this.evaluationService = new EvaluationService();
    }

    // Méthode pour afficher les détails de la ressource
    public void setRessource(Ressource ressource) {
        this.ressource = ressource;
        if (ressource != null) {
            labelTitre.setText(ressource.getTitre());
            labelType.setText(ressource.getType());
            labelDescription.setText(ressource.getDescription());
            labelLien.setText(ressource.getlien());

            System.out.println("Chargement de la ressource : " + ressource.getIdResource());
        } else {
            System.out.println("Erreur : Ressource null !");
        }
    }

    // Fermer la fenêtre
    @FXML
    private void fermerFenetre() {
        Stage stage = (Stage) labelTitre.getScene().getWindow();
        stage.close();
    }

    // Soumettre une note
    @FXML
    private void noterRessource() {
        if (ressource == null) {
            showAlert("Erreur", "Aucune ressource sélectionnée.");
            return;
        }

        double note = ratingSlider.getValue();
        int idResource = ressource.getIdResource();
        int idUser = 1; // TODO: Remplacer par l'utilisateur connecté

        if (note < 0 || note > 5) {
            showAlert("Erreur", "La note doit être entre 0 et 5.");
            return;
        }

        try {
            evaluationService.ajouterOuMettreAJourEvaluation(idResource, idUser, note);
            showAlert("Succès", "Votre évaluation a été enregistrée avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter la note.");
        }
    }

    // Afficher un message d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
