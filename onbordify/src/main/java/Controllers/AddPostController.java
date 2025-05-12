package Controllers;

import Models.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Services.PublicationService;
import utils.UserSession;

public class AddPostController {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextArea txtContent;
    @FXML
    private ComboBox<String> categoryComboBox;  // Instead of TextField txtCategories
    @FXML
    private Button submitButton;

    private PublicationService publicationService = new PublicationService();
    private DisplayPostsController postListController;

    public void setPostListController(DisplayPostsController postListController) {
        this.postListController = postListController;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    private void submitPost() {
        String title = txtTitle.getText().trim();
        String content = txtContent.getText().trim();
        String categories = categoryComboBox.getValue(); // Get selected value from ComboBox

        // Input validation
        if (title.isEmpty() || content.isEmpty() || categories.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "All fields are required!");
            return;
        }

        if (title.length() > 255) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Title must not exceed 255 characters!");
            return;
        }
        if (categories == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a category!");
            return;
        }

        // Format categories as JSON array
        String categoriesJson = formatCategoriesToJson(categories);

        // Create and save the new post
        Publication newPost = new Publication();
        newPost.setTitle(title);
        newPost.setContenu(content);
        newPost.setCategories(categoriesJson);
        newPost.setSignaled(false); // Default to not signaled

        // Use the current user's ID from the UserSession
        UserSession session = UserSession.getInstance();
        if (session != null) {
            newPost.setUserId(session.getUserId());
        } else {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No active session. Please log in.");
            return;
        }

        try {
            publicationService.add(newPost);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Post added successfully!");

            // Refresh the post list
            if (postListController != null) {
                postListController.loadPublications();
            }

            // Close the window
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the post.");
            e.printStackTrace();
        }
    }

    private String formatCategoriesToJson(String categoriesInput) {
        // Simple formatting - could be enhanced with proper JSON handling
        String[] categoriesArray = categoriesInput.split(",");
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < categoriesArray.length; i++) {
            jsonBuilder.append("\"").append(categoriesArray[i].trim()).append("\"");
            if (i < categoriesArray.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}