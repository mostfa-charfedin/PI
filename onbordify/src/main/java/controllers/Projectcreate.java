package controllers;

import Models.Projet;
import Services.ProjetService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class Projectcreate {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> cbProjectManager;
    @FXML
    private Button btnCreate;
    @FXML
    private Label lblStatus;

    private final ProjetService projetService = new ProjetService();

    @FXML
    public void initialize() {
        // Load project managers into dropdown
        try {
            List<String> managers = projetService.getAllChefProjetNames();
            ObservableList<String> managerList = FXCollections.observableArrayList(managers);
            cbProjectManager.setItems(managerList);
        } catch (Exception e) {
            lblStatus.setText("Error loading project managers.");
        }
    }

    @FXML
    private void handleCreateProject() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String selectedManager = cbProjectManager.getValue();

        if (title.isEmpty() || description.isEmpty() || selectedManager == null) {
            lblStatus.setText("Please fill all fields!");
            return;
        }

        try {
            Projet newProject = new Projet(title, description, 0); // idUser will be retrieved inside the service
            projetService.create(newProject);
            lblStatus.setText("Project created successfully!");
            lblStatus.setStyle("-fx-text-fill: green;");

            // Clear fields after success
            txtTitle.clear();
            txtDescription.clear();
            cbProjectManager.getSelectionModel().clearSelection();
        } catch (Exception e) {
            lblStatus.setText("Error creating project.");
        }
    }
}
