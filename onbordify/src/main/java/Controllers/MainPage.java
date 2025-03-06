package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import java.io.IOException;
import utils.UserSession;

public class MainPage {

    @FXML
    private StackPane contentPane;  // Zone où afficher les pages

    @FXML
    private Button btnPage1, btnPage2, btnPage3, btnPage4, btnPage5, btnPage6, btnPage7, btnPage8, btnPage9;  // Boutons du menu latéral

    @FXML
    public void initialize() {
        // Charger la page d'accueil par défaut
        loadPage("/fxml/GestionUser.fxml");

        // Check user role and adjust visibility of the button
        UserSession session = UserSession.getInstance();
        if (session != null && session.getRole() != null) {
            System.out.println("User Role: " + session.getRole()); // Print user role
            if (session.getRole().toString().equals("ADMIN")) {
                btnPage8.setVisible(true); // Show the button if user is ADMIN
            } else {
                btnPage8.setVisible(false); // Hide the button if user is not ADMIN
            }
        } else {
            btnPage8.setVisible(false); // Hide if session is null
        }

        // Gestion des clics sur les boutons
        btnPage1.setOnAction(e -> loadPage("/fxml/Profile.fxml"));
        btnPage2.setOnAction(e -> loadPage("/fxml/Score.fxml"));
        btnPage3.setOnAction(e -> loadPage("/fxml/GestionUser.fxml"));
        btnPage4.setOnAction(e -> loadPage(""));
        btnPage5.setOnAction(e -> loadPage(""));
        btnPage6.setOnAction(e -> loadPage(""));
        btnPage7.setOnAction(e -> loadPage("/reclamation.fxml"));
        btnPage8.setOnAction(e -> loadPage("/listreclamation.fxml"));
        btnPage9.setOnAction(e -> loadPage("/display_posts.fxml"));
    }

    private boolean isUserAdmin() {
        UserSession session = UserSession.getInstance();
        return session != null && session.getRole() != null && session.getRole().toString().equals("ADMIN");

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