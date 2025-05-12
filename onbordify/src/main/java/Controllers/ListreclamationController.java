package Controllers;

import Models.Reclamation;
import Services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import utils.UserSession;

import java.util.List;

public class ListreclamationController {

    @FXML
    private ListView<String> reclamationListView;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button updateStatusButton;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<String> reclamationList = FXCollections.observableArrayList();
    private UserSession session = UserSession.getInstance();

    @FXML
    public void initialize() {
        // Initialiser la ComboBox avec les statuts possibles
        statusComboBox.setItems(FXCollections.observableArrayList("PENDING", "REJECTED", "RESOLVED"));

        // Charger les réclamations
        loadReclamations();

        // Configurer le rendu des cellules avec les couleurs
        reclamationListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);

                    // Appliquer la couleur selon le statut
                    if (item.contains("PENDING")) {
                        setTextFill(Color.ORANGE);
                    } else if (item.contains("RESOLVED")) {
                        setTextFill(Color.GREEN);
                    } else if (item.contains("REJECTED")) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });

        // Activer/désactiver le bouton selon la sélection
        reclamationListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateButtonState();
        });

        statusComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateButtonState();
        });
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.getAll();
        reclamationList.clear();

        for (Reclamation rec : reclamations) {
            String status;
            if (rec.getStatus() == null) {
                status = "PENDING";
            } else if (rec.getStatus()) {
                status = "RESOLVED";
            } else {
                status = "REJECTED";
            }

            // Format d'affichage avec l'ID inclus
            String reclamationItem = String.format("ID: %d | Subject: %s | Content: %s | Status: %s",
                    rec.getId(),
                    rec.getSubject(),
                    rec.getContent(),
                    status);

            reclamationList.add(reclamationItem);
        }

        reclamationListView.setItems(reclamationList);
    }

    @FXML
    private void handleUpdateStatus(ActionEvent event) {
        int selectedIndex = reclamationListView.getSelectionModel().getSelectedIndex();
        String selectedStatus = statusComboBox.getValue();

        if (selectedIndex >= 0 && selectedStatus != null) {
            try {
                String selectedItem = reclamationListView.getSelectionModel().getSelectedItem();

                // Vérifier que l'item contient bien un ID
                if (!selectedItem.contains("ID:")) {
                    showAlert("Error", "Invalid complaint format. Please contact support.");
                    return;
                }

                // Extraire l'ID
                int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID:", "").trim());

                Boolean statusValue = null;
                // Modification ici - utilisez une méthode disponible dans UserSession
                String response = "Status updated by user ID: " + session.getUserId();

                switch (selectedStatus) {
                    case "RESOLVED":
                        statusValue = true;
                        break;
                    case "REJECTED":
                        statusValue = false;
                        break;
                    case "PENDING":
                        statusValue = null;
                        break;
                }

                // Mettre à jour le statut
                reclamationService.updateStatus(id, statusValue, response, session.getUserId());

                showAlert("Success", "Complaint status updated successfully!");
                loadReclamations(); // Rafraîchir la liste
            } catch (NumberFormatException e) {
                showAlert("Error", "Could not parse complaint ID: " + e.getMessage());
            } catch (Exception e) {
                showAlert("Error", "An error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Error", "Please select a complaint and a new status.");
        }
    }

    private void updateButtonState() {
        boolean isSelected = reclamationListView.getSelectionModel().getSelectedIndex() >= 0;
        boolean hasStatus = statusComboBox.getValue() != null;
        updateStatusButton.setDisable(!isSelected || !hasStatus);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}