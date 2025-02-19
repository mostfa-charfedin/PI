package controller;

import Services.RessourceService;
import Model.Ressource;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ListeRessources {

    @FXML private ListView<String> listViewRessources;
    @FXML private Button btnAjouter;

    private RessourceService ressourceService;

    @FXML
    public void initialize() {
        ressourceService = new RessourceService();

        // Charger les ressources dans la ListView
        loadRessources();

        // Action pour ajouter une nouvelle ressource
        btnAjouter.setOnAction(event -> ouvrirAjouterRessource());

        // Action lorsque l'utilisateur sélectionne une ressource dans la ListView
        listViewRessources.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ouvrirModifierRessource(newValue);
            }
        });
    }

    // Méthode pour charger les ressources dans le ListView
    private void loadRessources() {
        try {
            // Récupérer toutes les ressources
            List<Ressource> ressources = ressourceService.getAll();
            if (ressources != null) {
                // Créer une liste pour stocker les titres des ressources
                List<String> titres = new ArrayList<>();
                for (Ressource ressource : ressources) {
                    titres.add(ressource.getTitre());  // Ajouter chaque titre
                }
                // Mettre à jour la ListView avec les titres
                listViewRessources.setItems(FXCollections.observableArrayList(titres));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des ressources !");
        }
    }

    // Méthode pour ouvrir la fenêtre d'ajout de ressource
    private void ouvrirAjouterRessource() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AddResource.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Ajouter Ressource");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ouvrir la fenêtre de modification de ressource
    private void ouvrirModifierRessource(String titre) {
        try {
            // Récupérer la ressource correspondante au titre sélectionné
            Ressource ressource = ressourceService.getAll().stream()
                    .filter(r -> r.getTitre().equals(titre))
                    .findFirst()
                    .orElse(null);

            if (ressource != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModifyResource.fxml"));
                Parent root = loader.load();

                // Récupérer le contrôleur et lui passer la ressource à modifier
                ModifyResourceController controller = loader.getController();
                if (controller == null) {
                    System.out.println("Erreur : contrôleur ModifyResourceController non trouvé !");
                } else {
                    controller.setRessource(ressource);
                }



                // Créer une nouvelle fenêtre (Stage)
                Stage stage = new Stage();
                stage.setTitle("Modifier Ressource");
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert("Ressource non trouvée !");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ouverture de la fenêtre de modification !");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Méthode pour afficher une alerte en cas d'erreur
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
