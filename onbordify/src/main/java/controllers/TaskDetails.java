package controllers;

import Models.Tache;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskDetails {

    @FXML
    private TextField txtTitle, txtUser;

    @FXML
    private TextArea txtDescription;

    public void setTaskData(Tache task) {
        if (task != null) {
            txtTitle.setText(task.getTitre());
            txtDescription.setText(task.getDescription());
            txtUser.setText(task.getNom() + " " + task.getPrenom());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }
}
