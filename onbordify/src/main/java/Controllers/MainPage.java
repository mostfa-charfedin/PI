package Controllers;

import Models.Role;
import com.sun.javafx.menu.MenuItemBase;
import utils.UserSession;
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
import Models.Role;
import utils.UserSession;

public class MainPage {


    @FXML
    private StackPane contentPane;  // Zone où afficher les pages

    @FXML
    private Button btnPage1, btnPage2, btnPage3, btnPage4,btnPage6, btnPage5, btnPage7,btnPage61,btnPage62,btnPage65;  // Boutons du menu latéral
public UserSession session = UserSession.getInstance();
    @FXML
    public void initialize() {
        UserSession session = UserSession.getInstance();
        Role roleSession = session.getRole();
        // Charger la page d'accueil par défaut
        loadPage("/fxml/GestionUser.fxml");

        // Gestion des clics sur les boutons

        btnPage1.setOnAction(e -> loadPage("/fxml/Profile.fxml"));
        if (roleSession != null && roleSession.equals(Role.ADMIN)) {
            btnPage2.setVisible(true);
            btnPage3.setVisible(true);
            btnPage65.setVisible(true);

            btnPage2.setOnAction(e -> loadPage("/fxml/Score.fxml"));
            btnPage3.setOnAction(e -> loadPage("/fxml/GestionUser.fxml"));

        } else {
            btnPage2.setVisible(false);
            btnPage3.setVisible(false);
            btnPage65.setVisible(false);
        }

        if (roleSession == Role.ADMIN) {
            btnPage4.setOnAction(e -> loadPage("/projectvue.fxml"));
        } else {
            btnPage4.setOnAction(e -> loadPage("/userprojectvue.fxml"));
        }
        if (roleSession == Role.ADMIN) {
            btnPage5.setOnAction(e -> loadPage("/programmebienetre.fxml"));
        } else {
            btnPage5.setOnAction(e -> loadPage("/Avis.fxml"));
        }
        if (roleSession == Role.ADMIN) {
            btnPage65.setOnAction(e -> loadPage("/statisticAvis.fxml"));
        }

        if (roleSession == Role.ADMIN) {
            btnPage6.setOnAction(e -> loadPage("/views/ListQuiz.fxml"));
        } else {
            btnPage6.setOnAction(e -> loadPage("/views/QuizEmployee.fxml"));
        }
        if (roleSession == Role.ADMIN) {
            btnPage7.setOnAction(e -> loadPage("/views/evaluation.fxml"));
        } else {
            btnPage7.setOnAction(e -> loadPage("/views/ListeRessources.fxml"));
        }
        if (roleSession == Role.ADMIN) {
            btnPage61.setOnAction(e -> loadPage("/listreclamation.fxml"));
        } else {
            btnPage61.setOnAction(e -> loadPage("/reclamation.fxml"));
        }
        btnPage62.setOnAction(e -> loadPage("/display_posts.fxml"));

    }

    private void showAccessDenied() {
        System.out.println("Accès refusé : Vous devez être administrateur pour accéder à cette page.");

    }



    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newPage = loader.load();

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
