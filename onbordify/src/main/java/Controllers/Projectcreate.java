package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Models.Projet;
import Services.ProjetService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Projectcreate implements Initializable {
    private ProjetService projetService;
    private Projectview projectViewController; // Reference to Projectview

    @FXML
    private TextField txtTitle;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ComboBox<String> cbProjectManager;

    @FXML
    private Button btnCreate, btnViewProjects;

    @FXML
    private Label lblStatus;

    public Projectcreate() {
        this.projetService = new ProjetService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadProjectManagers();
    }

    public void setProjectViewController(Projectview projectViewController) {
        this.projectViewController = projectViewController;
    }

    private void loadProjectManagers() {
        try {
            List<String> managers = projetService.getAllChefProjetNames();
            cbProjectManager.getItems().addAll(managers);
        } catch (Exception e) {
            lblStatus.setText("Error loading project managers.");
        }
    }

    @FXML
    void handleCreateProject(ActionEvent event) {
        String title = txtTitle.getText();
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

        Projet projet = new Projet(title, description, nom, prenom);
        try {
            projetService.create(projet);

            // Refresh project list in Projectview
            if (projectViewController != null) {
                projectViewController.refreshProjects();
            }

            resetFields();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Project created successfully!");
            alert.showAndWait();

            // Close the current window
            Stage stage = (Stage) btnCreate.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void handleViewProjects(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/projectvue.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnViewProjects.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setContentText("Failed to load the project view page.");
            alert.showAndWait();
        }
    }

    private void resetFields() {
        txtTitle.clear();
        txtDescription.clear();
        cbProjectManager.setValue(null);
    }
}
