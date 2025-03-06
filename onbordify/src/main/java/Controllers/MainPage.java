package Controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import utils.UserSession;

import java.io.IOException;

public class MainPage {

    @FXML
    private StackPane contentPane;  // Zone où afficher les pages

    @FXML
    private Button btnPage1, btnPage2, btnPage3
            , btnPage7;  // Boutons du menu latéral
    UserSession session = UserSession.getInstance();
    @FXML
    public void initialize() {
        // Charger la page d'accueil par défaut
        loadPage("/fxml/GestionUser.fxml");

        // Gestion des clics sur les boutons
        btnPage1.setOnAction(e -> loadPage("/fxml/Profile.fxml"));
        btnPage2.setOnAction(e -> loadPage("/fxml/Score.fxml"));
        btnPage3.setOnAction(e -> loadPage("/fxml/GestionUser.fxml"));
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane newPage = loader.load();

            // Ajuster la taille pour qu'elle prenne tout l'espace disponible
            AnchorPane.setTopAnchor(newPage, 0.0);
            AnchorPane.setBottomAnchor(newPage, 0.0);
            AnchorPane.setLeftAnchor(newPage, 0.0);
            AnchorPane.setRightAnchor(newPage, 0.0);
            // Animation de transition fluide
            FadeTransition fade = new FadeTransition(Duration.millis(500), newPage);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            // Remplacer le contenu actuel
            contentPane.getChildren().setAll(newPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout()throws IOException{
        session.destroySession();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));

        try {
            Parent root = loader.load();
            btnPage1.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
