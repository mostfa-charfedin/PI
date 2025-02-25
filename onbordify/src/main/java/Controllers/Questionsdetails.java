package Controllers;

import Services.QuizService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Question;
import Services.QuestionService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class Questionsdetails {

    @FXML
    private ListView<String> listViewQuiz;

    @FXML
    private Button add_question;

    @FXML
    private Button edit_question;

    @FXML
    private Button remove_question;

    @FXML
    private Label title_label;

    private Question selectedquestion;
    private int selectedQuizId;
    private final QuestionService questionService = new QuestionService();

    public void setQuizId(int quizId) {
        this.selectedQuizId = quizId;
        loadQuestions();
    }

    @FXML
    private void initialize() {
        listViewQuiz.setOnMouseClicked(event -> {
            int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedquestion = questionService.getQuestionsByQuizId(selectedQuizId).get(selectedIndex);

                    // Double-click to open answers
                    if (event.getClickCount() == 2) {
                        openAnswersDetails(selectedquestion.getIdQuestion(), selectedquestion.getQuestion());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void openAnswersDetails(int questionId, String questionText) {
        if (questionId == -1) {
            showAlert("Error", "Invalid question ID.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Answersdetails.fxml"));
            Parent root = loader.load();

            Answersdetails answersDetailsController = loader.getController();
            answersDetailsController.setQuestionDetails(questionId, questionText);

            Stage stage = new Stage();
            stage.setTitle("Answers Details");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Failed to open the Answers Details window.");
            e.printStackTrace();
        }
    }

    private void loadQuestions() {
        try {
            List<Question> questions = questionService.getQuestionsByQuizId(selectedQuizId);
            ObservableList<String> questionTexts = FXCollections.observableArrayList();
            for (Question q : questions) {
                questionTexts.add(q.getQuestion());
            }
            listViewQuiz.setItems(questionTexts);
            listViewQuiz.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (Exception e) {
            showAlert("Error", "Failed to load questions.");
            e.printStackTrace();
        }
    }

    @FXML
    void addQuestion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addquestions.fxml"));
            Parent root = loader.load();

            Addquestions addQuestionsController = loader.getController();
            addQuestionsController.setQuizId(selectedQuizId); // Pass quiz ID to add questions

            Stage popupStage = new Stage();
            popupStage.setTitle("Add Question");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            loadQuestions(); // Refresh after adding
        } catch (IOException e) {
            showAlert("Error", "Failed to open the Add Question window.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void Remove_question(ActionEvent event) {
        int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            showAlert("No Selection", "Please select a question to delete.");
            return;
        }

        try {
            List<Question> questions = questionService.getQuestionsByQuizId(selectedQuizId);
            Question selectedQuestion = questions.get(selectedIndex);
            int questionId = selectedQuestion.getIdQuestion();

            if (questionId == -1) {
                showAlert("Error", "Could not find the question in the database.");
                return;
            }

            // Confirm before deleting
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this question?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database
                questionService.delete(questionId);

                // Refresh list after deletion
                loadQuestions();

                // Show success message
                showAlert("Success", "Question deleted successfully.");
            }

        } catch (Exception e) {
            showAlert("Error", "Failed to delete the question.");
            e.printStackTrace();
        }
    }

    public void cancel_button(ActionEvent actionEvent) {
        Stage stage = (Stage) title_label.getScene().getWindow();
        stage.close();
    }
}
