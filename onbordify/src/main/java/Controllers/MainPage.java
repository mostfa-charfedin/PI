package Controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.io.IOException;
import utils.UserSession ;
import Models.Role ;


public class MainPage {

    @FXML
    private StackPane contentPane;  // Zone où afficher les pages

    @FXML
    private Button btnPage1, btnPage2, btnPage3
            , btnPage7 , btnPage8;  // Boutons du menu latéral

    @FXML
    public void initialize() {
        // Charger la page d'accueil par défaut
        loadPage("/fxml/GestionUser.fxml");
        UserSession session = UserSession.getInstance();
        Role roleSession = session.getRole();

        // Gestion des clics sur les boutons
        btnPage1.setOnAction(e -> loadPage("/fxml/Profile.fxml"));
        btnPage2.setOnAction(e -> loadPage("/fxml/Score.fxml"));
        btnPage3.setOnAction(e -> loadPage("/fxml/GestionUser.fxml"));

        btnPage7.setOnAction(e -> loadPage("/fxml/ModuleFormation.fxml"));
        if (roleSession == Role.ADMIN) {
            btnPage8.setOnAction(e -> loadPage("/views/evaluation.fxml"));
        } else {
            btnPage8.setOnAction(e -> loadPage("/views/ListeRessources.fxml"));
        }

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
}
