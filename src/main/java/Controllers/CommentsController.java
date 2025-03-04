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
import service.CommentaireService;
import service.SpamService;

import java.io.File;
import java.util.List;

public class CommentsController {

    @FXML private ListView<HBox> listViewComments;
    @FXML private TextArea commentInput;
    @FXML private TextArea imagePathInput;
    @FXML private TextField titleInput;

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
        // Préparer l'interface pour modifier le commentaire
        selectedComment = comment;
        titleInput.setText(comment.getTitre());
        commentInput.setText(comment.getDescription());
        imagePathInput.setText(comment.getImagePath());
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
            // Mise à jour du commentaire sélectionné
            selectedComment.setTitre(title);
            selectedComment.setDescription(content);
            selectedComment.setImagePath(imagePath);

            commentaireService.update(selectedComment); // Appeler la méthode d'update
            selectedComment = null; // Reset selected comment
        } else {
            // Ajout d'un nouveau commentaire
            Commentaire newComment = new Commentaire(0, title, content, imagePath, 1, currentPublication.getIdPublication());
            commentaireService.add(newComment);
        }

        titleInput.clear();
        commentInput.clear();
        imagePathInput.clear();

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
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            imagePathInput.setText(selectedFile.getAbsolutePath());
        }
    }
}
