package service;

import javafx.scene.control.Alert;
import utils.configuration;

public class SpamService {

    // Méthode pour vérifier si la réclamation contient des mots interdits
    public static boolean contientMotsInterdits(String texte) {
        for (String mot : configuration.MOTS_INTERDITS) {
            if (texte.toLowerCase().contains(mot.toLowerCase())) {  // Vérification insensible à la casse
                return true;
            }
        }
        return false;
    }

    // Méthode pour traiter l'envoi de la réclamation
    public boolean traiterReclamation(String texteReclamation) {
        if (contientMotsInterdits(texteReclamation)) {
            afficherAlerte("Harmful Message", "Votre réclamation contient des mots inappropriés !");
            return true;
        } else {
            afficherAlerte("Succès", "Réclamation envoyée avec succès !");
            return false;
            // Ici, tu peux ajouter le code pour envoyer la réclamation dans la base de données
        }
    }

    // Méthode pour afficher une alerte (message pop-up)
    private static void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}