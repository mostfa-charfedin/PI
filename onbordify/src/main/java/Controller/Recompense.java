package Controller;

import Services.RecompenseService;
import Services.programmebienetreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Recompense implements Initializable {
    @FXML
    public ComboBox<models.programmebienetre> cmbProgrammes;  // Ensure type safety with generics

    @FXML
    private TextField txtType;

    @FXML
    private DatePicker dateAttribution;

    @FXML
    private ComboBox<String> cmbStatut;

    @FXML
    private ListView<String> listViewRecompenses;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnSupprimer;
    @FXML
    private Button btnModifier;

    private RecompenseService service = new RecompenseService();
    private ObservableList<models.Recompense> recompenseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        programmebienetreService programmeService = new programmebienetreService();
        try {
            List<models.programmebienetre> programmes = programmeService.getAll();
            cmbProgrammes.setItems(FXCollections.observableList(programmes));

            cmbProgrammes.setCellFactory(param -> new ListCell<models.programmebienetre>() {
                @Override
                protected void updateItem(models.programmebienetre programme, boolean empty) {
                    super.updateItem(programme, empty);
                    setText(programme == null || empty ? null : programme.getTitre());
                }
            });

            cmbProgrammes.setButtonCell(new ListCell<models.programmebienetre>() {
                @Override
                protected void updateItem(models.programmebienetre programme, boolean empty) {
                    super.updateItem(programme, empty);
                    setText(programme == null || empty ? null : programme.getTitre());
                }
            });

            listViewRecompenses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    loadSelectedRecompense(newSelection);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les programmes !");
        }

        loadRecompenses();
    }

    private void loadSelectedRecompense(String selection) {
        String[] details = selection.split(" - ");
        if (details.length == 3) {
            txtType.setText(details[0]);
            dateAttribution.setValue(LocalDate.parse(details[1]));
            cmbStatut.setValue(details[2]);
        }
    }

    private void loadRecompenses() {
        try {
            List<models.Recompense> recompenses = service.getAll();
            recompenseList.clear();
            recompenseList.addAll(recompenses);
            listViewRecompenses.setItems(FXCollections.observableArrayList(
                    recompenses.stream().map(r -> r.getType_Recompense() + " - " + r.getDateAttribution() + " - " + r.getStatusRecompence()).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les récompenses : " + e.getMessage());
        }
    }


    @FXML
    private void creer() {
        String type = txtType.getText();
        LocalDate date = dateAttribution.getValue();
        String statusRecompence = cmbStatut.getValue();

        // Validation des champs
        if (type.isEmpty() || date == null || statusRecompence == null) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }
        if (!type.matches("^[a-zA-Z]+$")) {
            showAlert("Erreur de saisie", "Le type ne doit contenir que des lettres (pas de nombres ni d'espaces).");
            return;
        }

        models.programmebienetre programmeSelectionne = cmbProgrammes.getSelectionModel().getSelectedItem();
        if (programmeSelectionne == null) {
            showAlert("Attention", "Veuillez sélectionner un programme !");
            return;
        }

        // Création de la récompense
        models.Recompense recompense = new models.Recompense(0, type, date, statusRecompence, programmeSelectionne.getIdProgramme());
        try {
            service.create(recompense);
            loadRecompenses(); // Recharger la liste des récompenses
            clearForm(); // Effacer les champs du formulaire
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Problème lors de l'insertion en base de données : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
    }

    // Méthode pour effacer les champs du formulaire
    private void clearForm() {
        txtType.clear();
        dateAttribution.setValue(null);
        cmbStatut.getSelectionModel().clearSelection();
        cmbProgrammes.getSelectionModel().clearSelection();
    }
    @FXML
    private void supprimerRecompense() {
        int selectedIndex = listViewRecompenses.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner une récompense à supprimer !");
            return;
        }

        models.Recompense selectedRecompense = recompenseList.get(selectedIndex);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer \"" + selectedRecompense.getType_Recompense() + "\" ?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer la récompense");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(selectedRecompense.getIdRecompense());
                loadRecompenses();
                showAlert("Succès", "La récompense a été supprimée avec succès !");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression de la récompense !");
            }
        }
    }
    @FXML
    private void ModifierRecompense() {
        int selectedIndex = listViewRecompenses.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner une récompense à modifier !");
            return;
        }

        models.Recompense selectedRecompense = recompenseList.get(selectedIndex);

        String type = txtType.getText();
        LocalDate date = dateAttribution.getValue();
        String statusRecompence = cmbStatut.getValue();

        if (type.isEmpty() || date == null || statusRecompence == null) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }

        models.programmebienetre programmeSelectionne = cmbProgrammes.getSelectionModel().getSelectedItem();
        if (programmeSelectionne == null) {
            showAlert("Attention", "Veuillez sélectionner un programme !");
            return;
        }

        selectedRecompense.setType_Recompense(type);
        selectedRecompense.setDateAttribution(date);
        selectedRecompense.setStatusRecompence(statusRecompence);
        selectedRecompense.setIdProgramme(programmeSelectionne.getIdProgramme());

        try {
            service.update(selectedRecompense);
            loadRecompenses(); // Reload the rewards list
            showAlert("Succès", "La récompense a été modifiée avec succès !");
            clearForm(); // Clear the form fields
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Problème lors de la mise à jour en base de données : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}