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

    @FXML
    public void initialize() {
        tacheService = new TacheService();
        loadTasks();

        // Handle single selection in ListView
        taskListView.setOnMouseClicked(event -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedTask = tacheService.getAll().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadTasks() {
        taskListView.getItems().clear();
        try {
            List<Tache> tasks = tacheService.getAll();
            for (Tache task : tasks) {
                taskListView.getItems().add(task.getTitre() + " - Assigned to: " + task.getNom() + " " + task.getPrenom());
            }
        } catch (Exception e) {
            lblStatus.setText("Error loading tasks: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateTask() {
        openPopup("/taskcreate.fxml", "Create Task");
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
