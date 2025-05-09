package Controllers;

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
    private TextField txtTitle, searchUser, txtWeeks;

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
        cmbSort.setOnAction(event -> handleSort());
        loadUsers();
        searchUser.setOnKeyReleased(this::handleSearch);
    }
    @FXML
    private void handleSort() {
        String selectedSort = cmbSort.getSelectionModel().getSelectedItem();

        if (selectedSort == null) return;

        List<String> sortedUsers = userList.stream().sorted((u1, u2) -> {
            String[] parts1 = u1.split(" ");
            String[] parts2 = u2.split(" ");

            if (selectedSort.equals("Nom")) {
                return parts1[0].compareTo(parts2[0]); // Sort by last name
            } else if (selectedSort.equals("Role")) {
                return parts1[parts1.length - 1].compareTo(parts2[parts2.length - 1]); // Sort by role
            }
            return 0;
        }).collect(Collectors.toList());

        listUsers.setItems(FXCollections.observableArrayList(sortedUsers));
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

    @FXML
    private void handleCreateTask(ActionEvent event) {
        try {
            // Ensure projectId is set before proceeding
            if (projectId == 0) {
                lblStatus.setText("Error: Project ID is not set.");
                System.err.println("Error: Project ID is not set.");
                return;
            }

            String title = txtTitle.getText().trim();
            String description = txtDescription.getText().trim();
            String selectedUser = listUsers.getSelectionModel().getSelectedItem();
            String weeksStr = txtWeeks.getText().trim();

            if (title.isEmpty() || description.isEmpty() || selectedUser == null || selectedUser.trim().isEmpty() || weeksStr.isEmpty()) {
                lblStatus.setText("Please fill all fields and select a user.");
                return;
            }

            int weeks;
            try {
                weeks = Integer.parseInt(weeksStr);
            } catch (NumberFormatException e) {
                lblStatus.setText("Invalid weeks value. Please enter a number.");
                return;
            }

            // Extracting nom and prenom correctly
            String userPattern = "^(.+?)\\s+(.+?)\\s*\\(.*\\)$"; // Matches "John Doe (Admin)"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(userPattern);
            java.util.regex.Matcher matcher = pattern.matcher(selectedUser.trim());

            String nom, prenom;
            if (matcher.matches()) {
                nom = matcher.group(1).trim();
                prenom = matcher.group(2).trim();
            } else {
                lblStatus.setText("Invalid user format. Please select a valid user.");
                return;
            }

            System.out.println("Creating task for Project ID: " + projectId); // Debugging output

            // Create and store the task
            Tache task = new Tache(title, description, projectId, nom, prenom, weeks);
            tacheService.create(task);

            lblStatus.setText("Task created successfully!");

            // Reset fields
            txtTitle.clear();
            txtDescription.clear();
            searchUser.clear();
            txtWeeks.clear();
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

    public void setProjectId(int projectId) {
        this.projectId = projectId;
        System.out.println("Project ID set to: " + this.projectId); // Debugging
    }


    // Setter to receive the Tacheview controller reference
    public void setTacheviewController(Tacheview tacheviewController) {
        this.tacheviewController = tacheviewController;
    }
}
