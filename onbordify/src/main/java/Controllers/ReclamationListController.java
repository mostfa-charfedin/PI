package Controllers;

import Models.Reclamation;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import Services.ReclamationService;
import java.io.IOException;
import java.util.List;

public class ReclamationListController {

    @FXML
    private ListView<Reclamation> reclamationListView;

    private ReclamationService reclamationService;

    public void initialize() {
        reclamationService = new ReclamationService();

        try {
            // Load reclamations from the service
            List<Reclamation> reclamations = reclamationService.getAll();

            // Populate the ListView with the reclamations
            reclamationListView.getItems().addAll(reclamations);

            // Set cell factory to display the reclamation's ID and a button to update
            reclamationListView.setCellFactory(param -> new ListCell<Reclamation>() {
                @Override
                protected void updateItem(Reclamation reclamation, boolean empty) {
                    super.updateItem(reclamation, empty);
                    if (empty || reclamation == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button updateButton = new Button("Update");
                        updateButton.setOnAction(event -> openUpdateWindow(reclamation));
                        HBox hbox = new HBox(10, new Label("ID: " + reclamation.getIdReclamation()), updateButton);
                        hbox.setAlignment(Pos.CENTER_LEFT);
                        setGraphic(hbox);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to open the update window
    private void openUpdateWindow(Reclamation reclamation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listreclamation.fxml"));
            Parent root = loader.load();

            // Set the selected reclamation in the update form
            UpdateReclamationStatusController controller = loader.getController();
            controller.setReclamation(reclamation);

            // Show the window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for the Back button
    @FXML
    private void goBack(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}