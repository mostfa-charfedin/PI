package testMain;

import Services.EmailService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Objects;


public class MainFx extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ListQuiz.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Quiz management");
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
       /* EmailService emailService = new EmailService();
        // Test email sending (Replace with actual values)
        String recipient = "akrimi041@gmail.com"; // Replace with the actual recipient email
        String subject = "Test Email";
        String messageBody = "<h3>Hello!</h3><p>This is a test email from the Java email service.</p>";
        boolean isHtml = true; // Set to false if you want plain text

        emailService.sendEmail(recipient, subject, messageBody, isHtml);*/
    }
}