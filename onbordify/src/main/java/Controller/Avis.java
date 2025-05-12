package Controller;
import Models.programmebienetre;
import Services.programmebienetreService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.UserSession;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class Avis implements Initializable {

    @FXML
    private ListView<HBox> listViewProgrammes;

    private final programmebienetreService service = new programmebienetreService();
    private final ObservableList<programmebienetre> programmeList = FXCollections.observableArrayList();
    private int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserSession session = UserSession.getInstance();
        userId = session.getUserId();

        loadProgrammes();
    }


    private void loadProgrammes() {
        try {
            programmeList.setAll(service.getAll());
            listViewProgrammes.setItems(FXCollections.observableArrayList());

            for (programmebienetre programme : programmeList) {
                HBox programmeItem = createProgrammeItem(programme);
                listViewProgrammes.getItems().add(programmeItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les programmes : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private HBox createProgrammeItem(programmebienetre programme) throws SQLException {
        Label lblTitre = new Label(programme.getTitre());
        lblTitre.setStyle("-fx-font-size: 18px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");

        Label lblDescription = new Label(programme.getDescription());
        lblDescription.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");

        // Afficher la moyenne des notes
        double averageRating = service.getAverageRating(programme.getIdProgramme());
        Label lblRating = new Label(String.format("Note moyenne: %.1f/5", averageRating));
        lblRating.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");



        Button btnAvis = new Button("Laisser un Avis");
        btnAvis.setOnAction(event -> laisserAvis(programme));
        btnAvis.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8px;");

        HBox hbox = new HBox(25, lblTitre, lblDescription, lblRating, btnAvis);
        hbox.setStyle("-fx-padding: 15; -fx-border-color: #D5DBDB; -fx-background-color: white; -fx-border-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        return hbox;
    }

    private void laisserAvis(programmebienetre programme) {
        // Créer une nouvelle fenêtre (Stage) pour la pop-up
        Stage popupStage = new Stage();
        popupStage.setTitle("Laisser un Avis");
        popupStage.initModality(Modality.APPLICATION_MODAL);

        // Conteneur principal pour la pop-up
        VBox content = new VBox(15);
        content.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 15;");

        // Label pour choisir une note
        Label lblRating = new Label("Choisissez une note :");
        lblRating.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        // Conteneur pour les étoiles
        HBox starContainer = new HBox(10);
        ImageView[] stars = new ImageView[5];
        final int[] selectedRating = {0};

        for (int i = 0; i < stars.length; i++) {
            final int rating = i + 1;
            stars[i] = new ImageView(new Image(getClass().getResource("/assets/star_empty.png").toExternalForm()));
            stars[i].setFitHeight(32);
            stars[i].setFitWidth(32);

            // Gestion des événements de souris pour plus de réactivité
            stars[i].setOnMouseEntered(event -> {
                // Prévisualisation des étoiles
                for (int j = 0; j < rating; j++) {
                    stars[j].setImage(new Image(getClass().getResource("/assets/star_filled.png").toExternalForm()));
                }
                for (int j = rating; j < stars.length; j++) {
                    stars[j].setImage(new Image(getClass().getResource("/assets/star_empty.png").toExternalForm()));
                }
            });

            stars[i].setOnMouseExited(event -> {
                // Restaurer l'état précédent
                for (int j = 0; j < stars.length; j++) {
                    stars[j].setImage(new Image(getClass().getResource("/assets/star_empty.png").toExternalForm()));
                }
                // Remplir les étoiles correspondant à la note sélectionnée
                for (int j = 0; j < selectedRating[0]; j++) {
                    stars[j].setImage(new Image(getClass().getResource("/assets/star_filled.png").toExternalForm()));
                }
            });

            stars[i].setOnMouseClicked(event -> {
                // Sélection définitive de la note
                selectedRating[0] = rating;
                for (int j = 0; j < stars.length; j++) {
                    if (j < rating) {
                        stars[j].setImage(new Image(getClass().getResource("/assets/star_filled.png").toExternalForm()));
                    } else {
                        stars[j].setImage(new Image(getClass().getResource("/assets/star_empty.png").toExternalForm()));
                    }
                }
            });

            starContainer.getChildren().add(stars[i]);
        }
        // Label pour le commentaire
        Label lblCommentaire = new Label("Laissez un commentaire :");
        lblCommentaire.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        // Zone de texte pour le commentaire
        TextArea commentaireField = new TextArea();
        commentaireField.setPromptText("Votre commentaire...");
        commentaireField.setStyle("-fx-font-size: 14px; -fx-pref-width: 300px; -fx-pref-height: 100px;");

        // Bouton pour soumettre l'avis
        Button btnSubmit = new Button("Soumettre Avis");
        btnSubmit.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 5px;");
        btnSubmit.setOnAction(e -> {
            int rating = selectedRating[0];
            String commentaire = commentaireField.getText();

            if (rating > 0 && !commentaire.isEmpty()) {
                try {
                    soumettreAvis(programme, rating, commentaire);
                    popupStage.close(); // Fermer la pop-up après la soumission
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("Erreur", "Impossible d'ajouter l'avis : " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner une note et écrire un commentaire.", Alert.AlertType.ERROR);
            }
        });

        // Ajouter les éléments au conteneur
        content.getChildren().addAll(lblRating, starContainer, lblCommentaire, commentaireField, btnSubmit);

        // Créer une scène pour la pop-up
        Scene scene = new Scene(content);
        popupStage.setScene(scene);

        // Afficher la pop-up
        popupStage.showAndWait(); // Bloquer l'interaction avec la fenêtre principale jusqu'à la fermeture de la pop-up
    }

    private void soumettreAvis(programmebienetre programme, int rating, String commentaire) {
        try {
            if (programme.getIdProgramme() == 0 || userId == 0) {
                showAlert("Erreur", "Programme ou utilisateur invalide.", Alert.AlertType.ERROR);
                return;
            }
            service.ajouterAvis(programme.getIdProgramme(), userId, rating, commentaire);
            Platform.runLater(() -> {
                showAlert("Avis ajouté", "Votre avis a été enregistré.", Alert.AlertType.INFORMATION);
                loadProgrammes(); // Recharger les programmes dans le thread JavaFX
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            Platform.runLater(() -> {
                showAlert("Erreur", ex.getMessage(), Alert.AlertType.ERROR);
            });
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}