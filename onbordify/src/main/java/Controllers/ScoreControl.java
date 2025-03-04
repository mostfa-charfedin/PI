package Controllers;

import Models.Score;
import Services.QuizService;
import Services.ScoreService;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreControl {
    ScoreService scoreService = new ScoreService();
    UserService userService = new UserService();
    QuizService quizService = new QuizService();
    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> listview;

    @FXML
    private Label messagelist;

    @FXML
    private TextField rechercheFild;
    @FXML
    private ComboBox<String> quizFilter;

    private Score selectedUser ;
    private ObservableList<String> userList;
    private FilteredList<String> filteredList;

    @FXML
    public void initialize() {
        // Charger les noms de quiz dans le ComboBox
        loadQuizNames();

        // Charger la liste des scores
        loadScors();

        // Appliquer le filtre au changement de sélection
        quizFilter.setOnAction(event -> filterByQuiz());

        rechercheFild.textProperty().addListener((observable, oldValue, newValue) -> {
            chercherUser();
        });

        listview.setOnMouseClicked(event -> {
            int selectedIndex = listview.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    this.selectedUser = scoreService.getAll().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }


    private void loadQuizNames() {
        try {
            List<String> quizNames = quizService.getAll().stream()
                    .map(quiz -> quiz.getNom())
                    .collect(Collectors.toList());

            ObservableList<String> quizList = FXCollections.observableArrayList(quizNames);
            quizList.add(0, "Tous"); // Option pour afficher tous les scores
            quizFilter.setItems(quizList);
            quizFilter.setValue("Tous"); // Valeur par défaut
        } catch (Exception e) {
            messagelist.setText("Erreur de chargement des quiz : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadScors() {
        try {
            List<Score> scores = scoreService.getAll();

            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            // Mettre à jour scoreList avec les scores formatés
            userList.setAll(scores.stream()
                    .map(score -> {
                        try {
                            return " | Nom user: " + userService.findUserById(String.valueOf(score.getIdUser())).getNom() +
                                    " | Quiz : " + quizService.getById(score.getIdQuiz()).getNom() +
                                    " | Score : " + score.getScore() + " %";
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList()));

            if (filteredList == null) {
                filteredList = new FilteredList<>(userList, p -> true);
                listview.setItems(filteredList);
            } else {
                filteredList.setPredicate(p -> true); // Réinitialiser le filtre
            }

        } catch (Exception e) {
            messagelist.setText("Erreur de chargement des scores : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void filterByQuiz() {
        String selectedQuiz = quizFilter.getValue();

        if (filteredList != null) {
            filteredList.setPredicate(item -> {
                if (selectedQuiz == null || selectedQuiz.equals("Tous")) {
                    return true; // Afficher tous les scores
                }
                return item.contains("Quiz : " + selectedQuiz);
            });
        }
    }


    @FXML
    void chercherUser() {
        // Récupérer le texte saisi dans le champ de recherche
        String searchText = rechercheFild.getText().toLowerCase();

        // Appliquer le filtre sur la liste
        filteredList.setPredicate(scoreString -> {
            if (searchText == null || searchText.isEmpty()) {
                return true;
            }
            return scoreString.toLowerCase().contains(searchText);
        });
    }

    @FXML
    void deleteScore(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Please Select a user");
            return;
        }
        try {
            scoreService.delete(selectedUser.getIdQuiz(),selectedUser.getIdUser());
            messagelist.setText("Score deleted.");
            loadScors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void goToGestion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainPage.fxml"));
        Parent root = loader.load();
        messagelist.getScene().setRoot(root);
    }


}
