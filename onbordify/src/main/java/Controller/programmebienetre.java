package Controller;

import Services.programmebienetreService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class programmebienetre {
    @FXML
    private TextField txtTitre;
    @FXML
    private ComboBox<String> cmbType;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ListView<String> listViewProgrammes;
    @FXML
    private Button btnAjouter;  // Déclaration du Button

    private programmebienetreService service = new programmebienetreService();

    @FXML
    public void initialize() {
        // Charger les programmes dans la ListView
        try {
            List<models.programmebienetre> programmes = service.getAll();
            for (models.programmebienetre p : programmes) {
                listViewProgrammes.getItems().add(p.getTitre() + " - " + p.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Associer l'action du Button
        btnAjouter.setOnAction(event -> ajouterProgramme());
    }



    @FXML
    public void ajouterProgramme() {
        String titre = txtTitre.getText();
        String type = cmbType.getValue();
        String description = txtDescription.getText();

        // Vérifier que tous les champs sont remplis
        if (titre.isEmpty() || type == null || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }

        models.programmebienetre programme = new models.programmebienetre(0, titre, type, description);
        try {
            service.create(programme);
            listViewProgrammes.getItems().add(titre + " - " + description);

            // Réinitialiser les champs après ajout
            txtTitre.clear();
            cmbType.setValue(null);
            txtDescription.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


