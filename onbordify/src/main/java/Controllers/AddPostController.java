package Controllers;

import Models.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.PublicationService;
import utils.UserSession;

import java.sql.Date;
import java.time.LocalDate;

public class AddPostController {
    @FXML
    private TextField txtContent;

    @FXML
    private Button submitButton;

    private PublicationService publicationService = new PublicationService();
    private DisplayPostsController postListController; // Reference to the post list

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

        // Input validation
        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Content cannot be empty!");
            return;
        }

        if (content.length() > 255) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Content must not exceed 255 characters!");
            return;
        }

        // Create and save the new post
        Publication newPost = new Publication();
        newPost.setContenu(content);
        newPost.setDate(Date.valueOf(today));

        // Use the current user's ID from the UserSession
        UserSession session = UserSession.getInstance();
        if (session != null) {
            newPost.setIdUser(session.getUserId());
        } else {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No active session. Please log in.");
            return;
        }

        try {
            publicationService.add(newPost);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Post added successfully!");

            // Refresh the post list
            if (postListController != null) {
                postListController.loadPublications(); // Call the method to reload posts
            }

            // Close the window
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the post.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}