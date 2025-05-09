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
import Services.SpamService;
import utils.UserSession;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CommentsController {

    @FXML private ListView<HBox> listViewComments;
    @FXML private TextArea commentInput;
    @FXML private TextArea imagePathInput;
    @FXML private TextField titleInput;
    @FXML private ImageView uploadedImageView; // Make sure this is linked in FXML

    private final CommentaireService commentaireService = new CommentaireService();
    private final SpamService spamService = new SpamService();
    private Publication currentPublication;
    private Commentaire selectedComment;

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

            if (comment.getImagePath() != null && !comment.getImagePath().isEmpty()) {
                File file = new File(comment.getImagePath());
                if (file.exists()) {
                    String imageUri = file.toURI().toString();
                    ImageView imageView = new ImageView(new Image(imageUri, 50, 50, true, true));
                    commentBox.getChildren().add(imageView);
                }
            }

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(e -> deleteComment(comment));

            Button updateButton = new Button("Update");
            updateButton.setOnAction(e -> editComment(comment));

            commentBox.getChildren().addAll(commentLabel, updateButton, deleteButton);
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
                loadComments();
            }
        });
    }

    private void editComment(Commentaire comment) {
        selectedComment = comment;
        titleInput.setText(comment.getTitre());
        commentInput.setText(comment.getDescription());
        imagePathInput.setText(comment.getImagePath());

        // Load the existing image if available
        if (comment.getImagePath() != null && !comment.getImagePath().isEmpty()) {
            File imgFile = new File(comment.getImagePath());
            if (imgFile.exists()) {
                uploadedImageView.setImage(new Image(imgFile.toURI().toString()));
            }
        } else {
            uploadedImageView.setImage(null); // Clear the image view if no image
        }
    }

    @FXML
    private void addComment() {
        String title = titleInput.getText().trim();
        String content = commentInput.getText().trim();
        String imagePath = imagePathInput.getText().trim();

        if (title.isEmpty() || !title.matches("[a-zA-ZÀ-ÿ\\s]+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Title", "The title must contain only letters and spaces.");
            return;
        }

        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Comment content cannot be empty!");
            return;
        }

        if (spamService.contientMotsInterdits(content)) {
            showAlert(Alert.AlertType.ERROR, "Harmful Message", "Your comment contains inappropriate words!");
            return;
        }

        if (!imagePath.isEmpty() && !isValidImagePath(imagePath)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Image Path", "Please enter a valid image file path.");
            return;
        }

        if (selectedComment != null) {
            selectedComment.setTitre(title);
            selectedComment.setDescription(content);
            selectedComment.setImagePath(imagePath);
            commentaireService.update(selectedComment);
            selectedComment = null;
        } else {
            UserSession session = UserSession.getInstance();
            if (session == null) {
                showAlert(Alert.AlertType.ERROR, "Session Error", "No active session. Please log in.");
                return;
            }

            Commentaire newComment = new Commentaire(0, title, content, imagePath, session.getUserId(), currentPublication.getIdPublication());
            commentaireService.add(newComment);
        }

        titleInput.clear();
        commentInput.clear();
        imagePathInput.clear();
        uploadedImageView.setImage(null); // Clear the uploaded image view
        loadComments();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    private boolean isValidImagePath(String path) {
        File file = new File(path);
        return file.exists() && !file.isDirectory() && path.matches(".*\\.(jpg|jpeg|png|gif)$");
    }

    @FXML
    void chooseImageFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Use the original path for the uploads directory
            File uploadsDir = new File("src/main/resources/uploads");
            if (!uploadsDir.exists()) {
                uploadsDir.mkdirs(); // Create the directory if it doesn't exist
            }

            // Use the absolute path for the input field
            String originalPath = selectedFile.getAbsolutePath();
            imagePathInput.setText(originalPath); // Set the original path for display

            // Create the destination file in the uploads directory
            File destinationFile = new File(uploadsDir, selectedFile.getName());

            try {
                // Copy the selected file to the destination
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Display the uploaded image in the ImageView
                uploadedImageView.setImage(new Image(destinationFile.toURI().toString()));
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Upload Failed", "Failed to upload image: " + e.getMessage());
            }
        }
    }
}