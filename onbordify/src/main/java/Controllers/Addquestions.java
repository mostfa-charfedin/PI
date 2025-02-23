package Controllers;

import Services.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Question;

import java.sql.SQLException;
import java.util.List;
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
            List<Question> questions = questionService.getQuestionsByQuizId(selectedQuizId);
            questionItems.clear(); // Clear old data

            for (Question q : questions) {
                questionItems.add(q.getQuestion() + " -- Quiz ID: " + q.getIdQuiz()); // Display question text
            }

            listViewQuestion.setItems(questionItems); // Update ListView
        } catch (Exception e) {
            status.setText("Something went wrong while loading questions.");
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
            questionService.create(question);
            status.setText("Question added successfully.");
            loadQuestionsForQuiz(); // Refresh list
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
            status.setText("No question selected to remove.");
            return;
        }

        String selectedQuestionText = listViewQuestion.getItems().get(selectedIndex).split(" -- ")[0]; // Extract question text

        try {
            Question selectedQuestion = questionService.getQuestionByText(selectedQuestionText, selectedQuizId);
            if (selectedQuestion != null) {
                questionService.delete(selectedQuestion.getIdQuestion());
                status.setText("Question removed successfully.");
                loadQuestionsForQuiz();
            } else {
                status.setText("Error finding selected question.");
            }
        } catch (Exception e) {
            status.setText("Error removing question.");
            e.printStackTrace();
        }
    }

    @FXML
    void add_answer(ActionEvent event) {

    }
}
