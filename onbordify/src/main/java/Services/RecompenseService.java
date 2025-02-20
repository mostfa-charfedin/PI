package Services;

import models.Recompense;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecompenseService implements CrudInterface<Recompense> {

    private MyDb DatabaseConnection;

    // Initialisation correcte dans le constructeur
    public RecompenseService() {
        DatabaseConnection = MyDb.getInstance();
    }


    @Override
    public void create(Recompense recompense) throws Exception {
        String sql = "INSERT INTO recompense (type, dateAttribution, statusRecompence, idProgramme) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setDate(2, java.sql.Date.valueOf(recompense.getDateAttribution()));
            stmt.setString(3, recompense.getStatusRecompence());
            stmt.setInt(4, recompense.getIdProgramme());
            stmt.executeUpdate();
            System.out.println("Récompense ajoutée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de l'ajout de la récompense : " + e.getMessage());
        }
    }
    @Override
    public void update(Recompense recompense) throws Exception {
        String sql = "UPDATE recompense SET type = ?, dateAttribution = ?, statusRecompence = ? WHERE idRecompense = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setDate(2, java.sql.Date.valueOf(recompense.getDateAttribution()));
            stmt.setString(3, recompense.getStatusRecompence()); // Utilisez le bon nom de colonne
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Recompense recompense = new Recompense(
                        rs.getInt("idRecompense"),
                        rs.getString("type"),
                        rs.getDate("dateAttribution").toLocalDate(),
                        rs.getString("statusRecompence"), // Utilisez le bon nom de colonne
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }}