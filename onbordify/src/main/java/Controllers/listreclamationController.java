package Controllers;
import Models.Reclamation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import Services.ReclamationService;

import java.sql.SQLException;
import java.util.List;

public class listreclamationController {

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
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.getAll();
        reclamationList.clear();
        for (Reclamation rec : reclamations) {
            reclamationList.add("ID: " + rec.getIdReclamation() + " | " + rec.getCommentaire() + " | Status: " + rec.getEtat());
        }
        reclamationListView.setItems(reclamationList);
    }

    @FXML
    private void handleUpdateStatus() {
        int selectedIndex = reclamationListView.getSelectionModel().getSelectedIndex();
        String selectedStatus = statusComboBox.getValue();

        if (selectedIndex >= 0 && selectedStatus != null) {
            String selectedItem = reclamationListView.getSelectionModel().getSelectedItem();
            int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

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