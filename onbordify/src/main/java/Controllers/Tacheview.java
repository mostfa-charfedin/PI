package Controllers;

import Services.TacheService;
import Services.PDFGenerator;
import Models.Tache;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class Tacheview {

    @FXML
    private ListView<String> taskListView;

    @FXML
    private Label lblStatus, progressLabel;

    @FXML
    private Button btnCreateTask, btnEditTask, btnDeleteTask;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ProgressIndicator progressIndicator;

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
            int completedTasks = 0;

            for (Tache task : tasks) {
                // Construct a string containing the task title, username, and status
                String taskDisplay = "Title: " + task.getTitre()
                        + " | Attributed to: " + task.getNom()
                        + " | Status: " + task.getStatus();
                taskListView.getItems().add(taskDisplay);

                // Count completed tasks
                if (task.getStatus().equalsIgnoreCase("done")) {
                    completedTasks++;
                }
            }

            // Update Progress
            updateProgress(tasks.size(), completedTasks);

        } catch (Exception e) {
            lblStatus.setText("Error loading tasks: " + e.getMessage());
        }
    }

    private void updateProgress(int totalTasks, int completedTasks) {
        double progress = (totalTasks > 0) ? (double) completedTasks / totalTasks : 0;

        progressBar.setProgress(progress);
        progressIndicator.setProgress(progress);

        // Update percentage label
        int percentage = (int) (progress * 100);
        progressLabel.setText(percentage + "%");

        // Change label color based on progress
        if (progress == 1.0) {
            progressLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
        } else {
            progressLabel.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
        }
    }

    @FXML
    private void handleCreateTask() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskcreate.fxml"));
            Parent root = loader.load();

            // Get Taskcreate controller and set project ID
            Taskcreate taskCreateController = loader.getController();
            taskCreateController.setProjectId(this.projectId);
            taskCreateController.setTacheviewController(this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Create Task");
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening Create Task window: " + e.getMessage());
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

    @FXML
    private void handleTaskDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
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

    private void openTaskDetailsPage(Tache task) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/taskdetails.fxml"));
            Parent root = loader.load();

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

    @FXML
    private void handleExportToPDF() {
        try {
            String filePath = "Tasks_Report.pdf";
            List<Tache> tasks = tacheService.getAllByProject(projectId);
            PDFGenerator.generateTaskPdf(tasks, filePath);
            lblStatus.setText("PDF Exported Successfully!");
        } catch (Exception e) {
            lblStatus.setText("Error exporting PDF: " + e.getMessage());
        }
    }
}
