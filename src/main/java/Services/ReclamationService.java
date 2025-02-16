package services;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {
    private Connection connection;

    public ReclamationService() {
        // Initialiser la connexion à la base de données
        connection = DatabaseConnection.getConnection();
    }

    // Create
    public void ajouterReclamation(Reclamation reclamation) {
        String query = "INSERT INTO Reclamation (Commentaire, date, idUser, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reclamation.getCommentaire());
            statement.setDate(2, Date.valueOf(reclamation.getDate()));
            statement.setInt(3, reclamation.getIdUser());
            statement.setBoolean(4, reclamation.isEtat());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Toutes les réclamations)
    public List<Reclamation> listerReclamations() {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM Reclamation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setIdReclamation(resultSet.getInt("idReclamation"));
                reclamation.setCommentaire(resultSet.getString("Commentaire"));
                reclamation.setDate(resultSet.getDate("date").toLocalDate());
                reclamation.setIdUser(resultSet.getInt("idUser"));
                reclamation.setEtat(resultSet.getBoolean("etat"));
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reclamations;
    }

    // Update
    public void mettreAJourReclamation(Reclamation reclamation) {
        String query = "UPDATE Reclamation SET Commentaire = ?, date = ?, idUser = ?, etat = ? WHERE idReclamation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reclamation.getCommentaire());
            statement.setDate(2, Date.valueOf(reclamation.getDate()));
            statement.setInt(3, reclamation.getIdUser());
            statement.setBoolean(4, reclamation.isEtat());
            statement.setInt(5, reclamation.getIdReclamation());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void supprimerReclamation(int idReclamation) {
        String query = "DELETE FROM Reclamation WHERE idReclamation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idReclamation);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}