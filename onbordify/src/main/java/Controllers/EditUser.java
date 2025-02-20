package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private User selectedUser;

    UserService userservice = new UserService();

    @FXML
    void updateUser(ActionEvent event) {
        try {
            System.out.println(selectedUser);
            this.userservice.update(this.selectedUser);
            reset();
            messageMod.setText("User updated successfuly");
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void setUserData(User user) {
        System.out.println(user);
this.selectedUser = user;
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
    void reset() {
        this.nomMod.clear();
        this.prenomMod.clear();
        this.emailMod.clear();
        this.cinMod.clear();
        this.dateMod.setValue(null);
        this.roleMod.setItems(null);
    }
}
