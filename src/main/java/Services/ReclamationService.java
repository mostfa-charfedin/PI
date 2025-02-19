package Services;

import models.Reclamation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {
    private Connection connection;

    public ReclamationService(Connection connection) {
        this.connection = connection;
    }

    public void ajouterReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO Reclamation (Commentaire, date, idUser, etat) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reclamation.getCommentaire());
           // statement.setDate(2, reclamation.getDate());
            statement.setInt(3, reclamation.getIdUser());
            statement.setBoolean(4, reclamation.isEtat());
            statement.executeUpdate();
        }
    }

    public List<Reclamation> listerReclamations() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM Reclamation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setIdReclamation(resultSet.getInt("idReclamation"));
                reclamation.setCommentaire(resultSet.getString("Commentaire"));
               // reclamation.setDate(resultSet.getDate("date"));
                reclamation.setIdUser(resultSet.getInt("idUser"));
                reclamation.setEtat(resultSet.getBoolean("etat"));
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }
}