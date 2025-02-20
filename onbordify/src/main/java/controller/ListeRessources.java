package controller;

import Model.Favoris;
import Services.FavorisService;
import Services.RessourceService;
import Model.Ressource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private Button btnFavoris; // Added the Favoris button
    @FXML private TextField searchField; // Champ de recherche ajouté

    private RessourceService ressourceService;
    private List<Ressource> ressources = new ArrayList<>();
    private ObservableList<String> titresObservableList; // Liste observable des titres des ressources

    @FXML
    public void initialize() {
        ressourceService = new RessourceService();

        // Charger les ressources dans la ListView
        loadRessources();

        // Action pour ajouter une nouvelle ressource
        btnAjouter.setOnAction(event -> ouvrirAjouterRessource());

        // Action pour ouvrir la liste des favoris
        btnFavoris.setOnAction(event -> {
            System.out.println("Button clicked, trying to load the next scene...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeFavoris.fxml")); // Path for ListeFavoris.fxml
                Parent root = loader.load();

                // Get current stage (window) and change scene
                Stage stage = (Stage) btnFavoris.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur lors de la navigation vers la liste des favoris.");
            }
        });

        // Ajouter un ContextMenu pour consulter une ressource
        ContextMenu contextMenu = new ContextMenu();

        // Option "Consulter"
        MenuItem consulterItem = new MenuItem("Consulter");
        contextMenu.getItems().add(consulterItem);

        // Option "Mettre en favoris"
        MenuItem mettreEnFavorisItem = new MenuItem("Mettre en favoris");
        contextMenu.getItems().add(mettreEnFavorisItem);

        // Gestion de l'événement "Consulter"
        consulterItem.setOnAction(event -> {
            String selectedTitre = listViewRessources.getSelectionModel().getSelectedItem();
            if (selectedTitre != null) {
                Ressource selectedRessource = findRessourceByTitle(selectedTitre);
                if (selectedRessource != null) {
                    ouvrirDetailRessource(selectedRessource);
                } else {
                    showAlert("Aucune ressource trouvée pour ce titre.");
                }
            } else {
                showAlert("Veuillez sélectionner une ressource.");
            }
        });

        // Gestion de l'événement "Mettre en favoris"
        mettreEnFavorisItem.setOnAction(event -> {
            String selectedTitre = listViewRessources.getSelectionModel().getSelectedItem();
            if (selectedTitre != null) {
                Ressource selectedRessource = findRessourceByTitle(selectedTitre);
                if (selectedRessource != null) {
                    ajouterAuxFavoris(selectedRessource);
                } else {
                    showAlert("Aucune ressource trouvée pour ce titre.");
                }
            } else {
                showAlert("Veuillez sélectionner une ressource.");
            }
        });

        // Attacher le ContextMenu à la ListView
        listViewRessources.setContextMenu(contextMenu);

        // Ajouter un listener pour filtrer les ressources lors de la saisie dans le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterRessources(newValue));
    }

    // Méthode pour charger les ressources dans le ListView
    private void loadRessources() {
        try {
            // Récupérer toutes les ressources
            ressources = ressourceService.getAll();
            if (ressources != null) {
                // Créer une liste pour stocker les titres des ressources
                List<String> titres = new ArrayList<>();
                for (Ressource ressource : ressources) {
                    titres.add(ressource.getTitre());  // Ajouter chaque titre
                }
                // Mettre à jour la ListView avec les titres
                titresObservableList = FXCollections.observableArrayList(titres);
                listViewRessources.setItems(titresObservableList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors du chargement des ressources !");
        }
    }

    // Méthode pour filtrer les ressources en fonction du texte de recherche
    private void filterRessources(String searchText) {
        ObservableList<String> filteredRessources = FXCollections.observableArrayList();

        // Si le champ de recherche est vide, on affiche toutes les ressources
        if (searchText.isEmpty()) {
            filteredRessources.addAll(titresObservableList);
        } else {
            for (String titre : titresObservableList) {
                // Si le titre contient le texte de recherche (insensible à la casse), on l'ajoute à la liste filtrée
                if (titre.toLowerCase().contains(searchText.toLowerCase())) {
                    filteredRessources.add(titre);
                }
            }
        }

        // Mettre à jour la ListView avec les ressources filtrées
        listViewRessources.setItems(filteredRessources);
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

    // Méthode pour ouvrir la fenêtre de détails de la ressource
    private void ouvrirDetailRessource(Ressource ressource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/DetailResource.fxml"));
            Parent root = loader.load();

            // Passer la ressource au contrôleur de la fenêtre de détails
            DetailResourceController controller = loader.getController();
            controller.setRessource(ressource);

            // Créer une nouvelle fenêtre (Stage)
            Stage stage = new Stage();
            stage.setTitle("Détails de la Ressource");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ajouter une ressource aux favoris
    private void ajouterAuxFavoris(Ressource ressource) {
        try {
            // Créer un objet Favoris sans l'attribut idResource
            Favoris favoris = new Favoris();

            // Set the title of the Ressource as the commentaire
            favoris.setTitreRessource(ressource.getTitre()); // Assuming 'getTitle()' is the method to get the title of the resource

            favoris.setNote(0); // Optionnel : note par défaut à 0

            // Appel à un service pour sauvegarder le favori
            FavorisService favorisService = new FavorisService();
            favorisService.create(favoris);

            // Afficher une alerte pour indiquer que la ressource a été ajoutée aux favoris
            showAlert("Ressource ajoutée aux favoris !");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'ajout aux favoris.");
        }
    }

    // Méthode pour trouver une ressource par son titre
    private Ressource findRessourceByTitle(String titre) {
        for (Ressource ressource : ressources) {
            if (ressource.getTitre().equals(titre)) {
                return ressource;
            }
        }
        return null;
    }

    // Méthode pour afficher une alerte en cas d'erreur
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        alert.showAndWait();
    }

}
