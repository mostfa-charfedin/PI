package Controllers;

import Services.QuestionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modles.Question;
import Services.QuestionService;
import modles.Quiz;

import java.io.IOException;
import java.text.CollationElementIterator;
import java.time.LocalDate;

public class CreateQuiz {


    @FXML
    private Button add_button;

    @FXML
    private Label add_question;

    @FXML
    private TextField answer_filed;

    @FXML
    private Button cancel_button;

    @FXML
    private ToggleButton correct_button;

    @FXML
    private ImageView delete_question;

    @FXML
    private TextArea question_field;

    @FXML
    private DatePicker quiz_date;

    @FXML
    private TextField quiz_name;


    @FXML
    void correct_button(ActionEvent event) {

    }
    @FXML
    void add_button(ActionEvent event) {
        String questionText = question_field.getText(); // Le champ pour la question
        String quizName = quiz_name.getText().trim();
        LocalDate creationDate = quiz_date.getValue();
        String answerText = answer_filed.getText(); // Le champ pour la answer

        if (quizName.isEmpty()) {
            showAlert("Erreur", "Le nom du quiz est obligatoire.");
            return;
        }
        if (creationDate == null) {
            showAlert("Erreur", "Veuillez sélectionner une date de création.");
            return;
        }
        if (questionText.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer une question.");
            return;
        }
        if (answerText.isEmpty()) {
            showAlert("Erreur", "Veuillez ajouter au moins une réponse.");
            return;
        }


        // Création de l'objet Question
        QuestionService question = new QuestionService();
        Question q = new Question();

        try {

            // Appel à la méthode de service pour ajouter la question
            question.create(q);

            // Réinitialisation des champs après l'ajout
            resetFields();

            // Affichage de l'alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Question added successfully!");
            alert.showAndWait();

            // Optionnel : Fermeture de la fenêtre (si nécessaire)
            Stage stage = (Stage) add_button.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            // Gestion des erreurs
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }}
    private void resetFields() {
        // Réinitialiser les champs du formulaire de création de question
        question_field.clear();  // Effacer le champ de texte pour la question
        quiz_date.setValue(null);  // Réinitialiser le DatePicker pour la date de création
        quiz_name.clear();  // Effacer le champ du nom du quiz
        answer_filed.clear();  // Réinitialiser la liste des champs de réponses

    }

        @FXML
        void cancel_button (ActionEvent event){
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
        private void showAlert (String title, String message){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

    }
