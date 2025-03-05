package Controllers;

import Models.Tache;
import Services.TacheService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class TaskDetailController {
    @FXML private Label lblTaskTitle;
    @FXML private Label lblTaskDescription;
    @FXML private Label lblTaskProject;
    @FXML private Label lblTaskStatus;
    @FXML private Button btnCompleteTask;

    private Tache currentTask;
    private TacheService tacheService = new TacheService();
    private Usertachevue usertachevueController;

    public void initData(Tache task, Usertachevue usertachevueController) {
        this.currentTask = task;
        this.usertachevueController = usertachevueController;

        lblTaskTitle.setText("Title: " + task.getTitre());
        lblTaskDescription.setText("Description: " + task.getDescription());
        lblTaskProject.setText("Project: " + task.getTitreProjet());
        lblTaskStatus.setText("Status: " + task.getStatus());

        // Disable button if task is already completed
        if ("done".equalsIgnoreCase(task.getStatus())) {
            btnCompleteTask.setDisable(true);
        }
    }


    @FXML
    private void handleCompleteTask() {
        if (currentTask != null) {
            tacheService.updateTaskStatus(currentTask.getIdTache(), "done");
            lblTaskStatus.setText("Status: done");
            btnCompleteTask.setDisable(true);

            // Refresh the task list in Usertachevue
            if (usertachevueController != null) {
                usertachevueController.refreshTaskList();
            }

            // Close the popup window
            Stage stage = (Stage) btnCompleteTask.getScene().getWindow();
            stage.close();
        }
    }
}
