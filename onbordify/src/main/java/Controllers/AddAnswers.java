package Controllers;

import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modles.Question;
import modles.Reponse;

import java.sql.SQLException;

public class AddAnswers {

    @FXML
    private TextField answer1, answer2, answer3, answer4;

    @FXML
    private ToggleButton toggle1, toggle2, toggle3, toggle4;

    @FXML
    private Button add_button, cancel_button;

    @FXML
    private Label lblstatus;

    private Question selectedQuestion;  // Store the selected question

    private ReponseService reponseService = new ReponseService();  // Instance of ReponseService to interact with the DB

    @FXML
    private void add_button(ActionEvent event) {
        String answer1Text = answer1.getText();
        String answer2Text = answer2.getText();
        String answer3Text = answer3.getText();
        String answer4Text = answer4.getText();

        // Check if all answers are filled in
        if (answer1Text.isEmpty() || answer2Text.isEmpty() || answer3Text.isEmpty() || answer4Text.isEmpty()) {
            lblstatus.setText("Please fill in all the answers.");
            return;
        }

        // Process the toggle states (to track which answer is correct)
        String statut1 = toggle1.isSelected() ? "correct" : "incorrect";
        String statut2 = toggle2.isSelected() ? "correct" : "incorrect";
        String statut3 = toggle3.isSelected() ? "correct" : "incorrect";
        String statut4 = toggle4.isSelected() ? "correct" : "incorrect";

        // Assume 'questionId' is the ID of the question you're adding answers for.
        // You need to get this ID somehow, for example from the UI or from the controller.
        int questionId = 1;  // This should be dynamically assigned based on your question context.

        try {
            // Create Reponse objects for each answer
            Reponse reponse1 = new Reponse(answer1Text, statut1, questionId);
            Reponse reponse2 = new Reponse(answer2Text, statut2, questionId);
            Reponse reponse3 = new Reponse(answer3Text, statut3, questionId);
            Reponse reponse4 = new Reponse(answer4Text, statut4, questionId);

            // Create an instance of your ReponseService to insert data
            ReponseService reponseService = new ReponseService();  // Assuming you have a no-argument constructor for ReponseService
            reponseService.create(reponse1);
            reponseService.create(reponse2);
            reponseService.create(reponse3);
            reponseService.create(reponse4);

            // Update the label to show success message
            lblstatus.setText("Answers Added Successfully.");
        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            lblstatus.setText("Error adding answers to the database.");
        }}



    // Helper method to clear input fields
    private void clearInputFields() {
        answer1.clear();
        answer2.clear();
        answer3.clear();
        answer4.clear();

        toggle1.setSelected(false);
        toggle2.setSelected(false);
        toggle3.setSelected(false);
        toggle4.setSelected(false);

        // Reset the status label after clearing
        lblstatus.setText("");
    }

    @FXML
    private void cancel_button(ActionEvent event) {
        Stage stage = (Stage) cancel_button.getScene().getWindow();
        stage.close();
    }

    // Set the selected question for the controller
    public void setQuestion(Question selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    @FXML
    void clear(ActionEvent event) {
        clearInputFields();
        lblstatus.setText("❌ Input cleared.");
    }

    @FXML
    void correct(ActionEvent event) {
        // Get the clicked toggle button
        ToggleButton clickedButton = (ToggleButton) event.getSource();

        // Update the status for the selected answer
        updateAnswerStatus(clickedButton);
    }

    // Helper method to update the status message for each answer button
    private void updateAnswerStatus(ToggleButton clickedButton) {
        // Check which toggle button was clicked and update the label accordingly
        if (clickedButton == toggle1) {
            lblstatus.setText("Answer 1 is " + (clickedButton.isSelected() ? "correct." : "incorrect."));
        } else if (clickedButton == toggle2) {
            lblstatus.setText("Answer 2 is " + (clickedButton.isSelected() ? "correct." : "incorrect."));
        } else if (clickedButton == toggle3) {
            lblstatus.setText("Answer 3 is " + (clickedButton.isSelected() ? "correct." : "incorrect."));
        } else if (clickedButton == toggle4) {
            lblstatus.setText("Answer 4 is " + (clickedButton.isSelected() ? "correct." : "incorrect."));
        }
    }
}
