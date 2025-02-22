package Controller;

import Services.programmebienetreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
    @FXML
    private Button btnModifier;


    @FXML
    private Button btnCreateRecompense; // Nouveau bouton

    private programmebienetreService service = new programmebienetreService();
    //conversion taa liste observation liste bch tkhali listeview dima dynamique
    private ObservableList<models.programmebienetre> programmeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser la ComboBox avec les types
        cmbType.getItems().addAll("Fitness", "Nutrition", "Mental Health", "Wellness");

        // Charger les programmes existants
        loadProgrammes();

        // Ajouter un événement au bouton Ajouter
        btnAjouter.setOnAction(event -> ajouterProgramme());

        // Ajouter un événement au bouton CreateRecompense
        btnCreateRecompense.setOnAction(event -> createRecompense());
        btnModifier.setOnAction(event -> modifierProgramme());

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
        if (!titre.matches("^[a-zA-Z]+$")) {
            showAlert("Erreur de saisie", "Le titre ne doit contenir que des lettres (pas de nombres ni d'espaces).");
            return;
        }
        // Si le bouton "Ajouter" est en mode "Mettre à jour"
        if (btnAjouter.getText().equals("Mettre à jour")) {
            int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Attention", "Veuillez sélectionner un programme à modifier !");
                return;
            }

            // Récupérer le programme sélectionné
            models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);

            // Mettre à jour les données du programme
            selectedProgramme.setTitre(titre);
            selectedProgramme.setType(type);
            selectedProgramme.setDescription(description);

            try {
                service.update(selectedProgramme);
                showAlert("Succès", "Le programme a été mis à jour avec succès !");

                // Recharger les programmes après la mise à jour
                loadProgrammes();

                // Réinitialiser les champs après la mise à jour
                txtTitre.clear();
                cmbType.getSelectionModel().clearSelection();
                txtDescription.clear();
                btnModifier.setDisable(false);
                // Revenir au mode "Ajouter"
                btnAjouter.setText("Ajouter Programme");


            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la mise à jour du programme !");
            }
        } else {
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
    }
    @FXML
    private void modifierProgramme() {
        int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner un programme à modifier !");
            return;
        }

        // Récupérer le programme sélectionné
        models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);

        // Remplir les champs avec les données du programme sélectionné
        txtTitre.setText(selectedProgramme.getTitre());
        cmbType.setValue(selectedProgramme.getType());
        txtDescription.setText(selectedProgramme.getDescription());

        // Changer le texte du bouton "Ajouter" en "Mettre à jour"
        btnAjouter.setText("Mettre à jour");

        // Désactiver uniquement le bouton "Modifier" pour éviter les conflits
        btnModifier.setDisable(true);
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

    @FXML
    private void createRecompense() {
        try {
            // Charger la nouvelle vue (recompense.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recompense.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Récupérer la fenêtre actuelle (stage)
            Stage stage = (Stage) btnCreateRecompense.getScene().getWindow();

            // Changer la scène de la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger la page de récompense !");
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
