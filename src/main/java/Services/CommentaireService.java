package Services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService implements CrudInterface<CommentaireService>{
    private Connection connection;

    public CommentaireService() {
        connection = DatabaseConnection.getConnection();
    }

    // Create
    public void ajouterCommentaire(CommentaireService commentaire) {
        String query = "INSERT INTO Commentaire (message, idPublication, idUser) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getMessage());
            statement.setInt(2, commentaire.getIdPublication());
            statement.setInt(3, commentaire.getIdUser());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Tous les commentaires d'une publication)
    public List<Commentaire> listerCommentaires(int idPublication) {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM Commentaire WHERE idPublication = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPublication);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setIdCommentaire(resultSet.getInt("idCommentaire"));
                commentaire.setMessage(resultSet.getString("message"));
                commentaire.setIdPublication(resultSet.getInt("idPublication"));
                commentaire.setIdUser(resultSet.getInt("idUser"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commentaires;
    }

    // Update
    public void mettreAJourCommentaire(Commentaire commentaire) {
        String query = "UPDATE Commentaire SET message = ?, idPublication = ?, idUser = ? WHERE idCommentaire = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getMessage());
            statement.setInt(2, commentaire.getIdPublication());
            statement.setInt(3, commentaire.getIdUser());
            statement.setInt(4, commentaire.getIdCommentaire());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void supprimerCommentaire(int idCommentaire) {
        String query = "DELETE FROM Commentaire WHERE idCommentaire = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idCommentaire);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}