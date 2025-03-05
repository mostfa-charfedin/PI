package Controllers;

import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Quiz;
import Services.EmailService;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.scene.control.DateCell;

import java.sql.Date;
import java.time.LocalDate;

public class CreateQuiz {
    private final EmailService emailService = new EmailService();
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

    @FXML
    private void initialize() {
        // Disable past dates in DatePicker
        quiz_date.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ff9999;"); // Highlight disabled dates
                }
            }
        });
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
        if (localDate.isBefore(LocalDate.now())) {
            status.setText("La date du quiz ne peut pas être dans le passé !");
            return;
        }

        Date date = Date.valueOf(localDate);
        Quiz quiz = new Quiz(name, date);
        QuizService quizService = new QuizService();

        try {
            quizService.create(quiz);
            emailService.sendEmail("akrimi041@gmail.com", "Quiz Created", "A new quiz has been created, you can take it now!");

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
    }}