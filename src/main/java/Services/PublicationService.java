package Services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationService {
    private Connection connection;

    public PublicationService() {
        connection = DatabaseConnection.getConnection();
    }

    // Create
    public void ajouterPublication(Publication publication) {
        String query = "INSERT INTO Publication (body, idUser) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, publication.getBody());
            statement.setInt(2, publication.getIdUser());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Toutes les publications)
    public List<Publication> listerPublications() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publications;
    }

    // Update
    public void mettreAJourPublication(Publication publication) {
        String query = "UPDATE Publication SET body = ?, idUser = ? WHERE idPublication = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, publication.getBody());
            statement.setInt(2, publication.getIdUser());
            statement.setInt(3, publication.getIdPublication());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void supprimerPublication(int idPublication) {
        String query = "DELETE FROM Publication WHERE idPublication = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idPublication);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}