package Controllers;

import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Quiz;

import java.sql.Date; // استخدم java.sql.Date بدلاً من LocalDate
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
        LocalDate localDate = quiz_date.getValue(); // أخذ التاريخ من DatePicker

        if (name.isEmpty() || localDate == null) {
            status.setText("All fields are required!");
            return;
        }
        if (!name.matches("^[a-zA-Z]+$")) {
            status.setText("Le titre ne doit contenir que des lettres (pas de nombres ni d'espaces).");
            return;
        }

        // تحويل LocalDate إلى java.sql.Date
        Date date = Date.valueOf(localDate); // هنا يتم التحويل

        Quiz quiz = new Quiz(name, date); // استخدام java.sql.Date هنا
        QuizService quizService = new QuizService();

        try {
            quizService.create(quiz); // تمرير التاريخ المحول إلى قاعدة البيانات

            if (ListQuizController != null) {
                ListQuizController.loadQuiz(); // تحديث قائمة الاختبارات بعد الإضافة
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
