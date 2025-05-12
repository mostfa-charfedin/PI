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

    @FXML private TextField cin;
    @FXML private Label message;
    @FXML private Label messagelist;
    @FXML private DatePicker date;
    @FXML private TextField email;
    @FXML private ListView<String> listview;
    @FXML private TextField nom;
    @FXML private TextField prenom;
    @FXML private TextField num_phone;
    @FXML private TextField rechercheFildMod;
    @FXML private ComboBox<Role> role;
    @FXML private ComboBox<Role> roleFilter;
    @FXML private VBox prenomErrorBox;
    @FXML private VBox nomErrorBox;
    @FXML private VBox numErrorBox;
    @FXML private VBox emailErrorBox;
    @FXML private VBox cinErrorBox;
    @FXML private VBox dateNaissanceErrorBox;

    private User selectedUser;
    private ObservableList<String> userList;
    private FilteredList<String> filteredList;

    @FXML
    public void initialize() {
        setupRoleComboBoxes();
        loadUsers(null);
        setupSearchListener();
        setupListViewSelection();
        setupInputValidators();
    }

    private void setupRoleComboBoxes() {
        role.setItems(FXCollections.observableArrayList(Role.values()));
        ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
        roleFilter.setItems(roles);
        roleFilter.getItems().add(0, null);
        roleFilter.setValue(null);
        roleFilter.setOnAction(event -> filterByRole());
    }

    private void setupSearchListener() {
        rechercheFildMod.textProperty().addListener((observable, oldValue, newValue) -> {
            chercherUser();
        });
    }

    private void setupListViewSelection() {
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

    private void setupInputValidators() {
        // CIN validation (8 digits)
        cin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cin.setText(oldValue);
                showError(cinErrorBox, "CIN must contain only numbers");
            } else if (newValue.length() > 8) {
                cin.setText(oldValue);
                showError(cinErrorBox, "CIN must be exactly 8 digits");
            } else {
                clearError(cinErrorBox);
            }
        });

        // Phone number validation (8 digits)
        num_phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                num_phone.setText(oldValue);
                showError(numErrorBox, "Phone must contain only numbers");
            } else if (newValue.length() > 8) {
                num_phone.setText(oldValue);
                showError(numErrorBox, "Phone must be exactly 8 digits");
            } else {
                clearError(numErrorBox);
            }
        });

        // Name validation (letters only)
        nom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                nom.setText(oldValue);
                showError(nomErrorBox, "Name must contain only letters");
            } else if (newValue.length() > 50) {
                showError(nomErrorBox, "Name too long (max 50 characters)");
            } else {
                clearError(nomErrorBox);
            }
        });

        // First name validation (letters only)
        prenom.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-ZÀ-ÿ\\s]*")) {
                prenom.setText(oldValue);
                showError(prenomErrorBox, "First name must contain only letters");
            } else if (newValue.length() > 50) {
                showError(prenomErrorBox, "First name too long (max 50 characters)");
            } else {
                clearError(prenomErrorBox);
            }
        });

        // Email validation
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isValidEmail(newValue)) {
                showError(emailErrorBox, "Invalid email format");
            } else {
                clearError(emailErrorBox);
            }
        });

        // Birth date validation (minimum age 18)
        date.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int age = Period.between(newValue, LocalDate.now()).getYears();
                if (age < 18) {
                    date.setValue(oldValue);
                    showError(dateNaissanceErrorBox, "Minimum age is 18 years");
                } else {
                    clearError(dateNaissanceErrorBox);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private void showError(VBox errorBox, String message) {
        errorBox.getChildren().clear();
        Label errorLabel = new Label(message);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        errorBox.getChildren().add(errorLabel);
    }

    private void clearError(VBox errorBox) {
        errorBox.getChildren().clear();
    }

    public void loadUsers(Role roleFilter) {
        try {
            List<User> users = userservice.getAll();

            if (userList == null) {
                userList = FXCollections.observableArrayList();
            }

            List<User> filteredUsers = users.stream()
                    .filter(user -> roleFilter == null || user.getRole() == roleFilter)
                    .collect(Collectors.toList());

            userList.setAll(filteredUsers.stream()
                    .map(user -> String.format("%s | %s | %d | %s",
                            user.getNom(), user.getPrenom(), user.getCin(), user.getEmail()))
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
        loadUsers(roleFilter.getValue());
    }

    @FXML
    void addUser(ActionEvent event) {
        if (!validateAllFields()) {
            message.setText("Please correct the errors in the form");
            message.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            User user = new User(
                    nom.getText().trim(),
                    prenom.getText().trim(),
                    email.getText().trim(),
                    Integer.parseInt(cin.getText().trim()),
                    java.sql.Date.valueOf(date.getValue()),
                    role.getValue(),
                    Integer.parseInt(num_phone.getText().trim())
            );

            userservice.create(user);
            reset();
            initialize();
            message.setText("User created successfully");
            message.setStyle("-fx-text-fill: #4CAF50;");
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        // Check required fields
        if (cin.getText().trim().isEmpty()) {
            showError(cinErrorBox, "CIN is required");
            isValid = false;
        }

        if (nom.getText().trim().isEmpty()) {
            showError(nomErrorBox, "Name is required");
            isValid = false;
        }

        if (prenom.getText().trim().isEmpty()) {
            showError(prenomErrorBox, "First name is required");
            isValid = false;
        }

        if (email.getText().trim().isEmpty()) {
            showError(emailErrorBox, "Email is required");
            isValid = false;
        } else if (!isValidEmail(email.getText().trim())) {
            showError(emailErrorBox, "Invalid email format");
            isValid = false;
        }

        if (num_phone.getText().trim().isEmpty()) {
            showError(numErrorBox, "Phone is required");
            isValid = false;
        }

        if (date.getValue() == null) {
            showError(dateNaissanceErrorBox, "Birth date is required");
            isValid = false;
        }

        if (role.getValue() == null) {
            showAlert("Error", "Role is required");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    void chercherUser() {
        String searchText = rechercheFildMod.getText().toLowerCase();
        filteredList.setPredicate(userString ->
                searchText == null || searchText.isEmpty() ||
                        userString.toLowerCase().contains(searchText)
        );
    }

    @FXML
    void deleteUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Please select a user first");
            return;
        }

        try {
            userservice.delete(selectedUser.getId());
            loadUsers(roleFilter.getValue());
            messagelist.setText("User deleted successfully");
        } catch (Exception e) {
            showAlert("Error", "Failed to delete user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void updateUser(ActionEvent event) {
        if (selectedUser == null) {
            messagelist.setText("Please select a user to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editUser.fxml"));
            Parent root = loader.load();

            EditUser editUserController = loader.getController();
            editUserController.setUserData(selectedUser);
            editUserController.setGestionUserController(this);
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            showAlert("Error", "Failed to load edit form: " + e.getMessage());
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
        nom.clear();
        prenom.clear();
        email.clear();
        cin.clear();
        num_phone.clear();
        date.setValue(null);
        role.setValue(null);
        selectedUser = null;
        clearAllErrors();
    }

    private void clearAllErrors() {
        clearError(cinErrorBox);
        clearError(nomErrorBox);
        clearError(prenomErrorBox);
        clearError(emailErrorBox);
        clearError(numErrorBox);
        clearError(dateNaissanceErrorBox);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}