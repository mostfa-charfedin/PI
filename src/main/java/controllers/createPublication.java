package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Publication;

import java.io.File;

public class createPublication {

    @FXML
    private TextField bodyField; // Linked to the TextField in FXML

    @FXML
    private Button submitButton; // Linked to the Submit button in FXML

    @FXML
    private Button cancelButton; // Linked to the Cancel button in FXML

    @FXML
    private Button uploadButton; // Linked to the Upload File button in FXML

    private String filePath; // To store the path of the uploaded file

    @FXML
    private void handleSubmitButtonAction() {
        String body = bodyField.getText();
        if (body != null && !body.isEmpty()) {
            // Create a new publication
            Publication publication = new Publication(0, body, 1); // ID and user ID are placeholders
            if (filePath != null) {
                System.out.println("File uploaded: " + filePath);
            }
            System.out.println("Publication created: " + publication);
            bodyField.clear();
            filePath = null; // Reset file path after submission
        } else {
            System.out.println("Publication body is empty!");
        }
    }

    @FXML
    private void handleCancelButtonAction() {
        // Clear the fields
        bodyField.clear();
        filePath = null;
        System.out.println("Operation canceled.");
    }

    @FXML
    private void handleUploadButtonAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            System.out.println("File selected: " + filePath);
        }
    }
}