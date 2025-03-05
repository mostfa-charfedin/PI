package controller;

import Model.Ressource;
import Services.RessourceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

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




    // Service pour obtenir les ressources avec leur note moyenne
    private RessourceService ressourceService;

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

        // Configurer les colonnes du TableView pour les ressources à améliorer
        resourcesToImproveTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        resourcesToImproveNoteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));

        // Configurer les colonnes du TableView pour la table des ressources
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));
        satisfactionColumn.setCellValueFactory(new PropertyValueFactory<>("satisfaction"));

        // Charger les ressources dans les TableView
        loadResourcesWithAverageNote();
        loadResourcesToImprove();
        loadResourcesForTable();
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

            // Convertir la liste en ObservableList pour lier au TableView
            ObservableList<Ressource> observableList = FXCollections.observableArrayList(resources);

            // Associer l'ObservableList au TableView
            resourcesTable.setItems(observableList);

        } catch (Exception e) {
            e.printStackTrace();
            // Ici, tu peux afficher un message d'erreur si quelque chose se passe mal
        }
    }


}
