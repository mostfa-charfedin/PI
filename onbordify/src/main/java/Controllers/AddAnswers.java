package Controllers;

import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Reponse;
import modles.Question;

import java.io.IOException;

public class AddAnswers {

    @FXML
    private Button add_button;

    @FXML
    private ToggleButton statut; // Le bouton toggle pour marquer une réponse comme correcte

    @FXML
    private Button cancel_button;

    @FXML
    private TextField answer; // Champ de texte pour la réponse

    // Cette méthode permet de réinitialiser les champs de saisie
    private void resetFields() {
        answer.clear();
        statut.setSelected(false); // Réinitialiser le statut
    }

    // Lorsque l'utilisateur clique sur le bouton pour ajouter une réponse
    @FXML
    void add_button(ActionEvent event) {
        String ans = answer.getText();  // Récupérer la réponse saisie
        boolean isCorrect = statut.isSelected();  // Vérifier si la réponse est correcte

        if (ans.isEmpty()) {
            // Si la réponse est vide, afficher un message d'avertissement
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez saisir une réponse.");
            alert.showAndWait();
            return;
        }

        try {
            // Créer une nouvelle réponse
            Reponse reponse = new Reponse(ans, isCorrect ? "correct" : "incorrect");

            // Utiliser le service pour ajouter la réponse à la base de données
            ReponseService reponseService = new ReponseService();
            reponseService.create(reponse);  // Ajoute la réponse à la base de données

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Réponse ajoutée avec succès !");
            alert.showAndWait();

            resetFields();  // Réinitialiser les champs de saisie après ajout

            // Fermer la fenêtre actuelle
            Stage stage = (Stage) add_button.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            // Si une erreur survient, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Une erreur est survenue : " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Lorsque l'utilisateur clique sur le bouton pour annuler l'ajout
    @FXML
    void cancel_button(ActionEvent event) {
        try {
            // Charger la vue précédente (par exemple, la gestion des questions)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateQuestions.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Créer une Question"); // Nom de la fenêtre
            stage.setScene(new Scene(root));
            stage.show(); // Afficher la nouvelle fenêtre
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de création de questions.");
        }
    }
}