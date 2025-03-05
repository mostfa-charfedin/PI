package Controllers;

import Models.Role;
import utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.io.IOException;
import Models.Role;
import utils.UserSession;

public class MainPage {

    @FXML
    private StackPane contentPane;  // Zone où afficher les pages

    @FXML
    private Button btnPage1, btnPage2, btnPage3, btnPage4,btnPage6;  // Boutons du menu latéral

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
        if (roleSession == Role.ADMIN) {
            btnPage4.setOnAction(e -> loadPage("/projectvue.fxml"));
        } else {
            btnPage4.setOnAction(e -> loadPage("/userprojectvue.fxml"));
        }

        if (roleSession == Role.ADMIN) {
            btnPage6.setOnAction(e -> loadPage("/views/ListQuiz.fxml"));
        } else {
            btnPage6.setOnAction(e -> loadPage("/views/QuizEmployee.fxml"));
        }
    }
    private void showAccessDenied() {
        System.out.println("Accès refusé : Vous devez être administrateur pour accéder à cette page.");

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
