package Controller;
import Models.programmebienetre;
import Services.programmebienetreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class statisticAvis implements Initializable{
    @FXML
    private VBox chartContainer;

    @FXML
    private Label totalReviewsLabel;

    @FXML
    private Label averageRatingLabel;

    private programmebienetreService service = new programmebienetreService();
    private programmebienetre currentProgramme = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("StatisticAvis est initialisé!");
        try {
            // Charger les statistiques globales par défaut
            loadStatistics();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement des statistiques : " + e.getMessage());
        }
    }

    public void loadStatistics() throws Exception {
        // Récupérer les programmes
        List<programmebienetre> programmes = service.getAll();
        System.out.println("Nombre de programmes : " + programmes.size());

        // Créer un graphique à barres pour les notes moyennes par programme
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 5, 1);
        yAxis.setLabel("Note Moyenne");
        xAxis.setLabel("Programmes");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Notes Moyennes par Programme");

        // Série de données
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Notes Moyennes");

        // Variables pour les statistiques globales
        double totalAverageRating = 0;
        int totalReviews = 0;
        int validProgrammesCount = 0;

        // Collecter les statistiques pour chaque programme
        for (programmebienetre programme : programmes) {
            int programReviews = service.getTotalReviews(programme.getIdProgramme());
            System.out.println("Programme : " + programme.getTitre() + ", Nombre d'avis : " + programReviews);

            // Ne traiter que les programmes avec des avis
            if (programReviews > 0) {
                double averageRating = service.getAverageRating(programme.getIdProgramme());
                System.out.println("Note moyenne : " + averageRating);

                // Ajouter au graphique à barres
                series.getData().add(new XYChart.Data<>(programme.getTitre(), averageRating));

                // Calculer les totaux
                totalAverageRating += averageRating;
                totalReviews += programReviews;
                validProgrammesCount++;
            }
        }

        // Calculer la moyenne globale
        if (validProgrammesCount > 0) {
            totalAverageRating /= validProgrammesCount;

            // Mettre à jour les labels
            totalReviewsLabel.setText("Nombre total d'avis : " + totalReviews);
            averageRatingLabel.setText(String.format("Note moyenne globale : %.2f/5", totalAverageRating));

            // Ajouter la série au graphique
            barChart.getData().add(series);
        } else {
            totalReviewsLabel.setText("Aucun avis disponible");
            averageRatingLabel.setText("Pas de note moyenne");
        }

        // Créer un graphique à secteurs pour la répartition des notes
        PieChart pieChart = createRatingDistributionChart();

        // Ajouter les graphiques au conteneur
        chartContainer.getChildren().clear();
        chartContainer.getChildren().addAll(barChart, pieChart);
    }

    private PieChart createRatingDistributionChart() throws SQLException {
        // Collecter la distribution des notes
        Map<Integer, Integer> ratingDistribution = new HashMap<>();
        for (int rating = 1; rating <= 5; rating++) {
            int count = service.getReviewCountByRating(rating);
            System.out.println("Rating " + rating + " : " + count + " avis");
            if (count > 0) {
                ratingDistribution.put(rating, count);
            }
        }

        // Créer le graphique à secteurs
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Distribution des Notes");

        // Ajouter les données
        for (Map.Entry<Integer, Integer> entry : ratingDistribution.entrySet()) {
            PieChart.Data slice = new PieChart.Data(
                    entry.getKey() + " étoile" + (entry.getKey() > 1 ? "s" : ""),
                    entry.getValue()
            );
            pieChart.getData().add(slice);
        }

        return pieChart;
    }
}
