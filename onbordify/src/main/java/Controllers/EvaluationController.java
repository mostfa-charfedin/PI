package Controllers;

import Models.Ressource;
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
import utils.UserSession;

import java.util.List;

public class EvaluationController {

    // TableView pour les meilleures ressources
    @FXML
    private TableView<Ressource> bestRatedTable;
    @FXML
    private TableColumn<Ressource, String> bestRatedTitleColumn;
    @FXML
    private TableColumn<Ressource, Double> bestRatedNoteColumn;

    // TableView pour les ressources à améliorer
    @FXML
    private TableView<Ressource> resourcesToImproveTable;
    @FXML
    private TableColumn<Ressource, String> resourcesToImproveTitleColumn;
    @FXML
    private TableColumn<Ressource, Double> resourcesToImproveNoteColumn;

    // TableView générale des ressources
    @FXML
    private TableView<Ressource> resourcesTable;
    @FXML
    private TableColumn<Ressource, String> titreColumn;
    @FXML
    private TableColumn<Ressource, Double> noteColumn;
    @FXML
    private TableColumn<Ressource, String> satisfactionColumn;

    // BarChart pour les notes
    @FXML
    private BarChart<String, Number> resourcesBarChart;
    @FXML
    private CategoryAxis barChartXAxis;
    @FXML
    private NumberAxis barChartYAxis;

    // Filtres
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

    // Service
    private final RessourceService ressourceService;

    public EvaluationController() {
        this.ressourceService = new RessourceService();
    }

    @FXML
    public void initialize() {
        // Initialiser colonnes des tableaux
        bestRatedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        bestRatedNoteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));

        resourcesToImproveTitleColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        resourcesToImproveNoteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));

        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("noteAverage"));
        satisfactionColumn.setCellValueFactory(new PropertyValueFactory<>("satisfaction"));

        // Charger les données
        loadResourcesWithAverageNote();
        loadResourcesToImprove();
        loadResourcesForTable();
        populateBarChartWithResourceData();

        // Optionnel : initialiser ComboBoxes si nécessaire
    }

    private void loadResourcesWithAverageNote() {
        try {
            List<Ressource> resources = ressourceService.getResourcesWithHighAverageNote();
            ObservableList<Ressource> observableList = FXCollections.observableArrayList(resources);
            bestRatedTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadResourcesToImprove() {
        try {
            List<Ressource> resourcesToImprove = ressourceService.getResourcesWithLowAverageNote();
            ObservableList<Ressource> observableList = FXCollections.observableArrayList(resourcesToImprove);
            resourcesToImproveTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadResourcesForTable() {
        try {
            List<Ressource> resources = ressourceService.getAll();
            ObservableList<Ressource> observableList = FXCollections.observableArrayList();

            for (Ressource ressource : resources) {
                double note = ressourceService.getNoteFromEvaluation(ressource.getIdResource());
                ressource.setNoteAverage(note);
                observableList.add(ressource);
            }

            resourcesTable.setItems(observableList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateBarChartWithResourceData() {
        try {
            List<Ressource> resources = ressourceService.getAll();
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Moyenne des notes");

            for (Ressource ressource : resources) {
                double note = ressourceService.getNoteFromEvaluation(ressource.getIdResource());
                int count = ressourceService.getEvaluationCount(ressource.getIdResource());

                String label = ressource.getTitre() + " (" + count + " vote" + (count > 1 ? "s" : "") + ")";
                ressource.setNoteAverage(note);
                series.getData().add(new XYChart.Data<>(label, note));
            }

            resourcesBarChart.getData().clear();
            resourcesBarChart.getData().add(series);

            barChartXAxis.setLabel("Ressource (nombre de votes)");
            barChartYAxis.setLabel("Note Moyenne");
            barChartYAxis.setAutoRanging(false);
            barChartYAxis.setLowerBound(0);
            barChartYAxis.setUpperBound(5);
            barChartYAxis.setTickUnit(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
