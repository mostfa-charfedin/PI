package Controller;

import Services.RecompenseService;
import Services.programmebienetreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public ComboBox<Models.programmebienetre> cmbProgrammes;  // Ensure type safety with generics
    @FXML
    private ComboBox<String> cmbRecompenseType;  // ComboBox pour le type de récompense

    @FXML
    private DatePicker dateAttribution;  // Sélecteur de date pour la date d'attribution

    @FXML
    private ComboBox<String> cmbStatut;  // ComboBox pour le statut de la récompense

    @FXML
    private ListView<String> listViewRecompenses;  // ListView pour afficher les récompenses

    @FXML
    private Button btnAjouter;  // Bouton pour ajouter une récompense

    @FXML
    private Button btnSupprimer;  // Bouton pour supprimer une récompense

    @FXML
    private Button btnModifier;  // Bouton pour modifier une récompense

    private RecompenseService service = new RecompenseService();  // Service pour gérer les récompenses
    private ObservableList<Models.Recompense> recompenseList = FXCollections.observableArrayList();  // Liste observable des récompenses

    private int programmeId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation des données
        initializeProgrammes();
        initializeStatut();
        initializeListView();
        // Initialiser les types de récompense
        cmbRecompenseType.getItems().clear();
        cmbRecompenseType.getItems().addAll("Gift Card", "Voucher", "Bonus", "Recognition", "Other");
        // Blocage des dates passées dans le DatePicker
        dateAttribution.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);  // Désactiver les dates passées
                    setStyle("-fx-background-color: #ffc0cb;"); // Optionnel : couleur pour les dates désactivées
                }
            }
        });
    }

    // Initialiser la ComboBox des programmes
    private void initializeProgrammes() {
        programmebienetreService programmeService = new programmebienetreService();
        try {
            List<Models.programmebienetre> programmes = programmeService.getAll();
            cmbProgrammes.setItems(FXCollections.observableList(programmes));

            cmbProgrammes.setCellFactory(param -> new ListCell<Models.programmebienetre>() {
                @Override
                protected void updateItem(Models.programmebienetre programme, boolean empty) {
                    super.updateItem(programme, empty);
                    setText(programme == null || empty ? null : programme.getTitre());
                }
            });

            cmbProgrammes.setButtonCell(new ListCell<Models.programmebienetre>() {
                @Override
                protected void updateItem(Models.programmebienetre programme, boolean empty) {
                    super.updateItem(programme, empty);
                    setText(programme == null || empty ? null : programme.getTitre());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les programmes !");
        }
    }

    // Initialiser la ComboBox des statuts
    private void initializeStatut() {
        cmbStatut.setItems(FXCollections.observableArrayList("Pending", "Approved", "Delivered", "Cancelled"));
    }

    // Initialiser la ListView des récompenses
    private void initializeListView() {
        listViewRecompenses.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadSelectedRecompense(newSelection);
            }
        });

        loadRecompenses();
    }

    // Charger les détails de la récompense sélectionnée dans le formulaire
    private void loadSelectedRecompense(String selection) {
        String[] details = selection.split(" - ");
        if (details.length == 3) {
            cmbRecompenseType.setValue(details[0]);
            dateAttribution.setValue(LocalDate.parse(details[1]));
            cmbStatut.setValue(details[2]);
        }
    }

    // Charger toutes les récompenses dans la ListView
    private void loadRecompenses() {
        try {
            List<Models.Recompense> recompenses = service.getAll();
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

    // Ajouter une récompense
    @FXML
    private void creer() {
        String type = cmbRecompenseType.getValue();
        LocalDate date = dateAttribution.getValue();
        String statusRecompence = cmbStatut.getValue();

        // Validation des champs
        if (type == null || type.isEmpty() || date == null || statusRecompence == null) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }

        Models.programmebienetre programmeSelectionne = cmbProgrammes.getSelectionModel().getSelectedItem();
        if (programmeSelectionne == null) {
            showAlert("Attention", "Veuillez sélectionner un programme !");
            return;
        }

        // Création de la récompense
        Models.Recompense recompense = new Models.Recompense(0, type, date, statusRecompence, programmeSelectionne.getIdProgramme());
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

    // Supprimer une récompense
    @FXML
    private void supprimerRecompense() {
        int selectedIndex = listViewRecompenses.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner une récompense à supprimer !");
            return;
        }

        Models.Recompense selectedRecompense = recompenseList.get(selectedIndex);
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

    // Modifier une récompense
    @FXML
    private void ModifierRecompense() {
        int selectedIndex = listViewRecompenses.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner une récompense à modifier !");
            return;
        }

        Models.Recompense selectedRecompense = recompenseList.get(selectedIndex);

        String type = cmbRecompenseType.getValue();
        LocalDate date = dateAttribution.getValue();
        String statusRecompence = cmbStatut.getValue();

        if (type == null || type.isEmpty() || date == null || statusRecompence == null) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }

        Models.programmebienetre programmeSelectionne = cmbProgrammes.getSelectionModel().getSelectedItem();
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
            loadRecompenses(); // Recharger la liste des récompenses
            showAlert("Succès", "La récompense a été modifiée avec succès !");
            clearForm(); // Effacer les champs du formulaire
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur SQL", "Problème lors de la mise à jour en base de données : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur inattendue s'est produite : " + e.getMessage());
        }
    }

    // Effacer les champs du formulaire
    private void clearForm() {
        cmbRecompenseType.getSelectionModel().clearSelection();
        dateAttribution.setValue(null);
        cmbStatut.getSelectionModel().clearSelection();
        cmbProgrammes.getSelectionModel().clearSelection();
    }

    // Afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }
}