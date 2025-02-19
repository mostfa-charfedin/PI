package Controller;

import Services.programmebienetreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class programmebienetre implements Initializable {

    @FXML
    private TextField txtTitre;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private TextArea txtDescription;

    @FXML
    private ListView<String> listViewProgrammes;

    @FXML
    private Button btnAjouter;

    private programmebienetreService service = new programmebienetreService();
    private ObservableList<models.programmebienetre> programmeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser la ComboBox avec les types
        cmbType.getItems().addAll("Fitness", "Nutrition", "Mental Health", "Wellness");

        // Charger les programmes existants
        loadProgrammes();

        // Ajouter un événement au bouton Ajouter
        btnAjouter.setOnAction(event -> ajouterProgramme());

        // Ajouter un événement de suppression sur double-clic
        listViewProgrammes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Double-clic détecté
                supprimerProgramme();
            }
        });
    }

    private void loadProgrammes() {
        try {
            List<models.programmebienetre> programmes = service.getAll();
            programmeList.clear();
            programmeList.addAll(programmes);

            listViewProgrammes.setItems(FXCollections.observableArrayList(
                    programmes.stream().map(p -> p.getTitre() + " - " + p.getDescription()).toList()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les programmes !");
        }
    }

    @FXML
    private void ajouterProgramme() {
        String titre = txtTitre.getText();
        String type = cmbType.getValue();
        String description = txtDescription.getText();

        // Vérification des champs vides
        if (titre.isEmpty() || type == null || description.isEmpty()) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }

        // Création et ajout du programme
        models.programmebienetre programme = new models.programmebienetre(0, titre, type, description);
        try {
            service.create(programme);

            // Recharger les programmes après ajout
            loadProgrammes();

            // Réinitialiser les champs après l'ajout
            txtTitre.clear();
            cmbType.getSelectionModel().clearSelection();
            txtDescription.clear();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout du programme !");
        }
    }

    @FXML
    private void supprimerProgramme() {
        int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner un programme à supprimer !");
            return;
        }

        // Récupérer l'objet programmebienetre correspondant
        models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);
        int id = selectedProgramme.getIdProgramme();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le programme");
        alert.setContentText("Voulez-vous vraiment supprimer \"" + selectedProgramme.getTitre() + "\" ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(id); // Suppression par ID
                loadProgrammes(); // Recharger la liste après suppression
                showAlert("Succès", "Le programme a été supprimé avec succès !");
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la suppression du programme !");
            }
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