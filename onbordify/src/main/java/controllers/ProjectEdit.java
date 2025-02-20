package controllers;

import Models.Projet;
import Services.ProjetService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class ProjectEdit {

    @FXML
    private TextField txtTitre;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cbProjectManager;

    @FXML
    private Label lblStatus;

    private Projet project;
    private Projectview projectView;

    public void setProjectData(Projet project, Projectview projectView) {
        this.project = project;
        this.projectView = projectView;

        // Populate the fields with the project data
        txtTitre.setText(project.getTitre());
        txtDescription.setText(project.getDescription());

        // Load project managers into the ComboBox
        try {
            ProjetService projetService = new ProjetService();
            List<String> chefNames = projetService.getAllChefProjetNames();
            cbProjectManager.getItems().addAll(chefNames);

            // Set the current project manager as the selected value
            String currentManager = project.getNom() + " " + project.getPrenom();
            cbProjectManager.setValue(currentManager);
        } catch (Exception e) {
            lblStatus.setText("Error loading project managers: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String title = txtTitre.getText();
        String description = txtDescription.getText();
        String manager = cbProjectManager.getValue();

        if (title.isEmpty() || description.isEmpty() || manager == null) {
            lblStatus.setText("All fields are required!");
            return;
        }

        String[] nameParts = manager.split(" ", 2);
        if (nameParts.length < 2) {
            lblStatus.setText("Invalid project manager selection");
            return;
        }

        String nom = nameParts[0];
        String prenom = nameParts[1];

        // ✅ Fix: Use the existing project ID
        Projet projet = new Projet  (project.getIdProjet(), title, description, nom, prenom);

        try {
            ProjetService projetService = new ProjetService();
            projetService.update(projet);

            // Refresh the project list in the main view
            projectView.refreshProjects();

            // Close the pop-up window
            Stage stage = (Stage) txtTitre.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Error saving project: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        // Close the pop-up window without saving
        Stage stage = (Stage) txtTitre.getScene().getWindow();
        stage.close();
    }
}