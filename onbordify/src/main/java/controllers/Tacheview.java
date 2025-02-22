package controllers;

import Services.TacheService;
import Models.Tache;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Tacheview {

    @FXML
    private ListView<String> taskListView;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnCreateTask, btnDeleteTask;

    private TacheService tacheService;
    private Tache selectedTask;
    private int projectId; // Stores the selected project ID

    @FXML
    public void initialize() {
        tacheService = new TacheService();

        // Handle task selection
        taskListView.setOnMouseClicked(event -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedTask = tacheService.getAllByProject(projectId).get(selectedIndex);
                } catch (Exception e) {
                    lblStatus.setText("Error selecting task: " + e.getMessage());
                }
            }
        });
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
        loadTasks();
    }

    private void loadTasks() {
        taskListView.getItems().clear();
        try {
            List<Tache> tasks = tacheService.getAllByProject(projectId);
            for (Tache task : tasks) {
                taskListView.getItems().add(task.getTitre());
            }
        } catch (Exception e) {
            lblStatus.setText("Error loading tasks: " + e.getMessage());
        }
    }

    @FXML

    private void handleCreateTask() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskcreate.fxml"));
            Parent root = loader.load();

            // Get the Taskcreate controller and set the project id
            Taskcreate taskCreateController = loader.getController();
            taskCreateController.setProjectId(projectId);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Create Task");
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening Task Create window: " + e.getMessage());
        }
    }


    @FXML
    private void handleDeleteTask() throws Exception {
        if (selectedTask == null) {
            lblStatus.setText("Select a task to delete.");
            return;
        }
        tacheService.delete(selectedTask.getIdTache());
        loadTasks();
        lblStatus.setText("Task deleted.");
    }

    private void openPopup(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle(title);
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening window.");
        }
    }

    public void refreshTasks() {
        loadTasks();
    }

}
