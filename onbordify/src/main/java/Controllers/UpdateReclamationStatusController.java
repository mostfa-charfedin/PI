package Controllers;

import Models.Reclamation;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import Services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.control.ListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        ObservableList<String> statusList = FXCollections.observableArrayList("Pending", "Resolved", "Rejected");
        statusComboBox.setItems(statusList);

        // Customize ComboBox with colors for statuses
        statusComboBox.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(null);
                } else {
                    setText(item);
                    // Apply colors based on status
                    if (item.equals("Pending")) {
                        setTextFill(Color.ORANGE);  // Color orange for "Pending"
                    } else if (item.equals("Resolved")) {
                        setTextFill(Color.GREEN);  // Color green for "Resolved"
                    } else if (item.equals("Rejected")) {
                        setTextFill(Color.RED);  // Color red for "Rejected"
                    }
                }
            }
        });

        // Apply the same color for the selected item in ComboBox
        statusComboBox.setButtonCell(statusComboBox.getCellFactory().call(null));

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
                // Update the reclamation status based on ComboBox selection
                String newStatus = statusComboBox.getValue();
                reclamation.setEtat(newStatus);  // Set the new status to the reclamation

                // Call the service to update the reclamation in the database
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