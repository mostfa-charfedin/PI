import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;


    @FXML
    private TextArea messageField; // Champ de texte pour le message du commentaire
    @FXML
    private TableView<Commentaire> commentaireTable; // Tableau pour afficher les commentaires
    @FXML
    private TableColumn<Commentaire, Integer> idColumn;
    @FXML
    private TableColumn<Commentaire, String> messageColumn;

    private CommentaireService commentaireService = new CommentaireService();
    private int idPublication; // ID de la publication associée

    // Méthode pour définir l'ID de la publication
    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
        chargerCommentaires();
    }

    // Méthode pour charger les commentaires dans le tableau
    private void chargerCommentaires() {
        ObservableList<Commentaire> commentaires = FXCollections.observableArrayList(commentaireService.listerCommentaires(idPublication));
        commentaireTable.setItems(commentaires);
    }

    // Méthode appelée lorsque l'utilisateur clique sur "Ajouter"
    @FXML
    public void ajouterCommentaire() {
        String message = messageField.getText();

        if (message.isEmpty()) {
            showAlert("Erreur", "Veuillez remplir le message du commentaire.");
            return;
        }

        Commentaire commentaire = new Commentaire();
        commentaire.setMessage(message);
        commentaire.setIdPublication(idPublication);
        commentaire.setIdUser(1); // Remplacez par l'ID de l'utilisateur connecté

        commentaireService.ajouterCommentaire(commentaire);
        showAlert("Succès", "Commentaire ajouté avec succès !");

        // Réinitialiser le formulaire et recharger les commentaires
        messageField.clear();
        chargerCommentaires();
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
