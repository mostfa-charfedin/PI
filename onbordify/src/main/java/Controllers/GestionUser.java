package Controllers;

import Models.Role;
import Models.User;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class GestionUser {
    UserService userservice = new UserService();

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
    private TextField num_phone;

    @FXML
    private TextField rechercheFildMod;

    @FXML
    private ComboBox<Role> role;

    @FXML
    private ComboBox<Role> roleFilter;

    @FXML
    private VBox prenomErrorBox;
    @FXML
    private VBox nomErrorBox;
    @FXML
    private VBox numErrorBox;
    @FXML
    private VBox emailErrorBox;
    @FXML
    private VBox cinErrorBox;
    @FXML
    private VBox dateNaissanceErrorBox;



    //  private final String LOGIN_URL = "http://localhost:8080/users/login";

    private User selectedUser ;
    private ObservableList<String> userList;
    private FilteredList<String> filteredList;

    @FXML
    public void initialize() {

        role.setItems(FXCollections.observableArrayList(Role.values()));
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleFilter.setItems(roles);
        roleFilter.getItems().add(0, null);
        roleFilter.setValue(null);
        loadUsers(null);

        roleFilter.setOnAction(event -> filterByRole());
        // Listener pour la recherche en temps réel
        rechercheFildMod.textProperty().addListener((observable, oldValue, newValue) -> {
            chercherUser();
        });

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

        // Contrôle de saisie pour le Cin
        cin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cin.setText(oldValue);
                showError(cinErrorBox, "The Cin should contain only numbers.");
            } else if (newValue.length() > 8) {
                cin.setText(oldValue);
                showError(cinErrorBox, "The Cin should not exceed 8 digits.");
            } else {
                clearError(cinErrorBox);
            }
        });


        num_phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                num_phone.setText(oldValue);
                showError(numErrorBox, "The Number phone should contain only numbers.");
            } else if (newValue.length() > 8) {
                num_phone.setText(oldValue);
                showError(numErrorBox, "The Number phone should not exceed 8 digits.");
            } else {
                clearError(numErrorBox);
            }
        });

        // Contrôle de saisie pour le Nom (uniquement des lettres)
        nom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) { // Seules les lettres et espaces sont autorisés
                nom.setText(oldValue); // Rejeter la nouvelle valeur
                showError(nomErrorBox, "The name must contain only letters.");
            } else {
                clearError(nomErrorBox);
            }
        });

        // Contrôle de saisie pour le Prénom
        prenom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z ]*")) { // Seules les lettres et espaces sont autorisés
                prenom.setText(oldValue); // Rejeter la nouvelle valeur
                showError(prenomErrorBox, "The first name must contain only letters.");
            } else {
                clearError(prenomErrorBox);
            }
        });

        // Contrôle de saisie pour l'Email
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                showError(emailErrorBox, "Please enter a valid email address.");
            } else {
                clearError(emailErrorBox);
            }
        });


        // Contrôle de saisie pour la Date de Naissance
        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                LocalDate today = LocalDate.now();
                int age = Period.between(newValue, today).getYears();

                if (age < 18) {
                    date.setValue(oldValue); // Rétablir l'ancienne valeur
                    showError(dateNaissanceErrorBox, "The employee must be at least 18 years old.");
                } else {
                    clearError(dateNaissanceErrorBox);
                }
            }
        });
    }

    // Méthode pour valider le format de l'email
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Méthode pour afficher un message d'erreur sous un champ
    private void showError(VBox errorBox, String message) {
        errorBox.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorBox.getChildren().add(errorLabel);
    }

    // Méthode pour effacer un message d'erreur
    private void clearError(VBox errorBox) {
        errorBox.getChildren().clear();
    }

    public void loadUsers(Role roleFilter) {
        try {
            List<User> users = userservice.getAll();

            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            // Appliquer un filtre par rôle si nécessaire
            List<User> filteredUsers = users.stream()
                    .filter(user -> roleFilter == null || user.getRole() == roleFilter)
                    .collect(Collectors.toList());

            userList.setAll(filteredUsers.stream()
                    .map(user -> user.getNom() + " | " + user.getPrenom() + " | " + user.getCin() + " | " + user.getEmail())
                    .collect(Collectors.toList()));

            if (filteredList == null) {
                filteredList = new FilteredList<>(userList, p -> true);
                listview.setItems(filteredList);
            }

        } catch (Exception e) {
            messagelist.setText("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void filterByRole() {
        Role selectedRole = roleFilter.getValue();
        loadUsers(selectedRole);
    }





    @FXML
    void addUser(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (cin.getText().isEmpty() || nom.getText().isEmpty() || prenom.getText().isEmpty()|| num_phone.getText().isEmpty()
                || email.getText().isEmpty() || date.getValue() == null || role.getValue() == null) {
            message.setText("Please fill in all fields.");
            return; // Arrêter l'exécution si un champ est vide
        }

        // Vérifier l'email
        if (!isValidEmail(email.getText())) {
            showError(emailErrorBox, "Please enter a valid email address.");
            return;
        }

        // Vérifier que CIN est un entier valide
        int cinValue;
        try {
            cinValue = Integer.parseInt(cin.getText());
        } catch (NumberFormatException e) {
            showError(cinErrorBox, "CIN must be a valid number.");
            return;
        }

        // Création de l'objet utilisateur
        User user = new User(nom.getText(), prenom.getText(), email.getText(),
                Integer.parseInt(cin.getText()), java.sql.Date.valueOf(date.getValue()),
                role.getValue(),Integer.parseInt( num_phone.getText()));

        try {
            userservice.create(user);
            reset();
            initialize();
            message.setText("User created successfully");
            message.setStyle("-fx-text-fill: #4CAF50;");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace(); // Afficher l'erreur complète dans la console
        }
    }



    @FXML
    void chercherUser() {
        // Récupérer le texte saisi dans le champ de recherche
        String searchText = rechercheFildMod.getText().toLowerCase();

        // Appliquer le filtre sur la liste des utilisateurs
        filteredList.setPredicate(userString -> {
            if (searchText == null || searchText.isEmpty()) {
                return true; // Afficher tous les utilisateurs si le champ de recherche est vide
            }

            // Vérifier si le nom, le prénom ou le CIN correspond au texte de recherche
            return userString.toLowerCase().contains(searchText);
        });
    }

    @FXML
    void deleteUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Please Select a user");
            return;
        }
        try {
            userservice.delete(selectedUser.getId());
            loadUsers(roleFilter.getValue());
            messagelist.setText("User deleted.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void updateUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Select a user to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditUser.fxml"));
            Parent root = loader.load();

            EditUser editUserController = loader.getController();
            editUserController.setUserData(selectedUser);
            editUserController.setGestionUserController(this); // Pass GestionUser instance
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void goToScoreList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Score.fxml"));
        Parent root = loader.load();
        messagelist.getScene().setRoot(root);
    }



    void reset() {
        this.nom.clear();
        this.prenom.clear();
        this.email.clear();
        this.cin.clear();
        this.num_phone.clear();
        this.date.setValue(null);
        this.role.setItems(null);
        this.selectedUser= null;
    }
}

