package Controllers;

import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import modles.Quiz;
import java.sql.Date;
import java.time.LocalDate;

public class EditQuiz {

    @FXML
    private Label status;

    @FXML
    private TextArea question_field1;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button save;

    @FXML
    private Button cancel_button;

    private Quiz selectedQuiz;
    private QuizService quizService;
    private ListQuiz listQuizController;

    @FXML
    private void initialize() {
        quizService = new QuizService();
    }

    public void setQuiz(Quiz quiz) {
        this.selectedQuiz = quiz;
        if (quiz != null) {
            question_field1.setText(quiz.getNom());
            if (quiz.getDateCreation() != null) {
                datePicker.setValue(quiz.getDateCreation().toLocalDate());
            }
        }
    }

    public void setListQuizController(ListQuiz controller) {
        this.listQuizController = controller;
    }

    @FXML
    private void save(ActionEvent event) {
        if (selectedQuiz != null) {
            selectedQuiz.setNom(question_field1.getText());
            LocalDate localDate = datePicker.getValue();
            if (localDate != null) {
                selectedQuiz.setDateCreation(Date.valueOf(localDate));
            }

            try {
                quizService.update(selectedQuiz);
                status.setText("Quiz updated successfully!");
                listQuizController.refreshQuiz();
                closeWindow();
            } catch (Exception e) {
                status.setText("Error updating quiz.");
                e.printStackTrace();
            }
        } else {
            status.setText("No quiz selected.");
        }
    }

    @FXML
    private void cancel_button(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }
}
