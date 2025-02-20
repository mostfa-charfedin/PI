package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import Services.QuestionService;
import javafx.stage.Stage;
import modles.Question;

import java.io.IOException;

public class CreateQuestions {

    @FXML
    private Button Remove_ques;

    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    @FXML
    private ToggleButton correct_button;

    @FXML
    private TextArea question_field;

     QuestionService questionService = new QuestionService();
     int currentQuizId;

    public void setCurrentQuizId(int quizId) {
        this.currentQuizId = quizId; // Setter pour l'ID du quiz
    }

    @FXML
    void Remove_ques(ActionEvent event) {
        try {
            String questionText = question_field.getText();
            if (questionText.isEmpty()) {
                showAlert("Warning", "Please enter a question to remove.");
                return;
            }
            // Suppose we delete by question text or id, you could pass the ID as a parameter if needed
            Question question = findQuestionByText(questionText);
            if (question != null) {
                questionService.delete(question.getIdQuestion());  // Supprimer la question
                showAlert("Success", "Question removed successfully.");
                question_field.clear(); // Réinitialiser le champ question
            } else {
                showAlert("Error", "Question not found.");
            }
        } catch (Exception e) {
            showAlert("Error", "Error while removing question: " + e.getMessage());
        }
    }

    @FXML
    void add_button(ActionEvent event) {
        String questionText = question_field.getText();
        if (questionText.isEmpty()) {
            showAlert("Warning", "Please enter a question.");
            return;
        }

        try {
            // Créer une nouvelle question
            Question newQuestion = new Question(0, questionText, currentQuizId);
            questionService.create(newQuestion);  // Ajouter la question à la base de données
            showAlert("Success", "Question added successfully.");
            question_field.clear(); // Réinitialiser le champ question
        } catch (Exception e) {
            showAlert("Error", "Error while adding question: " + e.getMessage());
        }
    }

    @FXML
    void cancel_button(ActionEvent event) {
        try {
            // Charger l'interface de création de quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FillQuiz.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Liste Quiz"); // Nom de la fenêtre
            stage.setScene(new Scene(root));
            stage.show(); // Afficher la nouvelle fenêtre
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de création de quiz.");
        }
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void correct_button(ActionEvent event) {
        // Si vous gérez une question de réponse correcte, vous pouvez marquer la question comme correcte
        // Par exemple, ajouter une colonne "correct" à votre table Question ou gérer cela ailleurs
        boolean isCorrect = correct_button.isSelected();
        if (isCorrect) {

            // Marquer la question comme correcte (dépend de la logique de votre application)
            showAlert("Info", "This question is marked as correct.");
        } else {
            showAlert("Info", "This question is not marked as correct.");
        }
    }

     Question findQuestionByText(String text) {
        try {
            for (Question question : questionService.getAll()) {
                if (question.getQuestion().equals(text)) {
                    return question; // Retourner la question trouvée
                }
            }
        } catch (Exception e) {
            showAlert("Error", "Error while retrieving questions.");
        }
        return null;
    }

}
