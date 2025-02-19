package Controllers;

import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import modles.Quiz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListQuiz {

    @FXML
    private Pane panel_l;

    @FXML
    private TextField search_field;

    @FXML
    private Label title_label;

    @FXML
    private ListView<String> listViewQuiz;

     QuizService Quizservice = new QuizService();
     Quiz selectedquiz;


    // Create a new quiz when the button is clicked
    @FXML
    void create_quiz_action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateQuiz.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Créer un Quiz");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de création de quiz.");
        }
    }



    @FXML
    void edit_quiz_action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FillQuiz.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Créer un Quiz");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de création de quiz.");
        }
    }




    @FXML
    void remove_quiz_action(ActionEvent event) {
        try {
            Quizservice.delete(selectedquiz.getIdQuiz());
            initialize();
        } catch (SQLException e) {
            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("quiz deleted successfully!");
            alert.showAndWait();
        }

    }
    @FXML
    public void initialize() {
        loadQuiz();

        listViewQuiz.setOnMouseClicked(event -> {
            int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    this.selectedquiz = Quizservice.getAll().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void loadQuiz() {
        listViewQuiz.getItems().clear();
        try {
            List<Quiz> quizes = Quizservice.getAll();

            for (Quiz q : quizes) {
                listViewQuiz.getItems().add(q.getNom() + " --" + q.getDateCreation() );
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("quiz deleted successfully!");
            alert.showAndWait();
        }
    }



    }




