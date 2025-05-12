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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import Services.UserService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class programmebienetre implements Initializable {


    @FXML
    private TextField txtTitre;

    @FXML
    private ComboBox<String> cmbType;

    @FXML
    private TextArea txtDescription;

    @FXML
    private DatePicker dateProgramme;

    @FXML
    private ListView<String> listViewProgrammes;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnCreateRecompense;

    @FXML
    private ComboBox<Models.User> cmbManager;

    private programmebienetreService service = new programmebienetreService();
    private EmailService emailService = new EmailService(); // Initialisation de EmailService
    private UserService userService = new UserService(); // Initialisation de UserService
    private ObservableList<Models.programmebienetre> programmeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbType.getItems().clear();
        cmbType.getItems().addAll("Physical", "Mental", "Social", "Professional");

        // Load only admin users into cmbManager
        try {
            List<Models.User> users = userService.getAll();
            cmbManager.getItems().clear();
            for (Models.User user : users) {
                if (user.getRole() == Models.Role.ADMIN) {
                    cmbManager.getItems().add(user);
                }
            }
            cmbManager.setCellFactory(param -> new ListCell<Models.User>() {
                @Override
                protected void updateItem(Models.User user, boolean empty) {
                    super.updateItem(user, empty);
                    setText((user == null || empty) ? null : user.getNom() + " " + user.getPrenom());
                }
            });
            cmbManager.setButtonCell(new ListCell<Models.User>() {
                @Override
                protected void updateItem(Models.User user, boolean empty) {
                    super.updateItem(user, empty);
                    setText((user == null || empty) ? null : user.getNom() + " " + user.getPrenom());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        String titre = txtTitre.getText();
        String type = cmbType.getValue();
        String description = txtDescription.getText();
        LocalDate date = dateProgramme.getValue();
        String dateStr = date != null ? date.format(DateTimeFormatter.ISO_DATE) : "";
        Models.User selectedManager = cmbManager.getValue();

        if (titre.isEmpty() || type == null || description.isEmpty() || date == null || selectedManager == null) {
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
            selectedProgramme.setDate_programme(dateStr);
            selectedProgramme.setNom(selectedManager.getNom());
            selectedProgramme.setPrenom(selectedManager.getPrenom());

            try {
                service.update(selectedProgramme);
                showAlert("Succès", "Le programme a été mis à jour avec succès !");

                loadProgrammes();

                txtTitre.clear();
                cmbType.getSelectionModel().clearSelection();
                txtDescription.clear();
                dateProgramme.setValue(null);
                cmbManager.getSelectionModel().clearSelection();
                btnModifier.setDisable(false);

                btnAjouter.setText("Ajouter Programme");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la mise à jour du programme !");
            }
        } else {
            int idUser = selectedManager.getId();
            Models.programmebienetre programme = new Models.programmebienetre(0, titre, type, description, dateStr, idUser);
            programme.setNom(selectedManager.getNom());
            programme.setPrenom(selectedManager.getPrenom());
            try {
                service.create(programme);
                List<String> allEmails = userService.getAllUserEmails();
                for (String email : allEmails) {
                    emailService.sendEmail(email, "ProgWellBeing Created", "A new ProgWellBeing has been created, you can check it now!");
                }

                loadProgrammes();

                txtTitre.clear();
                cmbType.getSelectionModel().clearSelection();
                txtDescription.clear();
                dateProgramme.setValue(null);
                cmbManager.getSelectionModel().clearSelection();

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
        if (selectedProgramme.getDate_programme() != null && !selectedProgramme.getDate_programme().isEmpty()) {
            dateProgramme.setValue(LocalDate.parse(selectedProgramme.getDate_programme()));
        }
        // Set the selected manager in the ComboBox by matching id
        for (Models.User user : cmbManager.getItems()) {
            if (user.getNom().equals(selectedProgramme.getNom()) && user.getPrenom().equals(selectedProgramme.getPrenom())) {
                cmbManager.setValue(user);
                break;
            }
        }

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
        int selectedIndex = listViewProgrammes.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            showAlert("Attention", "Veuillez sélectionner un programme pour créer une récompense !");
            return;
        }

        Models.programmebienetre selectedProgramme = programmeList.get(selectedIndex);

        // Vérifier si le programme a déjà une récompense
        try {
            String sql = "SELECT COUNT(*) FROM recompense WHERE idProgramme = ?";
            try (PreparedStatement stmt = service.getConnection().prepareStatement(sql)) {
                stmt.setInt(1, selectedProgramme.getIdProgramme());
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    showAlert("Attention", "Ce programme a déjà une récompense associée !");
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la vérification des récompenses !");
            return;
        }

        // Si le programme n'a pas de récompense, ouvrir la fenêtre de création
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Recompense.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur de la fenêtre de récompense
            Controller.Recompense recompenseController = loader.getController();

            // Configurer la récompense avec le programme sélectionné
            recompenseController.setProgrammeId(selectedProgramme.getIdProgramme());

            // Créer et afficher la fenêtre
            Stage stage = new Stage();
            stage.setTitle("Créer une récompense pour " + selectedProgramme.getTitre());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Recharger la liste des programmes après la création
            loadProgrammes();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la fenêtre de récompense !");
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