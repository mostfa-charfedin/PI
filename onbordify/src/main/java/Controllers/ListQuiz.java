package Controllers;

import Services.QuizService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;


public class ListQuiz {

    @FXML
    private Label date_quiz;

    @FXML
    private ImageView delete_quiz;

    @FXML
    private ImageView edit_quiz;

    @FXML
    private TextField name_quiz;


    @FXML
    private Pane pane_lv;

    @FXML
    private Pane panel_l;

    @FXML
    private TextField search_field;

    @FXML
    private Label title_label;

    @FXML
    private ListView<String> listViewProgrammes;

    private QuizService service = new QuizService();
    private ObservableList<modles.Quiz> QuizList = FXCollections.observableArrayList();


    @FXML
    void create_quiz_action(ActionEvent event) {
        try {
            // Charger l'interface de création de quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateQuiz.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Créer un Quiz"); // Nom de la fenêtre
            stage.setScene(new Scene(root));
            stage.show(); // Afficher la nouvelle fenêtre
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement de l'interface de création de quiz.");
        }
    }

}
