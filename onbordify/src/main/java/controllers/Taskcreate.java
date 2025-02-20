package controllers;

import Models.Tache;
import Services.TacheService;
import Services.ProjetService;  // Assuming you have a service to fetch projects
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Projet;  // Assuming your project model

import java.util.List;

public class Taskcreate {

    @FXML
    private TextField txtTitle, txtNom, txtPrenom;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnSubmit, btnCancel;

    @FXML
    private ComboBox<Projet> cmbProjects;

    private final TacheService tacheService = new TacheService();
    private final ProjetService ProjetService = new ProjetService();  // Assuming you have a project service
    private Tacheview tacheViewController; // Reference to refresh tasks after adding

    /**
     * Sets the task view controller reference for refreshing tasks after creation.
     */
    public void setTacheViewController(Tacheview tacheViewController) {
        this.tacheViewController = tacheViewController;
    }

    /**
     * Initializes the ComboBox with the list of existing projects.
     */
    @FXML
    public void initialize() throws Exception {
        // Fetch all projects and populate ComboBox
        List<Projet> projects = ProjetService.getAll();  // Get all projects
        cmbProjects.getItems().addAll(projects);  // Add projects to ComboBox

        // You can customize the ComboBox to show the project name instead of the default `toString()`
        cmbProjects.setCellFactory(listView -> new ListCell<Projet>() {
            @Override
            protected void updateItem(Projet project, boolean empty) {
                super.updateItem(project, empty);
                if (project != null) {
                    setText(project.getTitre()); // Assuming Project has getProjectName method
                } else {
                    setText(null);
                }
            }
        });
    }

    /**
     * Handles the task creation process.
     */
    @FXML
    private void handleCreateTask() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        Projet selectedProject = cmbProjects.getSelectionModel().getSelectedItem();

        // Validate input fields
        if (title.isEmpty() || description.isEmpty() || nom.isEmpty() || prenom.isEmpty() || selectedProject == null) {
            lblStatus.setText("All fields are required!");
            return;
        }

        // Create a new task object with the selected project
        Tache newTask = new Tache(title, description, selectedProject.getIdProjet(), 0, nom, prenom, "");

        try {
            // Call the service to create the task
            tacheService.create(newTask);
            lblStatus.setText("Task created successfully!");

            // Refresh task list in the main view
            tacheViewController.refreshTasks();

            // Close the current window after success
            closeWindow();
        } catch (Exception e) {
            // Handle any errors during task creation
            lblStatus.setText("Error creating task: " + e.getMessage());
        }
    }

    /**
     * Cancels the task creation process and closes the window.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Closes the current task creation window.
     */
    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
