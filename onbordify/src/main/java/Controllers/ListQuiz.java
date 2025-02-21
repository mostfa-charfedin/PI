package Controllers;

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

    @FXML
    private void initialize() {
        quizService = new QuizService(); // تهيئة خدمة الـ Quiz
        loadQuiz();

        // تحديث `selectedQuiz` عند النقر على عنصر من القائمة
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
    }

    // تحميل الـ Quizzes وعرضها في ListView
    void loadQuiz() {
        try {
            List<Quiz> quizzes = quizService.getAll();
            ObservableList<String> quizItems = FXCollections.observableArrayList();

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
            popupStage.showAndWait(); // انتظار إغلاق النافذة

            loadQuiz(); // إعادة تحميل قائمة الاختبارات بعد الإنشاء

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

            // Get the controller and pass the selected quiz
            EditQuiz editController = loader.getController();
            editController.setQuiz(selectedQuiz);
            editController.setListQuizController(this);

            // Open the popup window
            Stage popupStage = new Stage();
            popupStage.setTitle("Edit Quiz");
            popupStage.setScene(new Scene(root));
            popupStage.showAndWait(); // Wait until the window is closed

            loadQuiz(); // Refresh the list after editing

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

    }








}
