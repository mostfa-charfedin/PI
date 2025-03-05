package Services;

import Models.Avis;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AvisService {
    private Connection conn;

    public AvisService() {
        this.conn = MyDb.getMydb().getConnection();
    }

    // ➤ Ajouter ou mettre à jour un avis
    public void ajouterAvis(int idProgramme, int idUser, int rating, String commentaire) throws SQLException {
        String sql = "INSERT INTO avis (idProgramme, idUser, rating , commentaire) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProgramme);
            stmt.setInt(2, idUser);
            stmt.setInt(3, rating);
            stmt.setString(4, commentaire);
            stmt.executeUpdate();
            System.out.println("Avis ajouté avec succès !");
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de l'ajout de l'avis : " + e.getMessage());
        }
    }

    // ➤ Récupérer les avis d'un programme
    public List<Avis> getAvisParProgramme(int idProgramme) {
        List<Avis> avisList = new ArrayList<>();
        String sql = "SELECT * FROM avis WHERE idProgramme = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Avis avis = new Avis(
                        rs.getInt("idProgramme"),
                        rs.getInt("idUser"),
                        rs.getInt("rating"),
                        rs.getString("commentaire")
                );
                avis.setId(rs.getInt("id"));
                avisList.add(avis);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des avis : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des avis", e);
        }
        return avisList;
    }
}