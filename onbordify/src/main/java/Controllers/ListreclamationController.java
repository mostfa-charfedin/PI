package Controllers;

import Models.Reclamation;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;


import java.util.List;

public class ListreclamationController {

    @FXML
    private ListView<String> reclamationListView;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button updateStatusButton;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<String> reclamationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadReclamations();
        statusComboBox.setItems(FXCollections.observableArrayList("PENDING", "Rejected", "RESOLVED"));

        // Apply color to the status text
        reclamationListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Set the text for the item
                    setText(item);

                    // Check if the status is "Pending", "Resolved", or "Rejected" and apply color
                    if (item.contains("Pending")) {
                        setTextFill(Color.ORANGE);  // Orange color for "Pending"
                    } else if (item.contains("Resolved")) {
                        setTextFill(Color.GREEN);   // Green color for "Resolved"
                    } else if (item.contains("Rejected")) {
                        setTextFill(Color.RED);     // Red color for "Rejected"
                    } else {
                        setTextFill(Color.BLACK);   // Default color if no status is matched
                    }
                }
            }
        });
    }

    private void loadReclamations() {
        // Fetch the list of reclamations from the service
        List<Reclamation> reclamations = reclamationService.getAll();
        reclamationList.clear();

        for (Reclamation rec : reclamations) {
            // Format the item text with ID, Commentaire, and Status
            String reclamationItem = "ID: " + rec.getIdReclamation() + " | " + rec.getCommentaire() + " | Status: " + rec.getEtat();
            reclamationList.add(reclamationItem);
        }

        // Set the list of items to the ListView
        reclamationListView.setItems(reclamationList);
    }

    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        int selectedIndex = reclamationListView.getSelectionModel().getSelectedIndex();
        String selectedStatus = statusComboBox.getValue();

        if (selectedIndex >= 0 && selectedStatus != null) {
            String selectedItem = reclamationListView.getSelectionModel().getSelectedItem();
            int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

            // Update the status in the service
            reclamationService.updateStatus(id, selectedStatus);
            showAlert("Success", "Claim status updated successfully!");
            loadReclamations();
        } else {
            showAlert("Error", "Please select a claim and a new status.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}