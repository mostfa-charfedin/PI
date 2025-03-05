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
import javafx.stage.Modality;
import javafx.stage.Stage;
import Services.EmailService;
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
    private Button btnCreateRecompense;

    private programmebienetreService service = new programmebienetreService();
    private ObservableList<Models.programmebienetre> programmeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbType.getItems().addAll("Fitness", "Nutrition", "Mental Health", "Wellness");

        loadProgrammes();

        btnAjouter.setOnAction(event -> ajouterProgramme());
        btnCreateRecompense.setOnAction(event -> createRecompense());
        btnModifier.setOnAction(event -> modifierProgramme());

        listViewProgrammes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                supprimerProgramme();
            }
        });
    }

    private void loadProgrammes() {
        try {
            List<Models.programmebienetre> programmes = service.getAll();
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
        EmailService emailService = new EmailService();
        String titre = txtTitre.getText();
        String type = cmbType.getValue();
        String description = txtDescription.getText();

        if (titre.isEmpty() || type == null || description.isEmpty()) {
            showAlert("Attention", "Veuillez remplir tous les champs !");
            return;
        }
        if (!titre.matches("^[a-zA-Z]+$")) {
            showAlert("Erreur de saisie", "Le titre ne doit contenir que des lettres (pas de nombres ni d'espaces).");
            return;
        }

        if (btnAjouter.getText().equals("Mettre à jour")) {
            int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                showAlert("Attention", "Veuillez sélectionner un programme à modifier !");
                return;
            }

            Models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);
            selectedProgramme.setTitre(titre);
            selectedProgramme.setType(type);
            selectedProgramme.setDescription(description);

            try {
                service.update(selectedProgramme);
                showAlert("Succès", "Le programme a été mis à jour avec succès !");

                loadProgrammes();

                txtTitre.clear();
                cmbType.getSelectionModel().clearSelection();
                txtDescription.clear();
                btnModifier.setDisable(false);

                btnAjouter.setText("Ajouter Programme");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la mise à jour du programme !");
            }
        } else {
            Models.programmebienetre programme = new Models.programmebienetre(0, titre, type, description);
            try {
                service.create(programme);
                emailService.sendEmail("chedlikilani87@gmail.com","ProgrammeBienEtre Créé","Un nouveau programme a été créé : " + titre);

                loadProgrammes();

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

        Models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);

        txtTitre.setText(selectedProgramme.getTitre());
        cmbType.setValue(selectedProgramme.getType());
        txtDescription.setText(selectedProgramme.getDescription());

        btnAjouter.setText("Mettre à jour");
        btnModifier.setDisable(true);
    }

    @FXML
    private void supprimerProgramme() {
        int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner un programme à supprimer !");
            return;
        }

        Models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);
        int id = selectedProgramme.getIdProgramme();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le programme");
        alert.setContentText("Voulez-vous vraiment supprimer \"" + selectedProgramme.getTitre() + "\" ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.delete(id);
                loadProgrammes();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/recompense.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage popupStage = new Stage();
            popupStage.setTitle("Créer une Récompense");
            popupStage.setScene(scene);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            Stage mainStage = (Stage) btnCreateRecompense.getScene().getWindow();
            popupStage.initOwner(mainStage);

            popupStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de récompense !");
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
