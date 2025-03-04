package Controllers;

import Models.Publication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import service.PublicationService;

public class UpdatePostController {
    @FXML
    private TextArea postContent;
    @FXML
    private Button saveButton;

    private Publication publication;
    private DisplayPostsController displayPostsController;
    private final PublicationService publicationService = new PublicationService();

    public void setPublication(Publication publication, DisplayPostsController controller) {
        this.publication = publication;
        this.displayPostsController = controller;
        postContent.setText(publication.getContenu());
    }

    @FXML
    private void saveChanges() {
        String updatedContent = postContent.getText();
        if (updatedContent.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Content cannot be empty!");
            alert.showAndWait();
            return;
        }
        publication.setContenu(updatedContent);
        publicationService.update(publication);
        displayPostsController.loadPublications();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Post updated successfully!");
        alert.showAndWait();
        postContent.getScene().getWindow().hide();
    }
}
