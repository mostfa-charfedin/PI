package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Services.favoirsService;
import Models.favoirs;

import java.io.IOException;
import java.util.List;

public class FavorisController {

    @FXML
    private ListView<String> favorisListView;

    @FXML
    private Button btnRetour;

    @FXML
    private Button btnFavoris;  // Add this new button

    // Method to load Favoris titles into ListView
    @FXML
    private void initialize() {
        loadFavoris();
    }

    private void loadFavoris() {
        // Initialize the service
        favoirsService favorisService = new favoirsService();

        try {
            // Get all favoris from the database
            List<favoirs> favorisList = favorisService.getAll(); // Call the method on the object

            // Create an ObservableList to hold the titles of the Favoris
            ObservableList<String> favorisTitles = FXCollections.observableArrayList();

            // Loop through the list of Favoris and add the title to the observable list
            for (favoirs favoris : favorisList) {
                favorisTitles.add(favoris.getTitreRessource());
            }

            // Set the items of the ListView to display the titles
            favorisListView.setItems(favorisTitles);

            // Add a ContextMenu to each item in the ListView
            favorisListView.setCellFactory(lv -> {
                javafx.scene.control.ListCell<String> cell = new javafx.scene.control.ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setContextMenu(null);
                        } else {
                            setText(item);

                            // Create ContextMenu for each item
                            ContextMenu contextMenu = new ContextMenu();

                            // Option "Supprimer"
                            MenuItem supprimerItem = new MenuItem("Supprimer");
                            contextMenu.getItems().add(supprimerItem);

                            // Set the ContextMenu for this cell
                            setContextMenu(contextMenu);

                            // Handle "Supprimer" action
                            supprimerItem.setOnAction(event -> handleDeleteFavoris(item));
                        }
                    }
                };
                return cell;
            });

        } catch (Exception e) {
            // Handle the exception, for example, by showing an error alert
            e.printStackTrace(); // Log the exception to the console
            showAlert("Erreur lors du chargement des favoris. Veuillez réessayer."); // Show user-friendly message
        }
    }

    private void showAlert(String message) {
        // Create a new Alert of type ERROR
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null); // No header text
        alert.setContentText(message); // Set the message passed as a parameter

        // Show the alert and wait for the user to close it
        alert.showAndWait();
    }

    // Handle back button click
    @FXML
    private void handleRetour(ActionEvent event) {
        // Code pour naviguer vers la scène précédente (par exemple, la scène principale)
        System.out.println("Retour button clicked");

        try {
            // Charger la scène précédente ou une autre scène
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeRessources.fxml")); // Remplace avec le chemin de ta scène
            Parent root = loader.load();

            // Récupérer le Stage actuel et changer de scène
            Stage currentStage = (Stage) btnRetour.getScene().getWindow();
            currentStage.setScene(new Scene(root));

            // Afficher la nouvelle scène
            currentStage.show();
        } catch (IOException e) {
            // Gestion des erreurs si le fichier FXML n'est pas trouvé
            e.printStackTrace();
            showAlert("Erreur lors du chargement de la scène précédente.");
        }
    }

    private void handleDeleteFavoris(String favorisTitle) {
        // Initialize the service
        favoirsService favorisService = new favoirsService();

        try {
            // Get all favoris and search for the one with the matching title
            List<favoirs> favorisList = favorisService.getAll();
            favoirs favorisToDelete = null;

            // Find the Favoris object that matches the title
            for (favoirs favoris : favorisList) {
                if (favoris.getTitreRessource().equals(favorisTitle)) {
                    favorisToDelete = favoris;
                    break;
                }
            }

            if (favorisToDelete != null) {
                // Call the delete method with the idFavoris
                favorisService.delete(favorisToDelete.getidFavoirs());

                // Remove the item from the ListView
                favorisListView.getItems().remove(favorisTitle);

                // Show a success message
                showAlert("Favori supprimé avec succès.");
            } else {
                showAlert("Favori introuvable.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de la suppression du favori.");
        }
    }
}
