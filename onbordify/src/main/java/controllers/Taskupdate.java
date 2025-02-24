package controllers;

import Models.Tache;
import Services.TacheService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Taskupdate implements Initializable {

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
    private Tache taskToUpdate;
    private Tacheview tacheviewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tacheService = new TacheService();
        cmbSort.setItems(FXCollections.observableArrayList("Nom", "Role"));
        loadUsers();
        searchUser.setOnKeyReleased(this::handleSearch);
    }

    private void loadUsers() {
        try {
            List<String> users = tacheService.getAllUserNamesWithRoles();
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

    public void setTaskData(Tache task, Tacheview tacheview) {
        if (task == null) {
            lblStatus.setText("Error: No task selected.");
            return;
        }
        this.taskToUpdate = task;

        txtTitle.setText(task.getTitre());
        txtDescription.setText(task.getDescription());

        // Select the correct user in the ListView
        String selectedUser = task.getNom() + " " + task.getPrenom();
        listUsers.getSelectionModel().select(selectedUser);
    }

    public void setTacheviewController(Tacheview controller) {
        this.tacheviewController = controller;
    }

    @FXML
    private void handleSaveTask() {
        if (taskToUpdate == null) {
            lblStatus.setText("No task selected for update.");
            return;
        }

        String newTitle = txtTitle.getText().trim();
        String newDescription = txtDescription.getText().trim();
        String selectedUser = listUsers.getSelectionModel().getSelectedItem();

        if (newTitle.isEmpty() || newDescription.isEmpty() || selectedUser == null || selectedUser.trim().isEmpty()) {
            lblStatus.setText("Please fill all fields and select a user.");
            return;
        }

        // Extracting nom and prenom (ignores role)
        String userPattern = "^(.+?)\\s+(.+?)\\s*\\(.*\\)$"; // Matches "John Doe (Admin)"
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(userPattern);
        java.util.regex.Matcher matcher = pattern.matcher(selectedUser.trim());

        String nom, prenom;
        if (matcher.matches()) {
            nom = matcher.group(1).trim(); // Get 'nom' part (first name)
            prenom = matcher.group(2).trim(); // Get 'prenom' part (last name)
        } else {
            // Fallback: user string without role (e.g., "John Doe")
            String[] nameParts = selectedUser.split("\\s+", 2); // Splits on the first space
            if (nameParts.length < 2) {
                lblStatus.setText("Invalid user format. Please select a valid user.");
                return;
            }
            nom = nameParts[0].trim(); // First part is 'nom'
            prenom = nameParts[1].trim(); // Second part is 'prenom'
        }

        try {
            taskToUpdate.setTitre(newTitle);
            taskToUpdate.setDescription(newDescription);
            taskToUpdate.setNom(nom);
            taskToUpdate.setPrenom(prenom);

            tacheService.update(taskToUpdate);
            lblStatus.setText("Task updated successfully!");

            // Refresh the task list in the Tacheview
            if (tacheviewController != null) {
                tacheviewController.refreshTasks();
            }

            // Close the window after successful update
            Stage stage = (Stage) btnSubmit.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            lblStatus.setText("Error updating task: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        ((Stage) btnCancel.getScene().getWindow()).close();

        if (tacheviewController != null) {
            tacheviewController.refreshTasks();
        }
    }
}
