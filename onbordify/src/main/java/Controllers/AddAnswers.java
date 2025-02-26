package Controllers;

import Services.ReponseService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Reponse;

import java.sql.SQLException;
import java.util.List;

public class AddAnswers {

    @FXML
    private TextField answer1, answer2, answer3, answer4;

    @FXML
    private ToggleButton toggle1, toggle2, toggle3, toggle4;

    @FXML
    private Button add_button, clear, cancel_button;

    @FXML
    private Label lblstatus;

    private ReponseService reponseService;
    private int questionId;

    @FXML
    private void initialize() {
        reponseService = new ReponseService();
        add_button.setDisable(true); // Désactiver le bouton au démarrage

        // Ajouter des listeners pour activer le bouton add
        List.of(answer1, answer2, answer3, answer4).forEach(field ->
                field.textProperty().addListener((observable, oldValue, newValue) -> validateFields()));
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @FXML
    private void addAnswers() {
        boolean hasCorrect = false;

        if (answer1.getText().trim().isEmpty() && answer2.getText().trim().isEmpty() &&
                answer3.getText().trim().isEmpty() && answer4.getText().trim().isEmpty()) {
            lblstatus.setText("Please enter at least one answer.");
            return;
        }

        try {
            if (!answer1.getText().trim().isEmpty()) {
                boolean isCorrect = toggle1.isSelected();
                hasCorrect |= isCorrect;
                reponseService.create(new Reponse(answer1.getText(), isCorrect ? "correct" : "incorrect", questionId));
            }

            if (!answer2.getText().trim().isEmpty()) {
                boolean isCorrect = toggle2.isSelected();
                hasCorrect |= isCorrect;
                reponseService.create(new Reponse(answer2.getText(), isCorrect ? "correct" : "incorrect", questionId));
            }

            if (!answer3.getText().trim().isEmpty()) {
                boolean isCorrect = toggle3.isSelected();
                hasCorrect |= isCorrect;
                reponseService.create(new Reponse(answer3.getText(), isCorrect ? "correct" : "incorrect", questionId));
            }

            if (!answer4.getText().trim().isEmpty()) {
                boolean isCorrect = toggle4.isSelected();
                hasCorrect |= isCorrect;
                reponseService.create(new Reponse(answer4.getText(), isCorrect ? "correct" : "incorrect", questionId));
            }

            if (!hasCorrect) {
                lblstatus.setText("Please select at least one correct answer.");
                return;
            }

            lblstatus.setText("Answers added successfully!");
            clearFields();
        } catch (SQLException e) {
            lblstatus.setText("Error adding answers.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clear() {
        clearFields();
        lblstatus.setText("");
    }

    private void clearFields() {
        List.of(answer1, answer2, answer3, answer4).forEach(TextField::clear);
        List.of(toggle1, toggle2, toggle3, toggle4).forEach(toggle -> toggle.setSelected(false));
        add_button.setDisable(true);
    }

    @FXML
    private void cancel() {
        ((Stage) cancel_button.getScene().getWindow()).close();
    }

    @FXML
    private void enforceSingleCorrectAnswer() {
        toggle1.setOnAction(e -> updateToggles(toggle1));
        toggle2.setOnAction(e -> updateToggles(toggle2));
        toggle3.setOnAction(e -> updateToggles(toggle3));
        toggle4.setOnAction(e -> updateToggles(toggle4));
    }

    private void updateToggles(ToggleButton selectedToggle) {
        List.of(toggle1, toggle2, toggle3, toggle4).forEach(toggle -> toggle.setSelected(false));
        selectedToggle.setSelected(true);
    }

    @FXML
    private void validateFields() {
        boolean isEmpty = List.of(answer1, answer2, answer3, answer4).stream().allMatch(field -> field.getText().trim().isEmpty());
        add_button.setDisable(isEmpty);
    }
}
