package controllers;

import Models.Tache;
import Services.TacheService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

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

    // Field to store the current project id
    private int projectId;

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
            System.out.println("Loaded Users: " + users);
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

            System.out.println("Selected User: " + selectedUser);

            if (title.isEmpty() || description.isEmpty() || selectedUser == null || selectedUser.trim().isEmpty()) {
                lblStatus.setText("Please fill all fields and select a user.");
                return;
            }

            // Split selected user into nom and prenom
            String[] userParts = selectedUser.trim().split("\\s+", 2);
            if (userParts.length < 2) {
                lblStatus.setText("Please select a user with both first and last name.");
                return;
            }

            String nom = userParts[0].trim();
            String prenom = userParts[1].trim();

            System.out.println("Extracted Nom: " + nom);
            System.out.println("Extracted Prenom: " + prenom);

            // Use the actual projectId set from the calling controller instead of 0
            Tache task = new Tache(title, description, projectId, nom, prenom);

            tacheService.create(task);
            lblStatus.setText("Task created successfully!");

            // Reset fields after creation
            txtTitle.clear();
            txtDescription.clear();
            searchUser.clear();
            listUsers.getSelectionModel().clearSelection();
            // Optionally, reset the list view to show the full user list
            listUsers.setItems(userList);
        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {

        // Reset the ListView to its original full list
        listUsers.setItems(userList);
    }

    // Setter to allow passing the project id from the calling controller
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
