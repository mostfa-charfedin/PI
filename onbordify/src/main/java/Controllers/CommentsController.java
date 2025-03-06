package Controllers;

import Models.Commentaire;
import Models.Publication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import Services.CommentaireService;

import java.io.File;
import java.util.List;

public class CommentsController {

    @FXML private ListView<HBox> listViewComments;
    @FXML private TextArea commentInput;
    @FXML private TextArea imagePathInput;

    @FXML
    private TextField titleInput;
    private final CommentaireService commentaireService = new CommentaireService();
    private Publication currentPublication;

    public void setPublication(Publication publication) {
        this.currentPublication = publication;
        loadComments();
    }

    private void loadComments() {
        listViewComments.getItems().clear();
        List<Commentaire> comments = commentaireService.getCommentsByPublication(currentPublication.getIdPublication());

        for (Commentaire comment : comments) {
            HBox commentBox = new HBox(10);
            Label commentLabel = new Label(comment.getTitre() + ": " + comment.getDescription());
            System.out.println("Image Path: " + comment.getImagePath()); // Debugging

            if (comment.getImagePath() != null && !comment.getImagePath().isEmpty()) {
                try {
                    File file = new File(comment.getImagePath());
                    if (file.exists()) {
                        // Convert to valid file URI
                        String imageUri = file.toURI().toString();
                        ImageView imageView = new ImageView(new Image(imageUri, 50, 50, true, true));
                        commentBox.getChildren().add(imageView);
                    } else {
                        System.err.println("Image file not found: " + comment.getImagePath());
                    }
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }
            }
            // Create Delete Button
            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteComment(comment));

            commentBox.getChildren().addAll(commentLabel, deleteButton);
            listViewComments.getItems().add(commentBox);
        }

    }
    private void deleteComment(Commentaire comment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this comment?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                commentaireService.delete(comment.getIdCommentaire());
                loadComments(); // Refresh the comment list
            }
        });
    }


    @FXML
    private void addComment() {
        String title = titleInput.getText().trim();
        String content = commentInput.getText().trim();
        String imagePath = imagePathInput.getText().trim();

        // Validate title
        if (title.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Title is required!");
            return;
        }

        // Validate comment content
        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Comment content cannot be empty!");
            return;
        }

        // Validate image path if provided
        if (!imagePath.isEmpty() && !isValidImagePath(imagePath)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Image Path", "Please enter a valid image file path.");
            return;
        }

        // Create and add new comment
        Commentaire newComment = new Commentaire(0, title, content, imagePath, 1, currentPublication.getIdPublication());
        commentaireService.add(newComment);

        // Clear inputs
        titleInput.clear();
        commentInput.clear();
        imagePathInput.clear();

        // Refresh the comments list
        loadComments();
    }

    /**
     * Helper method to display an alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Validates the image path.
     * Ensures the file exists and has a valid image extension.
     */
    private boolean isValidImagePath(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            return false;
        }

        // Check for common image file extensions
        String lowerCasePath = path.toLowerCase();
        return lowerCasePath.endsWith(".jpg") || lowerCasePath.endsWith(".jpeg") ||
                lowerCasePath.endsWith(".png") || lowerCasePath.endsWith(".gif");
    }

    @FXML
    void chooseImageFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Set the image path to the imageTF text field
            imagePathInput.setText(selectedFile.getAbsolutePath());
        } else {
            System.out.println("No image selected.");
        }
    }
}
