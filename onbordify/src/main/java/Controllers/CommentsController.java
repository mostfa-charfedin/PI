package Controllers;

import Models.Commentaire;
import Models.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import Services.CommentaireService;
import Services.SpamService;
import utils.UserSession;

import java.sql.Timestamp;
import java.util.List;

public class CommentsController {
    @FXML private ListView<VBox> listViewComments;
    @FXML private TextArea commentInput;
    @FXML private Label postTitleLabel;
    @FXML private Label postContentLabel;

    private final CommentaireService commentaireService = new CommentaireService();
    private final SpamService spamService = new SpamService();
    private Publication currentPublication;
    private Commentaire selectedComment;

    public void setPublication(Publication publication) {
        this.currentPublication = publication;
        displayPostDetails();
        loadComments();
    }

    private void displayPostDetails() {
        postTitleLabel.setText("Post: " + currentPublication.getTitle());
        postContentLabel.setText(currentPublication.getContenu());
    }

    private void loadComments() {
        listViewComments.getItems().clear();
        List<Commentaire> comments = commentaireService.getCommentsByPublication(currentPublication.getId());

        for (Commentaire comment : comments) {
            VBox commentBox = new VBox(5);
            // Ajoutez le nom de l'utilisateur
            Label userLabel = new Label(comment.getUserPrenom() + " " + comment.getUserNom());
            userLabel.setStyle("-fx-font-weight: bold;");
            HBox contentBox = new HBox(10);

            Text contentText = new Text(comment.getContenu());
            contentText.setWrappingWidth(400);

            Label timestampLabel = new Label(comment.getCreatedAt().toString());
            timestampLabel.setStyle("-fx-font-size: 10; -fx-text-fill: gray;");

            contentBox.getChildren().addAll(contentText, timestampLabel);

            // Ajouter d'abord le userLabel au commentBox
            commentBox.getChildren().add(userLabel); // <-- Cette ligne était manquante
            commentBox.getChildren().add(contentBox);

            UserSession session = UserSession.getInstance();
            if (session != null && session.getUserId() == comment.getUserId()) {
                HBox buttonBox = new HBox(10);
                Button updateButton = new Button("Edit");
                updateButton.setOnAction(e -> editComment(comment));

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(e -> deleteComment(comment));

                buttonBox.getChildren().addAll(updateButton, deleteButton);
                commentBox.getChildren().add(buttonBox);
            }
            // else n'est plus nécessaire car contentBox est déjà ajouté

            listViewComments.getItems().add(commentBox);
        }
    }

    private void deleteComment(Commentaire comment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete this comment?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                commentaireService.delete(comment.getId());
                loadComments();
            }
        });
    }

    private void editComment(Commentaire comment) {
        selectedComment = comment;
        commentInput.setText(comment.getContenu());
    }

    @FXML
    private void addComment() {
        String content = commentInput.getText().trim();

        if (content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Comment content cannot be empty!");
            return;
        }

        if (spamService.contientMotsInterdits(content)) {
            showAlert(Alert.AlertType.ERROR, "Harmful Message", "Your comment contains inappropriate words!");
            return;
        }

        UserSession session = UserSession.getInstance();
        if (session == null) {
            showAlert(Alert.AlertType.ERROR, "Session Error", "No active session. Please log in.");
            return;
        }

        Commentaire comment = selectedComment != null ? selectedComment : new Commentaire();
        comment.setContenu(content);
        comment.setUserId(session.getUserId());
        comment.setPosteId(currentPublication.getId());

        if (selectedComment != null) {
            commentaireService.update(comment);
            selectedComment = null;
        } else {
            commentaireService.add(comment);
        }

        commentInput.clear();
        loadComments();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}