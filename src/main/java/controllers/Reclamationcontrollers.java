package controllers;

import Services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Commentaire;
import models.Reclamation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Reclamationcontrollers {
ReclamationService reclamationService= new ReclamationService();
private Reclamation selectedreclamation;

    // FXML Elements
    @FXML
    private Button UpdateButton;

    @FXML
    private TextField claim;

    @FXML
    private TextField date;

    @FXML
    private TextField firstname;

    @FXML
    private TextField lastname;

    @FXML
    private ListView<String> listclaim;

    @FXML
    private TextField state;

    @FXML
    private TextField userid;

    @FXML
    void handleUpdate(ActionEvent event) {

    }

    // List to store Reclamation objects (temporary storage)
    private List<Reclamation> reclamations = new ArrayList<>();

    // Initialize method (optional)
    @FXML
    public void initialize() {

        loadreclamation();

        listclaim.setOnMouseClicked(event -> {
            int selectedIndex = listclaim.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                try {
                    this.selectedreclamation = reclamationService.listerReclamations().get(selectedIndex);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void loadreclamation() {
    }

    private void loadUsers() {
        listclaim.getItems().clear();
        try {
            List<Reclamation> reclamations = reclamationService.listerReclamations();

            for (Reclamation R : reclamations) {
                listclaim.getItems().add(R.getIdUser() + " | " + R.getCommentaire() + " | " + R.getDate());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Handle Submit Button Action
    @FXML
    private void handleSubmitButtonAction() {
        // Get values from the form
        int userId = Integer.parseInt(userId.TextField.getText());
        String commentaire = commentaire.TextField.getText();
        LocalDate date = LocalDate.getvalue();

        // Create a new Reclamation object
        Reclamation reclamation = new Reclamation(0, commentaire, date, userId, false); // ID and etat are placeholders

        // Add the reclamation to the list (or save to database)
        reclamations.add(reclamation);

        // Print the reclamation to the console (for testing)
        System.out.println("Reclamation created: " + reclamation);

        // Clear the form fields
        clearForm();
    }

    // Handle Close Button Action
    @FXML
    private void handleCloseButtonAction() {
        // Close the window or navigate to another view
        System.out.println("Close button clicked.");
        // You can add logic to close the window or switch scenes here
    }

    // Clear the form fields
    private void clearForm() {
        USER_ID.TextField.clear();
        commentaire.TextField.clear();
        DateDatePicker.setValue(null);
    }

    // CRUD Operations

    // Create a new Reclamation
    public void createReclamation(Reclamation reclamation) {
        reclamations.add(reclamation);
        System.out.println("Reclamation created: " + reclamation);
    }

    // Read all Reclamations
    public List<Reclamation> readAllReclamations() {
        return reclamations;
    }

    // Read a Reclamation by ID
    public Reclamation readReclamationById(int id) {
        for (Reclamation reclamation : reclamations) {
            if (reclamation.getIdReclamation() == id) {
                return reclamation;
            }
        }
        return null; // Return null if not found
    }

    // Update a Reclamation
    public void updateReclamation(int id, String newCommentaire, LocalDate newDate, boolean newEtat) {
        for (Reclamation reclamation : reclamations) {
            if (reclamation.getIdReclamation() == id) {
                reclamation.setCommentaire(newCommentaire);
                reclamation.setDate(newDate);
                reclamation.setEtat(newEtat);
                System.out.println("Reclamation updated: " + reclamation);
                return;
            }
        }
        System.out.println("Reclamation not found with ID: " + id);
    }

    // Delete a Reclamation
    public void deleteReclamation(int id) {
        reclamations.removeIf(reclamation -> reclamation.getIdReclamation() == id);
        System.out.println("Reclamation deleted with ID: " + id);
    }
}