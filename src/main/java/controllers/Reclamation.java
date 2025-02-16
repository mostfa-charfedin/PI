package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDate;

    @FXML
    private TextArea commentaireField; // Champ de texte pour le commentaire
    @FXML
    private DatePicker datePicker; // Sélecteur de date
    @FXML
    private TableView<Reclamation> reclamationTable; // Tableau pour afficher les réclamations
    @FXML
    private TableColumn<Reclamation, Integer> idColumn;
    @FXML
    private TableColumn<Reclamation, String> commentaireColumn;
    @FXML
    private TableColumn<Reclamation, LocalDate> dateColumn;
    @FXML
    private TableColumn<Reclamation, Boolean> etatColumn;

    private ReclamationService reclamationService = new ReclamationService();

    // Méthode appelée lors de l'initialisation de l'interface
    @FXML
    public void initialize() {
        // Configurer les colonnes du tableau
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idReclamation"));
        commentaireColumn.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Charger les réclamations depuis le service
        chargerReclamations();
    }

    // Méthode pour charger les réclamations dans le tableau
    private void chargerReclamations() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.listerReclamations());
        reclamationTable.setItems(reclamations);
    }

    // Méthode appelée lorsque l'utilisateur clique sur "Soumettre"
    @FXML
    public void soumettreFeedback() {
        String commentaire = commentaireField.getText();
        LocalDate date = datePicker.getValue();

        if (commentaire.isEmpty() || date == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        Reclamation reclamation = new Reclamation();
        reclamation.setCommentaire(commentaire);
        reclamation.setDate(date);
        reclamation.setIdUser(1); // Remplacez par l'ID de l'utilisateur connecté
        reclamation.setEtat(false);

        reclamationService.ajouterReclamation(reclamation);
        showAlert("Succès", "Réclamation ajoutée avec succès !");

        // Réinitialiser le formulaire et recharger les réclamations
        commentaireField.clear();
        datePicker.setValue(null);
        chargerReclamations();
    }

    // Méthode pour marquer une réclamation comme résolue
    @FXML
    public void marquerCommeResolue() {
        Reclamation selected = reclamationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setEtat(true);
            reclamationService.mettreAJourReclamation(selected);
            chargerReclamations(); // Rafraîchir le tableau
        } else {
            showAlert("Erreur", "Veuillez sélectionner une réclamation.");
        }
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
