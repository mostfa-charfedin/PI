package Services;

import models.programmebienetre;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class programmebienetreService implements CrudInterface<programmebienetre> {

    private MyDb DatabaseConnection;

    public programmebienetreService() {
        DatabaseConnection = MyDb.getInstance();
    }

    @Override
    public void create(programmebienetre programme) throws Exception {
        String sql = "INSERT INTO programmebienetre (titre, type, description) VALUES (?, ?, ?)";
        try (Connection conn = MyDb.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, programme.getTitre());
            stmt.setString(2, programme.getType());
            stmt.setString(3, programme.getDescription());
            stmt.executeUpdate();
            System.out.println("Programme ajouté avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'ajout du programme : " + e.getMessage());
        }
    }

    @Override
    public void update(programmebienetre programme) throws Exception {
        String sql = "UPDATE programmebienetre SET titre = ?, type = ?, description = ? WHERE idProgramme = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, programme.getTitre());
            stmt.setString(2, programme.getType());
            stmt.setString(3, programme.getDescription());
            stmt.setInt(4, programme.getIdProgramme());
            stmt.executeUpdate();
            System.out.println("Programme mis à jour avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM programmebienetre WHERE idProgramme = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Programme supprimé avec succès !");
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la suppression : " + e.getMessage());
        }
    }


    @Override
    public List<programmebienetre> getAll() throws Exception {
        List<programmebienetre> programmes = new ArrayList<>();
        String sql = "SELECT * FROM programmebienetre";

        // Création d'une nouvelle connexion pour cette méthode
        try (Connection conn = MyDb.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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

}
