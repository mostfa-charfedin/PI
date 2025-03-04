package Controllers;

import Services.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Models.Question;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Addquestions {

    @FXML
    private Label quiz_name_label;

    @FXML
    private TextArea question_field;

    @FXML
    private ListView<String> listViewQuestion; // Update to store formatted question strings

    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    @FXML
    private Button Remove_ques;

    @FXML
    private Button add_Answer;

    @FXML
    private Label status;

    private QuestionService questionService;
    private int selectedQuizId;
    private ObservableList<String> questionItems = FXCollections.observableArrayList(); // Store formatted questions

    public Addquestions() {
        this.questionService = new QuestionService();
    }

    public void setQuizName(String quizName) {
        quiz_name_label.setText("Quiz: " + quizName);
    }

    public void setSelectedQuizId(int quizId) {
        this.selectedQuizId = quizId;
        loadQuestionsForQuiz();
    }

    @FXML
    private void loadQuestionsForQuiz() {
        try {
            // Clear the list first
            listViewQuestion.getItems().clear();

            // Fetch the selected quiz name
            String quizName = quiz_name_label.getText().replace("Quiz: ", "");

            // Get the quiz ID based on the name
            int quizId = questionService.getQuizIdByName(quizName);
            if (quizId == -1) {
                status.setText("Error: Quiz not found.");
                return;
            }

            // Fetch all questions for the quiz
            List<Question> questions = questionService.getQuestionsByQuizId(quizId);

            // Add them to the ListView
            for (Question q : questions) {
                listViewQuestion.getItems().add(q.getQuestion() + " -- ID: " + q.getIdQuestion());
            }

        } catch (Exception e) {
            status.setText("Error loading questions.");
            e.printStackTrace();
        }
    }

    @FXML
    private void add_button(ActionEvent event) {
        String questionText = question_field.getText().trim();

        if (questionText.isEmpty()) {
            status.setText("Question field cannot be empty.");
            return;
        }

        String quizName = quiz_name_label.getText().replace("Quiz: ", ""); // Get quiz name

        Question question = new Question(questionText, quizName);

        try {
            // Add the question to the database
            questionService.create(question);

            // Show success message
            status.setText("Question added successfully.");

            // Refresh the ListView immediately
            loadQuestionsForQuiz();

            // Clear input field
            question_field.clear();
        } catch (SQLException e) {
            status.setText("Error adding question to the database.");
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void cancel_button(ActionEvent event) {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void Remove_ques(ActionEvent event) {
        int selectedIndex = listViewQuestion.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            status.setText("Select a question to delete.");
            return;
        }

        try {
            String selectedItem = listViewQuestion.getItems().get(selectedIndex);
            String[] parts = selectedItem.split(" -- ID: ");
            if (parts.length < 2) {
                status.setText("Error: Invalid question format.");
                return;
            }

            int questionId = Integer.parseInt(parts[1]);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("Are you sure you want to delete this question?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                questionService.delete(questionId);
                status.setText("Question deleted successfully!");
                loadQuestionsForQuiz();
            }
        } catch (NumberFormatException e) {
            status.setText("Error: Invalid question ID.");
            e.printStackTrace();
        } catch (Exception e) {
            status.setText("Error deleting question.");
            e.printStackTrace();
        }
    }

    @FXML
    void add_answer(ActionEvent event) {
        int selectedIndex = listViewQuestion.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            status.setText("Select a question to add an answer.");
            return;
        }

        try {
            // Extract question ID from selected item in ListView
            String selectedItem = listViewQuestion.getItems().get(selectedIndex);
            String[] parts = selectedItem.split(" -- ID: ");

            if (parts.length < 2) {
                status.setText("Error: Invalid question format.");
                return;
            }

            int questionId = Integer.parseInt(parts[1]);

            // Load AddAnswers.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddAnswers.fxml"));
            Parent root = loader.load();

            // Pass question ID to AddAnswers controller
            AddAnswers addAnswerController = loader.getController();
            addAnswerController.setQuestionId(questionId);  // Ensure method exists in AddAnswers

            // Show modal
            Stage popupStage = new Stage();
            popupStage.setTitle("Add Answer");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

            // Refresh after adding an answer
            loadQuestionsForQuiz();

        } catch (IOException e) {
            status.setText("Error loading the answer popup.");
            e.printStackTrace();
        }
    }

    public void setQuizId(int selectedQuizId) {
        this.selectedQuizId = selectedQuizId;
        try {
            loadQuestionsForQuiz(); // Load the questions associated with this quiz
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load questions for the selected quiz.");
        }
    }

    private void showAlert(String error, String s) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("error");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}