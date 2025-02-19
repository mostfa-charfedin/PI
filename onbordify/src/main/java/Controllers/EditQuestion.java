package Controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class EditQuestion {
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
    void correct_button(ActionEvent event) {

    }

    @FXML
    void save_edit(ActionEvent event) {

    }
}
