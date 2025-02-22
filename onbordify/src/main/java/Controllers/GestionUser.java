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
    private TextField rechercheFildMod;

    @FXML
    private ComboBox<Role> role;

    @FXML
    private VBox prenomErrorBox;
    @FXML
    private VBox nomErrorBox;
    @FXML
    private VBox emailErrorBox;
    @FXML
    private VBox cinErrorBox;


   // private final RestTemplate restTemplate = new RestTemplate();
    private final String LOGIN_URL = "http://localhost:8080/users/login";

private User selectedUser ;
private ObservableList<String> userList;
private FilteredList<String> filteredList;

    @FXML
    public void initialize() {
        role.setItems(FXCollections.observableArrayList(Role.values()));
        loadUsers();
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
            } else {
                clearError(cinErrorBox);
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

    public void loadUsers() {
        try {
            List<User> users = userservice.getAll();

            // Vérifier si userList est null avant de l'utiliser
            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            // Mettre à jour userList
            userList.setAll(users.stream()
                    .map(user -> user.getNom() + " | " + user.getPrenom() + " | " + user.getCin())
                    .collect(Collectors.toList()));

            // Vérifier si filteredList est null avant de l'utiliser
            if (filteredList == null) {
                filteredList = new FilteredList<>(userList, p -> true);
                listview.setItems(filteredList); // Lier la liste filtrée à la ListView une seule fois
            }

            // Appliquer les filtres déjà présents
            chercherUser();

        } catch (Exception e) {
            messagelist.setText("Error loading users: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    void addUser(ActionEvent event) {
        // Vérifier que tous les champs sont remplis
        if (cin.getText().isEmpty() || nom.getText().isEmpty() || prenom.getText().isEmpty()
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
                cinValue, java.sql.Date.valueOf(date.getValue()),
                role.getValue());

        try {
            userservice.create(user);
            reset();
            initialize();
            message.setText("User created successfully");
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
            loadUsers();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editUser.fxml"));
            Parent root = loader.load();
            EditUser editController = loader.getController();
            editController.setUserData(selectedUser);
            nom.getScene().setRoot(root);

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
        this.selectedUser= null;
    }
}

