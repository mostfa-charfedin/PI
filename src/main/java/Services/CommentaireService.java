package Services;

import models.Commentaire;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {
    private Connection connection;

    public CommentaireService(Connection connection) {
        this.connection = connection;
    }

    // Ajouter un commentaire
    public void ajouterCommentaire(Commentaire commentaire) throws SQLException {
        String query = "INSERT INTO Commentaire (message, idPublication, idUser) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, commentaire.getMessage());
            statement.setInt(2, commentaire.getIdPublication());
            statement.setInt(3, commentaire.getIdUser());
            statement.executeUpdate();
        }
    }

    // Récupérer tous les commentaires
    public List<Commentaire> listerCommentaires() throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM Commentaire";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setIdCommentaire(resultSet.getInt("idCommentaire"));
                commentaire.setMessage(resultSet.getString("message"));
                commentaire.setIdPublication(resultSet.getInt("idPublication"));
                commentaire.setIdUser(resultSet.getInt("idUser"));
                commentaires.add(commentaire);
            }
        }
        return commentaires;
    }

    // Récupérer les commentaires d'une publication spécifique
    public List<Commentaire> getCommentairesByPublication(int idPublication) throws SQLException {
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
        }
        return commentaires;
    }

    // Supprimer un commentaire
    public void supprimerCommentaire(int idCommentaire) throws SQLException {
        String query = "DELETE FROM Commentaire WHERE idCommentaire = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idCommentaire);
            statement.executeUpdate();
        }
    }
}