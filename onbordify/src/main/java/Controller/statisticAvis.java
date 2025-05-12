package Controller;

import Models.programmebienetre;
import Services.programmebienetreService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

public class statisticAvis implements Initializable {
    @FXML private Label averageRatingValue;
    @FXML private HBox starBox;
    @FXML private Label totalReviewersValue;
    @FXML private BarChart<String, Number> ratingBarChart;
    @FXML private TableView<ProgramStat> programStatsTable;
    @FXML private TableColumn<ProgramStat, String> colProgramName;
    @FXML private TableColumn<ProgramStat, Integer> colNumReviews;
    @FXML private TableColumn<ProgramStat, Double> colAvgRating;
    @FXML private TableColumn<ProgramStat, Double> colRatingDist;

    private final programmebienetreService service = new programmebienetreService();

    public static class ProgramStat {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty numReviews;
        private final SimpleDoubleProperty avgRating;
        private final SimpleDoubleProperty ratingDist;

        public ProgramStat(String name, int numReviews, double avgRating, double ratingDist) {
            this.name = new SimpleStringProperty(name);
            this.numReviews = new SimpleIntegerProperty(numReviews);
            this.avgRating = new SimpleDoubleProperty(avgRating);
            this.ratingDist = new SimpleDoubleProperty(ratingDist);
        }
        public String getName() { return name.get(); }
        public int getNumReviews() { return numReviews.get(); }
        public double getAvgRating() { return avgRating.get(); }
        public double getRatingDist() { return ratingDist.get(); }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ratingBarChart.getStylesheets().add(getClass().getResource("/statisticAvis.css").toExternalForm());
            loadDashboardStats();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDashboardStats() throws Exception {
        List<programmebienetre> programmes = service.getAll();

        int totalReviews = 0;
        double totalRatingSum = 0;
        int totalRatingCount = 0;
        Map<Integer, Integer> ratingDistribution = new HashMap<>();
        ObservableList<ProgramStat> stats = FXCollections.observableArrayList();

        for (programmebienetre prog : programmes) {
            int numReviews = service.getTotalReviews(prog.getIdProgramme());
            double avgRating = numReviews > 0 ? service.getAverageRating(prog.getIdProgramme()) : 0.0;
            stats.add(new ProgramStat(prog.getTitre(), numReviews, avgRating, avgRating / 5.0));
            totalReviews += numReviews;
            totalRatingSum += avgRating * numReviews;
            totalRatingCount += numReviews;
            for (int i = 1; i <= 5; i++) {
                int count = service.getReviewCountByRatingForProgramme(prog.getIdProgramme(), i);
                ratingDistribution.put(i, ratingDistribution.getOrDefault(i, 0) + count);
            }
        }

        double globalAvg = totalRatingCount > 0 ? totalRatingSum / totalRatingCount : 0.0;
        averageRatingValue.setText(String.format("%.1f / 5", globalAvg));
        setStars(globalAvg);
        int uniqueUsers = service.getUniqueReviewerCount();
        totalReviewersValue.setText(String.valueOf(uniqueUsers));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Reviews");
        for (int i = 1; i <= 5; i++) {
            series.getData().add(new XYChart.Data<>(i + " Stars", ratingDistribution.getOrDefault(i, 0)));
        }
        ratingBarChart.getData().clear();
        ratingBarChart.getData().add(series);

        // Set per-bar color for each bar
        for (int i = 0; i < series.getData().size(); i++) {
            XYChart.Data<String, Number> item = series.getData().get(i);
            String color;
            switch (i) {
                case 0: color = "#eeb6c2"; break; // 1 Star - Pink
                case 1: color = "#ffe0a3"; break; // 2 Stars - Orange/Yellow
                case 2: color = "#ffeeb3"; break; // 3 Stars - Light Yellow
                case 3: color = "#b6e2f2"; break; // 4 Stars - Light Blue
                case 4: color = "#b6d7f2"; break; // 5 Stars - Blue
                default: color = "#eeb6c2";
            }
            item.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
        }

        colProgramName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colNumReviews.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumReviews()).asObject());
        colAvgRating.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAvgRating()).asObject());
        colAvgRating.setCellFactory(column -> new TableCell<ProgramStat, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", value));
                }
            }
        });
        colRatingDist.setCellFactory(column -> new TableCell<ProgramStat, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    ProgressBar bar = new ProgressBar(value);
                    bar.setStyle("-fx-accent: #FFC107;");
                    bar.setPrefWidth(120);
                    setGraphic(bar);
                }
            }
        });
        colRatingDist.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getRatingDist()).asObject());
        programStatsTable.setItems(stats);
    }

    private void setStars(double rating) {
        starBox.getChildren().clear();
        int fullStars = (int) rating;
        boolean halfStar = (rating - fullStars) >= 0.5;
        for (int i = 0; i < fullStars; i++) {
            Label star = new Label("★");
            star.setStyle("-fx-font-size: 28px; -fx-text-fill: #FFC107;");
            starBox.getChildren().add(star);
        }
        if (halfStar) {
            Label half = new Label("☆");
            half.setStyle("-fx-font-size: 28px; -fx-text-fill: #FFC107;");
            starBox.getChildren().add(half);
        }
        for (int i = fullStars + (halfStar ? 1 : 0); i < 5; i++) {
            Label empty = new Label("☆");
            empty.setStyle("-fx-font-size: 28px; -fx-text-fill: #E0E0E0;");
            starBox.getChildren().add(empty);
        }
    }
}