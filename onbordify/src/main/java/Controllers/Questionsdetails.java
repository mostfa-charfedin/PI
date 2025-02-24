package Controllers;

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
import java.util.List;

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

    private int selectedQuizId;
    private final QuestionService questionService = new QuestionService();

    public void setQuizId(int quizId) {
        this.selectedQuizId = quizId;
        loadQuestions();
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

   @FXML
    void editQuestion(ActionEvent event) {
       /* String selectedQuestion = listViewQuiz.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/editquestion.fxml"));
                Parent root = loader.load();

                EditQuestion editQuestionController = loader.getController();
                editQuestionController.setQuestionDetails(selectedQuizId, selectedQuestion);

                Stage popupStage = new Stage();
                popupStage.setTitle("Edit Question");
                popupStage.setScene(new Scene(root));
                popupStage.showAndWait();

                loadQuestions(); // Refresh after editing
            } catch (IOException e) {
                showAlert("Error", "Failed to open the Edit Question window.");
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a question to edit.");
        }*/
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Remove_question(ActionEvent actionEvent) {
        String selectedQuestion = listViewQuiz.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            try {
                questionService.deleteQuestionByText(selectedQuestion, selectedQuizId);
                listViewQuiz.getItems().remove(selectedQuestion);
                showAlert("Success", "Question deleted successfully.");
            } catch (Exception e) {
                showAlert("Error", "Failed to delete the question.");
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a question to remove.");
        }
    }

    public void cancel_button(ActionEvent actionEvent) {
        Stage stage = (Stage) title_label.getScene().getWindow();
        stage.close();
    }
}