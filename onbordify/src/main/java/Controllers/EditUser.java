package Controllers;

import Models.Role;
import Models.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
public class EditUser {

    @FXML
    private TextField cinMod;

    @FXML
    private DatePicker dateMod;

    @FXML
    private TextField emailMod;

    @FXML
    private Label messageMod;

    @FXML
    private TextField nomMod;

    @FXML
    private TextField prenomMod;

    @FXML
    private ComboBox<Role> roleMod;

    @FXML
    void updateUser(ActionEvent event) {
    }

    public void setUserData(User user) {
        System.out.println(user);

        nomMod.setText(user.getNom());
        prenomMod.setText(user.getPrenom());
        emailMod.setText(user.getEmail());
        cinMod.setText(String.valueOf(user.getCin()));




        if (roleMod.getItems().isEmpty()) {
            roleMod.setItems(FXCollections.observableArrayList(Role.values()));
        }
        roleMod.setValue(user.getRole());
    }

    public LocalDate convertSqlDateToLocalDate(Date sqlDate) {
        Instant instant = sqlDate.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
