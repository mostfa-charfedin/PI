package Controllers;

import Models.Projet;
import Services.ProjetService;
import utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;

public class Userprojectvue {
    @FXML
    private ListView<String> projectListView;
    @FXML
    private Label lblStatus;

    private ProjetService projetService = new ProjetService();
    private List<Projet> projects; // Store projects for reference

    @FXML
    public void initialize() {
        loadProjects();

        // Add event listener for double-click
        projectListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openProjectTasks(event);
            }
        });
    }

    private void loadProjects() {
        UserSession session = UserSession.getInstance();
        if (session == null) {
            lblStatus.setText("Session expired! Please log in.");
            return;
        }

        int userId = session.getUserId();
        projects = projetService.getProjectsForUser(userId); // Store the projects list

        if (projects.isEmpty()) {
            lblStatus.setText("No projects found.");
        } else {
            lblStatus.setText("");
        }

        ObservableList<String> projectTitles = FXCollections.observableArrayList();
        for (Projet p : projects) {
            projectTitles.add(p.getTitre() + " - Managed by: " + p.getNom() + " " + p.getPrenom());
        }

        projectListView.setItems(projectTitles);
    }

    private void openProjectTasks(MouseEvent event) {
        int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Projet selectedProject = projects.get(selectedIndex);
            showTaskPopup(selectedProject.getIdProjet());
        }
    }

    private void showTaskPopup(int projectId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usertachevue.fxml"));
            Parent root = loader.load();

            Usertachevue controller = loader.getController();
            controller.initData(projectId);

            Stage stage = new Stage();
            stage.setTitle("Project Tasks");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            showError("Error loading tasks", "Could not open the tasks view.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
