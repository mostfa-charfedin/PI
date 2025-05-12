package Controllers;

import Models.Reclamation;
import Services.ReclamationService;
import Services.SpamService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import utils.UserSession;

public class ReclamationController {

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea contentField;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<String> complaintsList = FXCollections.observableArrayList();
    private SpamService spamService = new SpamService();
    private UserSession session = UserSession.getInstance();
    private int selectedComplaintId = -1;

    @FXML
    public void initialize() {
        loadComplaints();
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                handleComplaintSelection();
            }
        });
    }

    private void handleComplaintSelection() {
        String selectedItem = listView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            selectedComplaintId = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());
            Reclamation selectedComplaint = reclamationService.getById(selectedComplaintId);
            if (selectedComplaint != null) {
                subjectField.setText(selectedComplaint.getSubject());
                contentField.setText(selectedComplaint.getContent());
            }
        }
    }

    private void loadComplaints() {
        complaintsList.clear();
        reclamationService.getAll().forEach(complaint -> {
            String status;
            if (complaint.getStatus() == null) {
                status = "Pending";
            } else if (complaint.getStatus()) {
                status = "Resolved";
            } else {
                status = "Rejected";
            }

            complaintsList.add(String.format("ID: %d | Subject: %s | Status: %s | Date: %s",
                    complaint.getId(),
                    complaint.getSubject(),
                    status,
                    complaint.getCreatedAt().toString()));
        });
        listView.setItems(complaintsList);
    }

    @FXML
    private void ajouterReclamation() {
        String subject = subjectField.getText();
        String content = contentField.getText();

        if (subject.isEmpty() || content.isEmpty()) {
            showAlert("Error", "Please enter both subject and content.");
            return;
        }

        if (!spamService.traiterReclamation(content)) {
            Reclamation newComplaint = new Reclamation();
            newComplaint.setUserId(session.getUserId());
            newComplaint.setSubject(subject);
            newComplaint.setContent(content);
            newComplaint.setCreatedAt(LocalDateTime.now());

            reclamationService.add(newComplaint);
            showAlert("Success", "Complaint added successfully!");
            loadComplaints();
            clearFields();
        }
    }

    @FXML
    private void updateReclamation() {
        if (selectedComplaintId == -1) {
            showAlert("Error", "Please select a complaint to update.");
            return;
        }

        String subject = subjectField.getText();
        String content = contentField.getText();

        if (subject.isEmpty() || content.isEmpty()) {
            showAlert("Error", "Please enter both subject and content.");
            return;
        }

        if (!spamService.traiterReclamation(content)) {
            Reclamation updatedComplaint = reclamationService.getById(selectedComplaintId);
            if (updatedComplaint != null) {
                updatedComplaint.setSubject(subject);
                updatedComplaint.setContent(content);
                reclamationService.update(updatedComplaint);
                showAlert("Success", "Complaint updated successfully!");
                loadComplaints();
                clearFields();
                selectedComplaintId = -1;
            }
        }
    }

    @FXML
    private void supprimerReclamation() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

            reclamationService.delete(id);
            showAlert("Success", "Complaint deleted successfully!");
            loadComplaints();
            clearFields();
            selectedComplaintId = -1;
        } else {
            showAlert("Error", "Please select a complaint to delete.");
        }
    }

    private void clearFields() {
        subjectField.clear();
        contentField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}