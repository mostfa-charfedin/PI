package controllers;

import Models.Tache;
import Services.TacheService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class Taskcreate {

    @FXML
    private TextField txtTitle, searchUser;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cmbSort;

    @FXML
    private ListView<String> listUsers;

    @FXML
    private Button btnSubmit, btnCancel;

    @FXML
    private Label lblStatus;

    private TacheService tacheService;
    private ObservableList<String> userList;

    private int projectId; // Stores the project ID

    private Tacheview tacheviewController; // Reference to Tacheview controller

    public void initialize() {
        tacheService = new TacheService();
        cmbSort.setItems(FXCollections.observableArrayList("Nom", "Role"));
        loadUsers();
        searchUser.setOnKeyReleased(this::handleSearch);
    }

    private void loadUsers() {
        try {
            List<String> users = tacheService.getAllUserNames();
            userList = FXCollections.observableArrayList(users);
            listUsers.setItems(userList);
        } catch (Exception e) {
            lblStatus.setText("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchUser.getText().toLowerCase();
        List<String> filteredUsers = userList.stream()
                .filter(user -> user.toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        listUsers.setItems(FXCollections.observableArrayList(filteredUsers));
    }

    @FXML
    private void handleCreateTask(ActionEvent event) {
        try {
            String title = txtTitle.getText().trim();
            String description = txtDescription.getText().trim();
            String selectedUser = listUsers.getSelectionModel().getSelectedItem();

            if (title.isEmpty() || description.isEmpty() || selectedUser == null || selectedUser.trim().isEmpty()) {
                lblStatus.setText("Please fill all fields and select a user.");
                return;
            }

            String[] userParts = selectedUser.trim().split("\\s+", 2);
            if (userParts.length < 2) {
                lblStatus.setText("Please select a user with both first and last name.");
                return;
            }

            String nom = userParts[0].trim();
            String prenom = userParts[1].trim();

            Tache task = new Tache(title, description, projectId, nom, prenom);
            tacheService.create(task);

            lblStatus.setText("Task created successfully!");

            // Reset fields
            txtTitle.clear();
            txtDescription.clear();
            searchUser.clear();
            listUsers.getSelectionModel().clearSelection();
            listUsers.setItems(userList);

            // Refresh task list in Tacheview
            if (tacheviewController != null) {
                tacheviewController.refreshTasks();
            }
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Close the popup
        ((Stage) btnCancel.getScene().getWindow()).close();

        // Refresh task list in Tacheview
        if (tacheviewController != null) {
            tacheviewController.refreshTasks();
        }
    }

    // Setter to pass project ID from Tacheview
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    // Setter to receive the Tacheview controller reference
    public void setTacheviewController(Tacheview tacheviewController) {
        this.tacheviewController = tacheviewController;
    }
}
