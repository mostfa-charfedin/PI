package Controllers;

import Models.Reclamation;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import utils.UserSession;

public class UpdateReclamationStatusController {

    @FXML
    private TextField reclamationIdField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextArea responseField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private ReclamationService reclamationService;
    private Reclamation reclamation;
    private UserSession session = UserSession.getInstance();

    public void initialize() {
        reclamationService = new ReclamationService();

        // Add statuses to ComboBox
        ObservableList<String> statusList = FXCollections.observableArrayList("PENDING", "RESOLVED", "REJECTED");
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
                    if (item.equals("PENDING")) {
                        setTextFill(Color.ORANGE);
                    } else if (item.equals("RESOLVED")) {
                        setTextFill(Color.GREEN);
                    } else if (item.equals("REJECTED")) {
                        setTextFill(Color.RED);
                    }
                }
            }
        });

        // Apply the same color for the selected item in ComboBox
        statusComboBox.setButtonCell(statusComboBox.getCellFactory().call(null));

        // Set initial values if reclamation is provided
        if (reclamation != null) {
            reclamationIdField.setText(String.valueOf(reclamation.getId()));
            responseField.setText(reclamation.getResponse() != null ? reclamation.getResponse() : "");

            // Set current status in ComboBox
            if (reclamation.getStatus() == null) {
                statusComboBox.setValue("PENDING");
            } else if (reclamation.getStatus()) {
                statusComboBox.setValue("RESOLVED");
            } else {
                statusComboBox.setValue("REJECTED");
            }
        }
    }

    public void setReclamation(Reclamation reclamation) {
        this.reclamation = reclamation;
    }

    @FXML
    private void saveStatus(ActionEvent event) {
        if (reclamation != null && statusComboBox.getValue() != null && !responseField.getText().isEmpty()) {
            try {
                String selectedStatus = statusComboBox.getValue();
                Boolean statusValue = null;

                switch (selectedStatus) {
                    case "RESOLVED":
                        statusValue = true;
                        break;
                    case "REJECTED":
                        statusValue = false;
                        break;
                    case "PENDING":
                        statusValue = null;
                        break;
                }

                // Update the reclamation status and response
                reclamationService.updateStatus(
                        reclamation.getId(),
                        statusValue,
                        responseField.getText(),
                        session.getUserId()
                );

                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Complaint status updated successfully!");
                alert.showAndWait();

                // Close the window
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update complaint status: " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a status and provide a response");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancelUpdate(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}