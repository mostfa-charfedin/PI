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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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

    private Score selectedUser ;
    private ObservableList<String> userList;
    private FilteredList<String> filteredList;

    @FXML
    public void initialize() {
        loadScors();

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


    public void loadScors() {
        try {
            List<Score> scors = scoreService.getAll();

            // Vérifier si userList est null avant de l'utiliser
            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            // Mettre à jour userList
            userList.setAll(scors.stream()
                    .map(score -> {
                        try {
                            return " | Nom user:  " +userService.findUserById(String.valueOf(score.getIdUser())).getNom() +" | Id Quiz : " +
                                    quizService.getById(score.getIdQuiz()).getNom()  +" | Score : " + score.getScore() +" %" ;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList()));

            // Vérifier si filteredList est null avant de l'utiliser
            if (filteredList == null) {
                filteredList = new FilteredList<>(userList, p -> true);
                listview.setItems(filteredList); // Lier la liste filtrée à la ListView une seule fois
            }

            // Appliquer les filtres déjà présents
            chercherUser();

        } catch (Exception e) {
            messagelist.setText("Error loading users: " + e.getMessage());
            e.printStackTrace();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GestionUser.fxml"));
        Parent root = loader.load();
        messagelist.getScene().setRoot(root);
    }


}
