package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;


public class GestionUser {
    UserService userservice = new UserService();;

    @FXML
    private TextField cin;

    @FXML
    private Label message;

    @FXML
    private Label messagelist;

    @FXML
    private DatePicker date;

    @FXML
    private TextField email;

    @FXML
    private ListView<String> listview;

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField rechercheFildMod;

    @FXML
    private ComboBox<Role> role;
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

private User selectedUser ;
    @FXML
    public void initialize() {
        role.setItems(FXCollections.observableArrayList(Role.values()));
        loadUsers();

        listview.setOnMouseClicked(event -> {
            int selectedIndex = listview.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    this.selectedUser = userservice.getAll().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void loadUsers() {
        listview.getItems().clear();
        try {
            List<User> users = userservice.getAll();

            for (User user : users) {
                listview.getItems().add(user.getNom() + " | " + user.getPrenom() + " | " + user.getCin());
            }
        } catch (Exception e) {
            messagelist.setText("Error loading User: " + e.getMessage());
        }
    }

    @FXML
    void addUser(ActionEvent event) {

        User user = new User(this.nom.getText(), this.prenom.getText(), this.email.getText(), Integer.parseInt(this.cin.getText()),
                java.sql.Date.valueOf(this.date.getValue()),
                this.role.getValue());
        System.out.println(user);
        try {
            this.userservice.create(user);
            reset();
            initialize();
            message.setText("User created successfuly");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }


    @FXML
    void chercherUser(ActionEvent event) {

    }

    @FXML
    void deleteUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Please Select a user");
            return;
        }
        try {
            userservice.delete(selectedUser.getId());
            loadUsers();
            messagelist.setText("User deleted.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void updateUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Select a project to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editUser.fxml"));
            Parent root = loader.load(); // Load the FXML first

            // Get the controller after loading the FXML
            EditUser editController = loader.getController();
            editController.setUserData(selectedUser);

            // Show the edit window
            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Edit Project");
            popupStage.show();

        } catch (Exception e) {
            messagelist.setText("Error opening edit window: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error opening edit window: " + e.getMessage());
            alert.showAndWait();
        }
    }




    void reset() {
        this.nom.clear();
        this.prenom.clear();
        this.email.clear();
        this.cin.clear();
        this.date.setValue(null);
        this.role.setItems(null);
    }
}

