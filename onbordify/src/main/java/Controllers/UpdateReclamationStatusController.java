package controller;

import Models.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import service.ReclamationService;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UpdateReclamationStatusController {

    @FXML
    private TextField reclamationIdField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private ReclamationService reclamationService;
    private Reclamation reclamation;

    // This method is called when the window is opened
    public void initialize() {
        reclamationService = new ReclamationService();

        // Add statuses to ComboBox
        statusComboBox.getItems().addAll("Pending", "Resolved", "rejected");

        // You can modify the reclamation's ID and status from the previous window
        if (reclamation != null) {
            reclamationIdField.setText(String.valueOf(reclamation.getIdReclamation()));
        }
    }

    // Called to set the reclamation object to be edited
    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    // Method to save the updated status
    @FXML
    private void saveStatus(ActionEvent event) {
        if (reclamation != null && statusComboBox.getValue() != null) {
            try {
                boolean newStatus = statusComboBox.getValue().equals("Resolved");
                reclamationService.update(reclamation);

                // Show success message (you can use an alert or other UI components)
                System.out.println("Status updated successfully!");

                // Close the current window
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Method to cancel the update
    @FXML
    private void cancelUpdate(ActionEvent event) {
        // Close the current window without saving
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
