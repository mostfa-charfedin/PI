package controllers;

import Services.TacheService;
import Models.Tache;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class Tacheview {

    @FXML
    private ListView<String> taskListView;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnCreateTask, btnEditTask, btnDeleteTask;

    private TacheService tacheService;
    private Tache selectedTask;
    private int projectId; // Stores the project ID

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

        // Handle double-click to open task details
        taskListView.setOnMouseClicked(this::handleTaskDoubleClick);
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

            // Get Taskcreate controller and set project ID
            Taskcreate taskCreateController = loader.getController();
            taskCreateController.setProjectId(this.projectId); // Pass the project ID ✅
            taskCreateController.setTacheviewController(this); // Pass Tacheview reference ✅

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Create Task");
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening Create Task window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditTask() {
        if (selectedTask == null) {
            lblStatus.setText("Select a task to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskupdate.fxml"));
            Parent root = loader.load();

            Taskupdate taskUpdateController = loader.getController();
            taskUpdateController.setTaskData(selectedTask, this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Edit Task");
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening Task Edit window: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTask() {
        if (selectedTask == null) {
            lblStatus.setText("Select a task to delete.");
            return;
        }
        try {
            tacheService.delete(selectedTask.getIdTache());
            refreshTasks();
            lblStatus.setText("Task deleted.");
        } catch (Exception e) {
            lblStatus.setText("Error deleting task: " + e.getMessage());
        }
    }

    public void refreshTasks() {
        loadTasks();
    }

    // Handle double-click to open task details
    @FXML
    private void handleTaskDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) { // Detect double-click
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedTask = tacheService.getAllByProject(projectId).get(selectedIndex);
                    openTaskDetailsPage(selectedTask);
                } catch (Exception e) {
                    lblStatus.setText("Error opening task details: " + e.getMessage());
                }
            }
        }
    }

    // Open task details page
    private void openTaskDetailsPage(Tache task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskdetails.fxml"));
            Parent root = loader.load();

            // Pass task data to the details controller
            TaskDetails controller = loader.getController();
            controller.setTaskData(task);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Task Details");
            stage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening task details window.");
        }
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
}
