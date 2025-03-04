package Controllers;

import Models.Publication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.CommentaireService;
import service.PublicationService;

import java.io.IOException;
import java.util.List;

public class DisplayPostsController {
    @FXML
    private ListView<Publication> listViewPublications;
    @FXML
    private Button addPostButton;

    private PublicationService publicationService = new PublicationService();
    private CommentaireService commentaireService = new CommentaireService();

    @FXML
    public void initialize() {
        loadPublications();
    }

    public void loadPublications() {
        List<Publication> publications = publicationService.getAll();
        ObservableList<Publication> observablePublications = FXCollections.observableArrayList(publications);
        listViewPublications.setItems(observablePublications);

        listViewPublications.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Publication publication, boolean empty) {
                super.updateItem(publication, empty);
                if (empty || publication == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Text postText = new Text(publication.getContenu());

                    Button viewCommentsButton = new Button("View Comments");
                    viewCommentsButton.setOnAction(event -> displayComments(publication));

                    Button editButton = new Button("Edit");
                    editButton.setOnAction(event -> editPost(publication));

                    Button deleteButton = new Button("Delete");
                    deleteButton.setOnAction(event -> deletePost(publication));

                    hbox.getChildren().addAll(postText, viewCommentsButton, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void displayComments(Publication publication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comments_view.fxml"));
            Parent root = loader.load();

            CommentsController commentsController = loader.getController();
            commentsController.setPublication(publication);

            Stage stage = new Stage();
            stage.setTitle("Comments");
            Scene scene = new Scene(root, 800, 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_post.fxml"));
            Parent root = loader.load();

            AddPostController addPostController = loader.getController();
            addPostController.setPostListController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editPost(Publication publication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/update_post.fxml"));
            Parent root = loader.load();

            UpdatePostController updatePostController = loader.getController();
            updatePostController.setPublication(publication, this);

            Stage stage = new Stage();
            stage.setTitle("Edit Post");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deletePost(Publication publication) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post?", ButtonType.YES, ButtonType.NO);
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                publicationService.delete(publication.getIdPublication());
                loadPublications();
            }
        });
    }
}
