package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Quiz;
import Services.QuizService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class QuizEmployee {

    @FXML
    private Label lblstatus;

    @FXML
    private ListView<String> listViewQuiz;

    @FXML
    private Button pass_quiz;

    @FXML
    private TextField search_field;

    @FXML
    private Label title_label;

    private Quiz selectedQuiz;
    private final QuizService quizService = new QuizService(); // Assuming QuizService to interact with the database

    private ObservableList<String> quizTitles;

    @FXML
    public void initialize() {
        quizTitles = FXCollections.observableArrayList();

        // Listen for text changes in the search field to trigger filtering
        search_field.textProperty().addListener((observable, oldValue, newValue) -> filterRessources(newValue));

        listViewQuiz.setOnMouseClicked(event -> {
            int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedQuiz = quizService.getAll().get(selectedIndex);
                    // Double-click to open quiz details or pass quiz
                    if (event.getClickCount() == 2) {
                        openQuizPopup(selectedQuiz);  // Directly open quiz popup on double-click
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        loadQuizzes();
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAll(); // Fetch quizzes from the service
            ObservableList<String> titles = FXCollections.observableArrayList();
            for (Quiz quiz : quizzes) {
                titles.add(quiz.getNom());
            }
            quizTitles = titles; // Assign quiz titles to the observable list
            listViewQuiz.setItems(titles);
            listViewQuiz.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (Exception e) {
            showAlert("Error", "Failed to load quizzes.");
            e.printStackTrace();
        }
    }

    private void openQuizPopup(Quiz selectedQuiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/QuizPopup.fxml"));
            Parent root = loader.load();

            QuizPopup controller = loader.getController();
            controller.setQuiz(selectedQuiz); // Pass the selected quiz to the QuizPopup

            Stage stage = new Stage();
            stage.setTitle("Quiz: " + selectedQuiz.getNom());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to open the Quiz Details window.");
            e.printStackTrace();
        }
    }

    @FXML
    void pass_quiz(ActionEvent event) throws SQLException {
        // Get the selected quiz
        selectedQuiz = getSelectedQuiz();

        if (selectedQuiz != null) {
            openQuizPopup(selectedQuiz); // Open the quiz popup directly here
        } else {
            showAlert("No Selection", "Please select a quiz first.");
        }
    }

    private Quiz getSelectedQuiz() throws SQLException {
        int selectedIndex = listViewQuiz.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            return quizService.getAll().get(selectedIndex);
        }
        return null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void cancel_button(ActionEvent actionEvent) {
        Stage stage = (Stage) title_label.getScene().getWindow();
        stage.close();
    }

    private void filterRessources(String searchText) {
        ObservableList<String> filteredQuizzes;

        if (searchText == null || searchText.trim().isEmpty()) {
            // If the search field is empty, show all quizzes, sorted alphabetically
            filteredQuizzes = FXCollections.observableArrayList(
                    quizTitles.stream()
                            .sorted(String::compareToIgnoreCase)
                            .toList()
            );
        } else {
            // Filter the quizzes based on the search text
            filteredQuizzes = FXCollections.observableArrayList(
                    quizTitles.stream()
                            .filter(quiz -> quiz.toLowerCase().contains(searchText.toLowerCase())) // Case-insensitive filter
                            .sorted(String::compareToIgnoreCase) // Sort the filtered results
                            .toList()
            );
        }

        // Update the ListView with the filtered and sorted quizzes
        listViewQuiz.setItems(filteredQuizzes);
    }
}