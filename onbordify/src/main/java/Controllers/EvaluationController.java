package Controllers;

import java.io.IOException;
import java.util.List;

import Models.Ressource;
import Services.RessourceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import utils.UserSession;

public class EvaluationController {

    // Lier les éléments FXML pour les ressources les mieux notées
    @FXML
    private TableView<Ressource> bestRatedTable;

    @FXML
    private TableColumn<Ressource, String> bestRatedTitleColumn;

    @FXML
    private TableColumn<Ressource, Double> bestRatedNoteColumn;

    // Lier les éléments FXML pour les ressources à améliorer
    @FXML
    private TableView<Ressource> resourcesToImproveTable;

    @FXML
    private TableColumn<Ressource, String> resourcesToImproveTitleColumn;

    @FXML
    private TableColumn<Ressource, Double> resourcesToImproveNoteColumn;

    // Lier les éléments FXML pour la table des ressources (resourcesTable)
    @FXML
    private TableView<Ressource> resourcesTable;

    @FXML
    private TableColumn<Ressource, String> titreColumn;

    @FXML
    private TableColumn<Ressource, Double> noteColumn;

    @FXML
    private TableColumn<Ressource, String> satisfactionColumn;
    // Lier les éléments FXML pour le BarChart
    @FXML
    private BarChart<String, Number> evaluationChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private DatePicker datePickerStart;

    @FXML
    private DatePicker datePickerEnd;

    @FXML
    private ComboBox<Double> minRatingComboBox;

    @FXML
    private Button applyButton;

    @FXML
    private Button resetButton;

    @FXML
    private MenuItem ouvrirMenuItem;

    @FXML
    private Label averageScoreLabel;
    @FXML
    private Label highestScoreLabel;
    @FXML
    private Label lowestScoreLabel;
    @FXML
    private Label totalResourcesLabel;

    // Service pour obtenir les ressources avec leur note moyenne
    private RessourceService ressourceService;
    private PieChart ratingDistributionChart; // Déclarez votre PieChart ici

    // Constructeur
    public EvaluationController() {
        this.ressourceService = new RessourceService();  // Initialisation du service qui va gérer la logique métier
    }

    // Méthode d'initialisation de la vue (lorsque le fichier FXML est chargé)
    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView pour les meilleures ressources
        bestRatedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        bestRatedNoteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));
        // Configurer la colonne "Rate" dans le TableView pour afficher la note de 'evaluation'
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));

        // Configurer les colonnes du TableView pour les ressources à améliorer
        resourcesToImproveTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        resourcesToImproveNoteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));

        // Configurer les colonnes du TableView pour la table des ressources
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));
        satisfactionColumn.setCellValueFactory(new PropertyValueFactory<>("satisfaction"));
        UserSession session = UserSession.getInstance();

        // Charger les ressources dans les TableView
        loadResourcesWithAverageNote();
        loadResourcesToImprove();
        loadResourcesForTable();

        // Mettre à jour les statistiques dynamiques
        updateStatisticsSummary();

        // Gestion du clic sur "Ouvrir"
        ouvrirMenuItem.setOnAction(event -> {
            System.out.println("Ouvrir clicked, loading ListeRessources.fxml...");
            try {
                // Chargement de la vue cible [[7]]
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ListeRessources.fxml"));
                Parent root = loader.load();

                // Navigation vers la nouvelle scène [[2]][[8]]
                Stage stage = (Stage) ouvrirMenuItem.getParentPopup().getOwnerWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Erreur lors du chargement de la liste des ressources.");
            }
        });
    }

    // Méthode utilitaire pour afficher des alertes [[6]]
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour charger les ressources avec leur note moyenne (Top Ressources)
    private void loadResourcesWithAverageNote() {
        try {
            // Appeler le service pour récupérer les ressources avec leur note moyenne
            List<Ressource> resources = ressourceService.getResourcesWithHighAverageNote();

            // Convertir la liste en ObservableList pour lier au TableView
            ObservableList<Ressource> observableList = FXCollections.observableArrayList(resources);

            // Associer l'ObservableList au TableView
            bestRatedTable.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
            // Ici, tu peux afficher un message d'erreur si quelque chose se passe mal
        }
    }

    // Méthode pour charger les ressources avec les plus mauvaises notes (Ressources à améliorer)
    private void loadResourcesToImprove() {
        try {
            // Appeler le service pour récupérer les ressources avec les plus mauvaises notes
            List<Ressource> resourcesToImprove = ressourceService.getResourcesWithLowAverageNote();

            // Convertir la liste en ObservableList pour lier au TableView
            ObservableList<Ressource> observableList = FXCollections.observableArrayList(resourcesToImprove);

            // Associer l'ObservableList au TableView
            resourcesToImproveTable.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
            // Ici, tu peux afficher un message d'erreur si quelque chose se passe mal
        }
    }

    // Méthode pour charger les ressources dans le tableau "resourcesTable"
    private void loadResourcesForTable() {
        try {
            // Appeler le service pour récupérer toutes les ressources
            List<Ressource> resources = ressourceService.getAll();

            // Créer une liste d'ObservableList pour lier au TableView
            ObservableList<Ressource> observableList = FXCollections.observableArrayList();

            // Pour chaque ressource, récupérer sa note dans la table 'evaluation'
            for (Ressource ressource : resources) {
                double note = ressourceService.getNoteFromEvaluation(ressource.getIdResource());  // Ajoutez cette méthode pour récupérer la note de 'evaluation'
                ressource.setNoteAverage(note); // Mettre à jour l'objet Ressource avec la note obtenue
                observableList.add(ressource);
            }

            // Associer l'ObservableList au TableView
            resourcesTable.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
            // Ici, tu peux afficher un message d'erreur si quelque chose se passe mal
        }
    }

    private void updateStatisticsSummary() {
        try {
            List<Ressource> allEvaluated = ressourceService.getResourcesWithAverageNote();
            // Filter only evaluated resources (avgNote > 0)
            List<Ressource> evaluated = allEvaluated.stream()
                    .filter(r -> r.getNoteAverage() > 0)
                    .toList();
            int total = evaluated.size();
            double sum = 0;
            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            for (Ressource res : evaluated) {
                double note = res.getNoteAverage();
                sum += note;
                if (note < min) min = note;
                if (note > max) max = note;
            }
            double avg = total > 0 ? sum / total : 0;
            averageScoreLabel.setText(total > 0 ? String.format("%.1f/10", avg) : "-");
            highestScoreLabel.setText(total > 0 ? String.format("%.1f/10", max) : "-");
            lowestScoreLabel.setText(total > 0 ? String.format("%.1f/10", min) : "-");
            totalResourcesLabel.setText(String.valueOf(total));
        } catch (Exception e) {
            averageScoreLabel.setText("-");
            highestScoreLabel.setText("-");
            lowestScoreLabel.setText("-");
            totalResourcesLabel.setText("-");
        }
    }
}
