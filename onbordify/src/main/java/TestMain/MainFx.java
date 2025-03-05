package TestMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML file
        URL fxmlResource = getClass().getResource("/display_posts.fxml");
        if (fxmlResource == null) {
            throw new RuntimeException("Cannot find CreatePublication.fxml");
        }
        Parent root = FXMLLoader.load(fxmlResource);
        // Create scene
        Scene scene = new Scene(root, 800 , 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("FXML Test");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}