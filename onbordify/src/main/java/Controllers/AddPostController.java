package Controllers;

import Models.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.PublicationService;

import java.sql.Date;
import java.time.LocalDate;

public class AddPostController {
    @FXML
    private TextField txtContent;

    @FXML
    private Button submitButton;



    private PublicationService publicationService = new PublicationService();
    private DisplayPostsController postListController; // Reference to post list

    public void setPostListController(DisplayPostsController postListController) {
        this.postListController = postListController;
    }
    LocalDate today = LocalDate.now();

    @FXML
    public void initialize() {
    }

    @FXML
    private void submitPost() {
        String content = txtContent.getText().trim();

        // Validate input
        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Content cannot be empty!");
            return;
        }

        // Create and save new post
        Publication newPost = new Publication();
        newPost.setContenu(content);
        newPost.setDate(Date.valueOf(today));
        newPost.setIdUser(1); // Dummy user ID

        publicationService.add(newPost);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Post added successfully!");

        // Refresh post list
        if (postListController != null) {
            postListController.loadPublications(); // Call method to reload posts
        }

        // Close the window
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }



    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}