package Services;

import utils.MyDb;
import java.sql.*;
import java.time.LocalDate;

public class EvaluationService {
    private Connection connection;

    public EvaluationService() {
        connection = MyDb.getInstance().getConnection();
    }

    // Méthode pour ajouter ou mettre à jour l'évaluation d'une ressource
    public void ajouterOuMettreAJourEvaluation(int idResource, int 	idUser, double note) {
        String query = "INSERT INTO evaluation (idResource, id, note, dateEvaluation) " +
                "VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE note = VALUES(note), dateEvaluation = VALUES(dateEvaluation)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idResource);
            stmt.setInt(2, 	idUser);
            stmt.setDouble(3, note);
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
