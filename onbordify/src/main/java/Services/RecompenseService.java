package Services;

import Models.Recompense;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecompenseService implements CrudInterface<Recompense> {

    private Connection connection;

    // Constructeur qui initialise la connexion
    public RecompenseService() {
        this.connection = MyDb.getMydb().getConnection();
    }

    @Override
    public void create(Recompense recompense) throws Exception {
        String sql = "INSERT INTO recompense (type, dateAttribution, statusRecompence, idProgramme) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setDate(2, java.sql.Date.valueOf(recompense.getDateAttribution()));
            stmt.setString(3, recompense.getStatusRecompence());
            stmt.setInt(4, recompense.getIdProgramme());
            stmt.executeUpdate();
            System.out.println("Récompense ajoutée avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'ajout de la récompense : " + e.getMessage());
        }
    }

    @Override
    public void update(Recompense recompense) throws Exception {
        String sql = "UPDATE recompense SET type = ?, dateAttribution = ?, statusRecompence = ? WHERE idRecompense = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setDate(2, java.sql.Date.valueOf(recompense.getDateAttribution()));
            stmt.setString(3, recompense.getStatusRecompence());
            stmt.setInt(4, recompense.getIdRecompense());
            stmt.executeUpdate();
            System.out.println("Récompense mise à jour avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public List<Recompense> getAll() throws Exception {
        List<Recompense> recompenses = new ArrayList<>();
        String sql = "SELECT * FROM recompense";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Recompense recompense = new Recompense(
                        rs.getInt("idRecompense"),
                        rs.getString("type"),
                        rs.getDate("dateAttribution").toLocalDate(),
                        rs.getString("statusRecompence"),
                        rs.getInt("idProgramme")
                );
                recompenses.add(recompense);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des récompenses : " + e.getMessage());
        }
        return recompenses;
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM recompense WHERE idRecompense = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Récompense supprimée avec succès !");
            } else {
                System.out.println("Aucune récompense trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la suppression de la récompense : " + e.getMessage());
        }
    }

    public boolean programmeExists(int idProgramme) throws SQLException {
        String sql = "SELECT COUNT(*) FROM programmebienetre WHERE idProgramme = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}