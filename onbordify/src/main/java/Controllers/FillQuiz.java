package Controllers;

import Services.QuestionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modles.Question;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FillQuiz {

    @FXML
    private Label name_quiz;

    @FXML
    private Pane pane_lv;

    @FXML
    private Pane panel_l;

    @FXML
    private Label title_label;
    @FXML
    private ListView<String> listViewQuestion;

    QuestionService questionService = new QuestionService();
    Question selectedquestion;

    @FXML
    void add_question(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateQuestions.fxml"));
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
    private void loadquestion() {
        listViewQuestion.getItems().clear();
        try {
            List<Question> quest = questionService.getAll();

            for (Question q : quest) {
                listViewQuestion.getItems().add(q.getQuestion() + " --" + q.getIdQuiz() );


            }} catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("An error occurred while loading quizzes.");
            alert.showAndWait();
        }}

    @FXML
    public  void initialize() {


        listViewQuestion.setOnMouseClicked(event -> {
            int selectedIndex = listViewQuestion.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    this.selectedquestion = questionService.getAll().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    @FXML
    void cancel_button(ActionEvent event) {
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
    }

    @FXML
    void save_edit(ActionEvent event) {

    }
    @FXML
    void Edit_question(ActionEvent event) {
        try {
            // Charger l'interface de création de quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditQuestions.fxml"));
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
    }

    @FXML
    void Remove_question(ActionEvent event) {
        if (selectedquestion == null) {
            // Si aucun quiz n'est sélectionné, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setContentText("Veuillez sélectionner un quiz à supprimer.");
            alert.showAndWait();
            return;
        }
        try {
            questionService.delete(selectedquestion.getIdQuiz());
            loadquestion();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setContentText("Quiz supprimé avec succès !");
            alert.showAndWait();
        }  catch (Exception e){
            // Show success alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Erreur lors de la suppression du quiz.");
            alert.showAndWait();


    }





}}
