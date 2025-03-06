package Controllers;

import Models.Reclamation;
import Services.ReclamationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Services.SpamService;

import java.time.LocalDate;
import java.util.List;

public class ReclamationController {

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField commentaireField;

    @FXML
    private DatePicker dateField;

    private ReclamationService reclamationService = new ReclamationService();
    private ObservableList<String> reclamationsList = FXCollections.observableArrayList();
    SpamService spamService = new SpamService();

    @FXML
    public void initialize() {
        loadReclamations();
        dateField.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now())); // Empêche la sélection de dates futures
            }
        });
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.getAll();
        reclamationsList.clear();
        for (Reclamation rec : reclamations) {
            reclamationsList.add("ID: " + rec.getIdReclamation() + " | " + rec.getCommentaire() + " | " + rec.getDate());
        }
        listView.setItems(reclamationsList);
    }

    @FXML
    private void ajouterReclamation() {
        String commentaire = commentaireField.getText();
        LocalDate date = dateField.getValue();

        if (commentaire.isEmpty() || date == null) {
            showAlert("Error", "Please enter a comment and select a date.");
            return;
        }

        // Vérification si la date est dans le futur
        if (date.isAfter(LocalDate.now())) {
            showAlert("Error", "The date cannot be in the future.");
            return;
        }

        if (!spamService.traiterReclamation(commentaire)) {
            Reclamation newRec = new Reclamation();
            newRec.setCommentaire(commentaire);
            newRec.setDate(date);
            newRec.setIdUser(1);  // Set the user ID dynamically if needed
            newRec.setEtat("Pending");

            reclamationService.add(newRec);
            showAlert("Success", "Claim added successfully!");
            loadReclamations();
            commentaireField.clear();
            dateField.setValue(null);
        }
    }

    @FXML
    private void supprimerReclamation() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String selectedItem = listView.getSelectionModel().getSelectedItem();
            int id = Integer.parseInt(selectedItem.split("\\|")[0].replace("ID: ", "").trim());

            reclamationService.delete2(id);
            showAlert("Success", "Claim deleted successfully!");
            loadReclamations();
        } else {
            showAlert("Error", "Please select a claim to delete.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
