package Controllers;

import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import modles.Reponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javafx.scene.control.ButtonType;

public class Answersdetails {
    private final ReponseService answerService = new ReponseService();

    @FXML
    private ListView<String> listViewQuiz;

    @FXML
    private Pane panel_l;

    @FXML
    private Label title_label;

    private int questionId;
    private List<Reponse> answers; // Keep track of answers

    // Method to remove a selected answer from both UI & Database
    @FXML
    void Remove_answer(ActionEvent event) {
        int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("Erreur", "Veuillez sélectionner une réponse à supprimer.");
            return;
        }

        // Retrieve the actual Reponse object from the list
        Reponse selectedAnswer = answers.get(selectedIndex);
        int answerId = selectedAnswer.getIdReponse();

        // Confirmation Alert before deleting
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation de suppression");
        confirmationAlert.setHeaderText("Voulez-vous vraiment supprimer cette réponse ?");
        confirmationAlert.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Delete from database
                answerService.delete(answerId);

                // Remove from UI ListView
                listViewQuiz.getItems().remove(selectedIndex);
                answers.remove(selectedIndex); // Remove from tracked list

                showAlert("Succès", "Réponse supprimée avec succès.");
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de supprimer la réponse.");
                e.printStackTrace();
            }
        }
    }

    // Method to cancel and close the window
    @FXML
    void cancel_button(ActionEvent event) {
        panel_l.getScene().getWindow().hide();
    }

    // Show an alert for errors or confirmation messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to set question details and load answers
    public void setQuestionDetails(int questionId, String selectedQuestionText) {
        this.questionId = questionId;
        title_label.setText("Question: " + selectedQuestionText);

        try {
            loadAnswersForQuestion(questionId);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les réponses.");
            e.printStackTrace();
        }
    }

    private void loadAnswersForQuestion(int questionId) throws SQLException {
        listViewQuiz.getItems().clear();

        answers = answerService.getReponsesByQuestionId(questionId); // Fetch answers once
        if (answers.isEmpty()) {
            listViewQuiz.getItems().add("Aucune réponse trouvée");
        } else {
            for (Reponse answer : answers) {
                String formattedAnswer = answer.getReponse() + " - " + answer.getStatut();
                listViewQuiz.getItems().add(formattedAnswer);
            }
        }
    }
}
