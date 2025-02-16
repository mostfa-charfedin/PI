import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

    @FXML
    private TextArea bodyField; // Champ de texte pour le corps de la publication
    @FXML
    private TableView<Publication> publicationTable; // Tableau pour afficher les publications
    @FXML
    private TableColumn<Publication, Integer> idColumn;
    @FXML
    private TableColumn<Publication, String> bodyColumn;

    private PublicationService publicationService = new PublicationService();

    // Méthode appelée lors de l'initialisation de l'interface
    @FXML
    public void initialize() {
        // Configurer les colonnes du tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPublication"));
        bodyColumn.setCellValueFactory(new PropertyValueFactory<>("body"));

        // Charger les publications depuis le service
        chargerPublications();
    }

    // Méthode pour charger les publications dans le tableau
    private void chargerPublications() {
        ObservableList<Publication> publications = FXCollections.observableArrayList(publicationService.listerPublications());
        publicationTable.setItems(publications);
    }

    // Méthode appelée lorsque l'utilisateur clique sur "Ajouter"
    @FXML
    public void ajouterPublication() {
        String body = bodyField.getText();

        if (body.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir le corps de la publication.");
            return;
        }

        Publication publication = new Publication();
        publication.setBody(body);
        publication.setIdUser(1); // Remplacez par l'ID de l'utilisateur connecté

        publicationService.ajouterPublication(publication);
        showAlert("Succès", "Publication ajoutée avec succès !");

        // Réinitialiser le formulaire et recharger les publications
        bodyField.clear();
        chargerPublications();
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}