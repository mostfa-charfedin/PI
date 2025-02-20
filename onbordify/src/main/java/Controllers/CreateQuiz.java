package Controllers;

import Services.QuizService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modles.Quiz;
import java.io.IOException;


public class CreateQuiz {
private ListQuiz ListQuiz;
    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    @FXML
    private DatePicker quiz_date;

    @FXML
    private TextField quiz_name;

    @FXML
    private Label lblstatus;
    private void resetFields() {
        quiz_name.clear();
        quiz_date.setValue(null);
    }

    @FXML
    void add_button(ActionEvent event) {

        String name = quiz_name.getText();
        String date = quiz_date.getValue().toString();
        QuizService quizService = new QuizService();
        Quiz q = new Quiz(name, date);

        //if (date.isEmpty() || name.isEmpty() ) {
            //lblstatus.setText("All fields are required!");
            //return;
       // }

        try {

            quizService.create(q);
            resetFields();

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Project created successfully!");
            alert.showAndWait();

            // Close the current window
            Stage stage = (Stage) add_button.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void cancel_button (ActionEvent event) {
        try {
            // Charger l'interface de création de quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListQuiz.fxml"));
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


    }}


