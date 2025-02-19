package Services;

import models.Publication;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationService {
    private Connection connection;

    public PublicationService(Connection connection) {
        this.connection = connection;
    }

    // Ajouter une publication
    public void ajouterPublication(Publication publication) throws SQLException {
        String query = "INSERT INTO Publication (body, idUser) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, publication.getBody());
            statement.setInt(2, publication.getIdUser());
            statement.executeUpdate();
        }
    }

    // Récupérer toutes les publications
    public List<Publication> listerPublications() throws SQLException {
        List<Publication> publications = new ArrayList<>();
        String query = "SELECT * FROM Publication";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Publication publication = new Publication();
                publication.setIdPublication(resultSet.getInt("idPublication"));
                publication.setBody(resultSet.getString("body"));
                publication.setIdUser(resultSet.getInt("idUser"));
                publications.add(publication);
            }
        }
        return publications;
    }

    // Récupérer une publication par son ID
    public Publication getPublicationById(int idPublication) throws SQLException {
        String query = "SELECT * FROM Publication WHERE idPublication = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPublication);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Publication publication = new Publication();
                publication.setIdPublication(resultSet.getInt("idPublication"));
                publication.setBody(resultSet.getString("body"));
                publication.setIdUser(resultSet.getInt("idUser"));
                return publication;
            }
        }
        return null;
    }

    // Supprimer une publication
    public void supprimerPublication(int idPublication) throws SQLException {
        String query = "DELETE FROM Publication WHERE idPublication = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPublication);
            statement.executeUpdate();
        }
    }
}