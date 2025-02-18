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
        String sql = "INSERT INTO recompense (type_Recompense, dateAttribution, statutdeRecompense, idProgramme) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setString(2, recompense.getDateAttribution());
            stmt.setString(3, recompense.getStatutdeRecompense());
            stmt.setInt(4, recompense.getIdProgramme());
            stmt.executeUpdate();
            System.out.println("Récompense ajoutée avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'ajout de la récompense : " + e.getMessage());
        }
    }

    @Override
    public void update(Recompense recompense) throws Exception {
        String sql = "UPDATE recompense SET type_Recompense = ?, dateAttribution = ?, statutdeRecompense = ? WHERE idRecompense = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, recompense.getType_Recompense());
            stmt.setString(2, recompense.getDateAttribution());
            stmt.setString(3, recompense.getStatutdeRecompense());
            stmt.setInt(4, recompense.getIdRecompense());
            stmt.executeUpdate();
            System.out.println("Récompense mise à jour avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM recompense WHERE idRecompense = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Récompense supprimée avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la suppression : " + e.getMessage());
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
                        rs.getString("dateAttribution"),
                        rs.getString("statusRecompense"),
                        rs.getInt("idProgramme")
                );
                recompenses.add(recompense);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des récompenses : " + e.getMessage());
        }
        return recompenses;
    }
}
