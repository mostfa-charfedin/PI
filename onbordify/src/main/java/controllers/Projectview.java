package controllers;

import Services.ProjetService;
import Models.Projet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Projectview {

    @FXML
    private ListView<String> projectListView;

    @FXML
    private Label lblStatus;

    @FXML
    private Button btnCreateProject, btnEditProject, btnDeleteProject, btnViewTasks;

    private ProjetService projetService;
    private Projet selectedProject;

    @FXML
    public void initialize() {
        projetService = new ProjetService();
        loadProjects();

        // Handle project selection
        projectListView.setOnMouseClicked(event -> {
            int selectedIndex = projectListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    selectedProject = projetService.getAll().get(selectedIndex);
                } catch (Exception e) {
                    lblStatus.setText("Error selecting project: " + e.getMessage());
                }
            }
        });
    }

    private void loadProjects() {
        projectListView.getItems().clear();
        try {
            List<Projet> projets = projetService.getAll();
            for (Projet projet : projets) {
                projectListView.getItems().add(projet.getTitre() + " - " + projet.getNom() + " " + projet.getPrenom());
            }
        } catch (Exception e) {
            lblStatus.setText("Error loading projects: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateProject() {
        openPopup("/projectcreate.fxml", "Create Project");
    }

    @FXML
    private void handleEditProject() {
        if (selectedProject == null) {
            lblStatus.setText("Select a project to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projectedit.fxml"));
            Parent root = loader.load();

            ProjectEdit editController = loader.getController();
            editController.setProjectData(selectedProject, this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Edit Project");
            popupStage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening edit window: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteProject() throws Exception {
        if (selectedProject == null) {
            lblStatus.setText("Select a project to delete.");
            return;
        }
        projetService.delete(selectedProject.getIdProjet());
        loadProjects();
        lblStatus.setText("Project deleted.");
    }

    @FXML
    private void handleViewTasks() {
        if (selectedProject == null) {
            lblStatus.setText("Please select a project first.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tacheview.fxml"));
            Parent root = loader.load();

            // Pass project ID to Tacheview controller
            Tacheview tacheController = loader.getController();
            tacheController.setProjectId(selectedProject.getIdProjet());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Task View");
            stage.show();
        } catch (IOException e) {
            lblStatus.setText("Error opening Task View: " + e.getMessage());
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

    public void refreshProjects() {
        loadProjects();
    }
}
