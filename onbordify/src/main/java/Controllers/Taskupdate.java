package Controllers;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Taskupdate implements Initializable {

    @FXML
    private TextField txtTitle, txtDate, searchUser;

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
        this.tacheviewController = tacheview;

        txtTitle.setText(task.getTitre());
        txtDescription.setText(task.getDescription());
        txtDate.setText(String.valueOf(task.getDate())); // Ensure date is set

        // Select the correct user in the ListView
        String selectedUser = task.getNom() + " " + task.getPrenom();
        listUsers.getSelectionModel().select(selectedUser);
    }

    @FXML
    private void handleUpdateTask() {
        if (taskToUpdate == null) {
            lblStatus.setText("No task selected for update.");
            return;
        }

        String newTitle = txtTitle.getText().trim();
        String newDescription = txtDescription.getText().trim();
        String dateText = txtDate.getText().trim();
        String selectedUser = listUsers.getSelectionModel().getSelectedItem();

        if (newTitle.isEmpty() || newDescription.isEmpty() || dateText.isEmpty() || selectedUser == null || selectedUser.trim().isEmpty()) {
            lblStatus.setText("Please fill all fields and select a user.");
            return;
        }

        // Validate that "date" is a valid integer
        int newDate;
        try {
            newDate = Integer.parseInt(dateText);
            if (newDate < 0) {
                lblStatus.setText("Date must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            lblStatus.setText("Invalid date. Please enter a valid number.");
            return;
        }

        // Extract nom and prenom
        Pattern pattern = Pattern.compile("^(.+?)\\s+(.+?)\\s*\\(.*\\)$");
        Matcher matcher = pattern.matcher(selectedUser.trim());

        String nom, prenom;
        if (matcher.matches()) {
            nom = matcher.group(1).trim();
            prenom = matcher.group(2).trim();
        } else {
            String[] nameParts = selectedUser.split("\\s+", 2);
            if (nameParts.length < 2) {
                lblStatus.setText("Invalid user format. Please select a valid user.");
                return;
            }
            nom = nameParts[0].trim();
            prenom = nameParts[1].trim();
        }

        try {
            taskToUpdate.setTitre(newTitle);
            taskToUpdate.setDescription(newDescription);
            taskToUpdate.setNom(nom);
            taskToUpdate.setPrenom(prenom);
            taskToUpdate.setDate(newDate); // Set the date as an integer

            tacheService.update(taskToUpdate);
            lblStatus.setText("Task updated successfully!");

            if (tacheviewController != null) {
                tacheviewController.refreshTasks();
            }

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
