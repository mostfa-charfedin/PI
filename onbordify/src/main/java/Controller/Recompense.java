/*package Controller;

import Services.RecompenseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.List;

public class Recompense {
    @FXML
    private TextField txtType;
    @FXML
    private DatePicker dateAttribution;
    @FXML
    private ComboBox<String> cmbStatut;
    @FXML
    private ListView<String> listViewRecompenses;

    private RecompenseService recompenseService;
    private ObservableList<String> recompenseList;

    public Recompense() {
        recompenseService = new RecompenseService();
        recompenseList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Charger les récompenses existantes
        loadRecompense();

        // Initialiser le ComboBox avec les statuts valides
        cmbStatut.setItems(FXCollections.observableArrayList("Expired", "Received", "Waiting"));
    }

    @FXML
    private void ajouterRecompense() {
        String type = txtType.getText().trim();
        LocalDate date = dateAttribution.getValue();
        String statut = cmbStatut.getValue();

        // Vérifications de saisie
        if (type.isEmpty() || date == null || statut == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs !");
            return;
        }

        if (type.length() > 100) {
            showAlert("Erreur", "Le type ne doit pas dépasser 100 caractères !");
            return;
        }

        // Création et ajout de la récompense
        models.Recompense recompense = new Recompense(0, type, date.toString(), statut, 1); // 1 est un idProgramme fictif
        try {
            recompenseService.create(recompense);
            recompenseList.add(type + " - " + date + " - " + statut);
            listViewRecompenses.setItems(recompenseList);

            // Réinitialisation des champs
            txtType.clear();
            dateAttribution.setValue(null);
            cmbStatut.getSelectionModel().clearSelection();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ajouter la récompense : " + e.getMessage());
        }
    }

    /*@FXML
    private void supprimerRecompense() {
        String selectedItem = listViewRecompenses.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Erreur", "Veuillez sélectionner une récompense à supprimer !");
            return;
        }

        try {
            // Extraire le type depuis la liste
            String type = selectedItem.split(" - ")[0];

            // Récupérer la récompense correspondante
            List<models.Recompense> recompenses = recompenseService.getAll();
            models.Recompense recompenseASupprimer = null;
            for (models.Recompense r : recompenses) {
                if (r.getType_Recompense().equals(type)) {
                    recompenseASupprimer = r;
                    break;
                }
            }

            if (recompenseASupprimer != null) {
                recompenseService.delete(recompenseASupprimer.getIdRecompense());
                recompenseList.remove(selectedItem);
                listViewRecompenses.setItems(recompenseList);
            } else {
                showAlert("Erreur", "Récompense introuvable !");
            }

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de supprimer la récompense : " + e.getMessage());
        }
    }

    private void loadRecompenses() {
        try {
            recompenseList.clear();
            List<Recompense> recompenses = recompenseService.getAll();
            for (Recompense r : recompenses) {
                recompenseList.add(r.getType_Recompense() + " - " + r.getDateAttribution() + " - " + r.getStatutdeRecompense());
            }
            listViewRecompenses.setItems(recompenseList);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les récompenses : " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
*/