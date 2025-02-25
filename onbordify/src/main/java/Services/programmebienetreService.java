package Services;

import models.programmebienetre;
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
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ? AND email = ?";
        String insertSQL = "INSERT INTO programmebienetre (titre, type, description, idUser) VALUES (?, ?, ?, ?)";

        PreparedStatement getUserStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Get the idUser from the user table
            getUserStmt = conn.prepareStatement(getUserIdSQL);
            getUserStmt.setString(1, programme.getNom());
            getUserStmt.setString(2, programme.getPrenom());
            getUserStmt.setString(3, programme.getemail());
            rs = getUserStmt.executeQuery();

            if (rs.next()) {
                int idUser = rs.getInt("id");  // Get the user ID

                // Step 2: Insert programme with retrieved idUser
                insertStmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, programme.getTitre());
                insertStmt.setString(2, programme.getType());
                insertStmt.setString(3, programme.getDescription());
                insertStmt.setInt(4, idUser);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new Exception("Programme insertion failed. No rows affected.");
                }

                // Récupérer l'ID généré
                try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        programme.setIdProgramme(generatedKeys.getInt(1));
                    }
                }

                System.out.println("Programme ajouté avec succès !");
            } else {
                throw new Exception("Error: User not found.");
            }
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        } finally {
            // Close resources properly
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (getUserStmt != null) try { getUserStmt.close(); } catch (SQLException ignored) {}
            if (insertStmt != null) try { insertStmt.close(); } catch (SQLException ignored) {}
        }
    }

    @Override
    public void update(programmebienetre programme) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ? AND email = ?";
        String checkProgrammeSQL = "SELECT COUNT(*) FROM programmebienetre WHERE idProgramme = ?";
        String updateSQL = "UPDATE programmebienetre SET titre = ?, type = ?, description = ?, idUser = ? WHERE idProgramme = ?";

        try (
                PreparedStatement getUserStmt = conn.prepareStatement(getUserIdSQL);
                PreparedStatement checkStmt = conn.prepareStatement(checkProgrammeSQL);
                PreparedStatement updateStmt = conn.prepareStatement(updateSQL)
        ) {

            getUserStmt.setString(1, programme.getNom());
            getUserStmt.setString(2, programme.getPrenom());
            getUserStmt.setString(3, programme.getemail());

            try (ResultSet rs = getUserStmt.executeQuery()) {
                if (rs.next()) {
                    int idUser = rs.getInt("id"); // Fetch User ID

                    // Check if the programme exists
                    checkStmt.setInt(1, programme.getIdProgramme());
                    try (ResultSet checkRs = checkStmt.executeQuery()) {
                        if (checkRs.next() && checkRs.getInt(1) == 0) {
                            throw new Exception("Programme with ID " + programme.getIdProgramme() + " does not exist.");
                        }
                    }

                    // Update the programme
                    updateStmt.setString(1, programme.getTitre());
                    updateStmt.setString(2, programme.getType());
                    updateStmt.setString(3, programme.getDescription());
                    updateStmt.setInt(4, idUser);
                    updateStmt.setInt(5, programme.getIdProgramme());

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new Exception("No programme updated. Ensure the programme ID is correct.");
                    } else {
                        System.out.println("Programme mis à jour avec succès !");
                    }
                } else {
                    throw new Exception("User not found.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Database error occurred: " + e.getMessage(), e);
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
        String sql = "SELECT p.idProgramme, p.titre, p.type, p.description, p.idUser, u.nom AS nom, u.prenom AS prenom, u.email AS email " +
                "FROM programmebienetre p " +
                "JOIN user u ON p.idUser = u.id";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            List<programmebienetre> programmes = new ArrayList<>();

            while (rs.next()) {
                programmebienetre programme = new programmebienetre(
                        rs.getInt("idProgramme"),
                        rs.getString("titre"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getInt("idUser"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email")
                );
                programmes.add(programme);
            }
            return programmes;
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des programmes : " + e.getMessage());
        }
    }

    /**
     * Get all available admins (for UI dropdown).
     * Only users with role 'admin' are returned.
     */
    public List<String> getAllAdminNames() throws Exception {
        String sql = "SELECT nom, prenom, email FROM user WHERE role = 'admin'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<String> adminNames = new ArrayList<>();
            while (rs.next()) {
                adminNames.add(rs.getString("nom") + " " + rs.getString("prenom") + " " + rs.getString("email")); // Combine nom + prenom + email
            }
            return adminNames;
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des noms d'admin : " + e.getMessage());
        }
    }
}