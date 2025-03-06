package Controllers;

import Models.Commentaire;
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
import Services.CommentaireService;
import Services.PublicationService;

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
        List<Publication> publications = publicationService.getAll();
        listViewPublications.getItems().addAll(publications);


        // Ensure we set a custom cell factory before adding items
        listViewPublications.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Publication publication, boolean empty) {
                super.updateItem(publication, empty);
                if (empty || publication == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create layout
                    HBox hbox = new HBox(10);
                    Text postText = new Text(publication.getContenu());

                    // ✅ Ensure the button is created and added
                    Button viewCommentsButton = new Button("View Comments");
                    viewCommentsButton.setOnAction(event -> displayComments(publication));

                    // ✅ Add both text and button to the HBox
                    hbox.getChildren().addAll(postText, viewCommentsButton);

                    // ✅ Set the HBox as the cell's graphic
                    setGraphic(hbox);
                }
            }
        });
        // ✅ Add publications after setting the cell factory
        listViewPublications.getItems().clear();
        listViewPublications.getItems().addAll(publications);

    }
    private void displayComments(Publication publication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/comments_view.fxml"));
            Parent root = loader.load();

            CommentsController commentsController = loader.getController();
            commentsController.setPublication(publication);

            Stage stage = new Stage();

            stage.setTitle("Comments");
            Scene scene = new Scene(root, 800 , 500);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPublications() {
        List<Publication> publications = publicationService.getAll();
        ObservableList<Publication> observablePublications = FXCollections.observableArrayList(publications);
        listViewPublications.setItems(observablePublications);

        // Custom cell factory
        listViewPublications.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Publication pub, boolean empty) {
                super.updateItem(pub, empty);
                if (empty || pub == null) {
                    setText(null);
                } else {
                    setText("📢 " + pub.getContenu() + "\n📅 " + pub.getDate());
                }
            }
        });
    }

    @FXML
    private void openAddPost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/add_post.fxml"));
            Parent root = loader.load();

            AddPostController addPostController = loader.getController();
            addPostController.setPostListController(this); // Pass reference for refreshing

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    @FXML
    private void deleteSelectedPost() {
        Publication selectedPublication = listViewPublications.getSelectionModel().getSelectedItem();
        if (selectedPublication != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this post?", ButtonType.YES, ButtonType.NO);
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    publicationService.delete(selectedPublication.getIdPublication());
                    loadPublications(); // Refresh list after deletion
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a post to delete.");
            alert.showAndWait();
        }
    }
}
