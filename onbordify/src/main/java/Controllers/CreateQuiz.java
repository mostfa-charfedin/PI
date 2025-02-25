package Controllers;

import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Quiz;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class CreateQuiz {

    private ListQuiz ListQuizController;

    @FXML
    private Button add_button, cancel_button;

    @FXML
    private DatePicker quiz_date;

    @FXML
    private TextField quiz_name;

    @FXML
    private Label status;

    public void setListQuizController(ListQuiz listQuizController) {
        this.ListQuizController = listQuizController;
    }



    private void resetFields() {
        quiz_name.clear();
        quiz_date.setValue(null);
    }

    @FXML
    void add_button(ActionEvent event) {
        String name = quiz_name.getText();
        LocalDate localDate = quiz_date.getValue();

        if (name.isEmpty() || localDate == null) {
            status.setText("All fields are required!");
            return;
        }
        if (!name.matches("^[a-zA-Z]+$")) {
            status.setText("Le titre ne doit contenir que des lettres (pas de nombres ni d'espaces).");
            return;
        }


        Date date = Date.valueOf(localDate);

        Quiz quiz = new Quiz(name, date);
        QuizService quizService = new QuizService();

        try {
            quizService.create(quiz);

            if (ListQuizController != null) {
                ListQuizController.loadQuiz();
            }
            resetFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Quiz created successfully!");
            alert.showAndWait();

            Stage stage = (Stage) add_button.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            status.setText("Error while creating quiz.");
            e.printStackTrace();
        }
    }

    @FXML
    void cancel_button(ActionEvent event) {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }
}
