package TestMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML file
        URL fxmlResource = getClass().getResource("/fxml/login.fxml");
        if (fxmlResource == null) {
            throw new RuntimeException("Cannot find projectcreate.fxml");
        }
        Parent root = FXMLLoader.load(fxmlResource);

        // Create scene
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Onboardfiy");
        primaryStage.setFullScreen(false);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);

    }
}