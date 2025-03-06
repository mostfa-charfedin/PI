package Services;

import Models.programmebienetre;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class programmebienetreService implements CrudInterface<programmebienetre> {

    private final Connection conn;

    public programmebienetreService() {
        this.conn = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(programmebienetre programme) throws Exception {
        String sql = "INSERT INTO programmebienetre (titre, type, description) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, programme.getTitre());
            stmt.setString(2, programme.getType());
            stmt.setString(3, programme.getDescription());
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    programme.setIdProgramme(generatedKeys.getInt(1));
                }
            }

            System.out.println("Programme ajouté avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'ajout du programme : " + e.getMessage());
        }
    }

    @Override
    public void update(programmebienetre programme) throws Exception {
        String sql = "UPDATE programmebienetre SET titre = ?, type = ?, description = ? WHERE idProgramme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programme.getTitre());
            stmt.setString(2, programme.getType());
            stmt.setString(3, programme.getDescription());
            stmt.setInt(4, programme.getIdProgramme());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("Aucune mise à jour effectuée, vérifiez l'ID !");
            }

            System.out.println("Programme mis à jour avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM programmebienetre WHERE idProgramme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new Exception("Aucun programme supprimé. Vérifiez si l'ID existe !");
            }

            System.out.println("Programme supprimé avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la suppression du programme : " + e.getMessage());
        }
    }

    @Override
    public List<programmebienetre> getAll() throws Exception {
        List<programmebienetre> programmes = new ArrayList<>();
        String sql = "SELECT * FROM programmebienetre";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                programmebienetre programme = new programmebienetre(
                        rs.getInt("idProgramme"),
                        rs.getString("titre"),
                        rs.getString("type"),
                        rs.getString("description")
                );
                programmes.add(programme);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des programmes : " + e.getMessage());
        }
        return programmes;
    }


    // Ajouter un avis
    public void ajouterAvis(int idProgramme, int idUser, int rating, String commentaire) throws SQLException {
        String sql = "INSERT INTO avis (idProgramme, idUser, rating, commentaire) " +
                "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE rating = VALUES(rating), commentaire = VALUES(commentaire)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            stmt.setInt(2, idUser);
            stmt.setInt(3, rating);
            stmt.setString(4, commentaire);
            stmt.executeUpdate();
            System.out.println("Avis ajouté ou mis à jour.");
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'ajout ou de la mise à jour de l'avis : " + e.getMessage());
        }
    }
    public int getTotalReviews(int idProgramme) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM avis WHERE idProgramme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0; // Retourne 0 si aucune donnée n'est trouvée
    }

    public double getAverageRating(int idProgramme) throws SQLException {
        String sql = "SELECT AVG(rating) AS average FROM avis WHERE idProgramme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                }
            }
        }
        return 0.0; // Retourne 0.0 si aucune donnée n'est trouvée
    }

public int getReviewCountByRating(int rating) throws SQLException {
    String query = "SELECT COUNT(*) FROM avis WHERE rating = ?";
    try (PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setInt(1, rating);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
    }
    return 0;
}
    public int getReviewCountByRatingForProgramme(int idProgramme, int rating) throws SQLException {
        String query = "SELECT COUNT(*) FROM avis WHERE idProgramme = ? AND rating = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, idProgramme);
            statement.setInt(2, rating);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
}
