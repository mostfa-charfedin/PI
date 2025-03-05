package Controllers;

import Models.Tache;
import Services.PDFGenerator;
import Services.TacheService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Usertachevue {
    @FXML
    private ListView<String> taskListView;
    @FXML
    private Label lblStatus;

    private TacheService tacheService = new TacheService();
    private int projectId;

    public void initData(int projectId) {
        this.projectId = projectId;
        loadTasks();
    }

    private void loadTasks() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            lblStatus.setText("Session expired! Please log in.");
            return;
        }

        int userId = session.getUserId();
        List<Tache> tasks = tacheService.getTasksForUserInProject(userId, projectId);

        if (tasks.isEmpty()) {
            lblStatus.setText("No tasks found for this project.");
        } else {
            lblStatus.setText("");
        }

        ObservableList<String> taskTitles = FXCollections.observableArrayList();
        for (Tache t : tasks) {
            taskTitles.add(t.getTitre() + " - Status: " + t.getStatus());
        }

        taskListView.setItems(taskTitles);
    }

    @FXML
    public void initialize() {
        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-click event
                String selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    openTaskDetailPopup(selectedTask);
                }
            }
        });
    }

    private void openTaskDetailPopup(String taskTitle) {
        try {
            for (Tache task : tacheService.getTasksForUserInProject(UserSession.getInstance().getUserId(), projectId)) {
                if (taskTitle.contains(task.getTitre())) { // Match title
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/TaskDetailView.fxml"));
                    Parent root = loader.load();

                    // Pass task data & reference to refresh method
                    TaskDetailController controller = loader.getController();
                    controller.initData(task, this);

                    // Open in a new window
                    Stage stage = new Stage();
                    stage.setTitle("Task Details");
                    stage.setScene(new Scene(root));
                    stage.show();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading task details: " + e.getMessage());
        }
    }

    // Method to refresh task list
    public void refreshTaskList() {
        loadTasks();
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
