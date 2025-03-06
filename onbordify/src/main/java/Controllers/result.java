package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class result {


        @FXML
        private Label score; // This corresponds to the 'score' label in your FXML



    public void setScore(int scoreValue) {
        score.setText("Your final score is: " + scoreValue+" points");
    }
    }
