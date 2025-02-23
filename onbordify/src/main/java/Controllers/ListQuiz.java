package Controllers;

import Services.QuestionService;
import Services.QuizService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modles.Quiz;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListQuiz {

    @FXML
    private Label lblstatus;

    @FXML
    private ListView<String> listViewQuiz;

    @FXML
    private Pane panel_l;

    @FXML
    private TextField search_field;

    @FXML
    private Label title_label;

    private QuizService quizService;
    private Quiz selectedQuiz;
    private ObservableList<String> quizItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        quizService = new QuizService();
        loadQuiz();

        listViewQuiz.setOnMouseClicked(event -> {
            int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedQuiz = quizService.getAll().get(selectedIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        search_field.textProperty().addListener((observable, oldValue, newValue) -> filterRessources(newValue));
    }

    void loadQuiz() {
        try {
            List<Quiz> quizzes = quizService.getAll();
            quizItems.clear();
            for (Quiz q : quizzes) {
                quizItems.add(q.getNom() + " -- " + q.getDateCreation());
            }
            listViewQuiz.setItems(quizItems);
        } catch (Exception e) {
            lblstatus.setText("Something went wrong while loading quizzes.");
            e.printStackTrace();
        }
    }

    @FXML
    private void create_quiz_action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateQuiz.fxml"));
            Parent root = loader.load();

            CreateQuiz createController = loader.getController();
            createController.setListQuizController(this);

            Stage popupStage = new Stage();
            popupStage.setTitle("Créer un Quiz");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            loadQuiz();
        } catch (IOException e) {
            lblstatus.setText("Something went wrong");
            e.printStackTrace();
        }
    }

    @FXML
    private void edit_questions(ActionEvent event) {
        if (selectedQuiz == null) {
            lblstatus.setText("Select a quiz to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditQuiz.fxml"));
            Parent root = loader.load();

            EditQuiz editController = loader.getController();
            editController.setQuiz(selectedQuiz);
            editController.setListQuizController(this);

            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Quiz");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            loadQuiz();
        } catch (IOException e) {
            lblstatus.setText("Something went wrong");
            e.printStackTrace();
        }
    }

    @FXML
    private void remove_quiz_action(ActionEvent event) {
        if (selectedQuiz == null) {
            lblstatus.setText("Select a quiz to delete");
            return;
        }

        try {
            quizService.delete(selectedQuiz.getIdQuiz());
            loadQuiz();
            lblstatus.setText("Quiz deleted successfully!");
        } catch (Exception e) {
            lblstatus.setText("Error deleting quiz.");
            e.printStackTrace();
        }
    }

    public void refreshQuiz() {
        loadQuiz();
    }

    @FXML
    void add_questions(ActionEvent event) {
        if (selectedQuiz == null) {
            lblstatus.setText("Select a quiz to add questions.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addquestions.fxml"));
            Parent root = loader.load();

            Addquestions addQuestionsController = loader.getController();
            addQuestionsController.setQuizName(selectedQuiz.getNom());

            Stage popupStage = new Stage();
            popupStage.setTitle("Ajouter des Questions");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait();

            loadQuiz();
        } catch (IOException e) {
            lblstatus.setText("Something went wrong while opening the add questions window.");
            e.printStackTrace();
        }
    }

    private void filterRessources(String searchField) {
        ObservableList<String> filteredRessources = FXCollections.observableArrayList();
        if (searchField == null || searchField.trim().isEmpty()) {
            filteredRessources.addAll(quizItems);
        } else {
            for (String titre : quizItems) {
                if (titre.toLowerCase().contains(searchField.toLowerCase())) {
                    filteredRessources.add(titre);
                }
            }
        }
        listViewQuiz.setItems(filteredRessources);
    }
}
