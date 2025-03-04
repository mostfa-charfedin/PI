package Services;

import Models.Projet;
import utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetService implements CrudInterface<Projet> {
    private Connection con;

    public ProjetService() {
        this.con = MyDb.getInstance().getConnection();
    }

    /**
     * Create a new project by selecting the chef de projet using their nom and prenom.
     * The selected user must have the role 'chefprojet'.
     */
    @Override
    public void create(Projet obj) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ? ";
        String insertSQL = "INSERT INTO projet (titre, Description, idUser) VALUES (?, ?, ?)";

        PreparedStatement getUserStmt = null;
        PreparedStatement insertStmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Get the idUser from the user table
            getUserStmt = con.prepareStatement(getUserIdSQL);
            getUserStmt.setString(1, obj.getNom());
            getUserStmt.setString(2, obj.getPrenom());
            rs = getUserStmt.executeQuery();

            if (rs.next()) {
                int idUser = rs.getInt("id");  // Get the user ID

                // Step 2: Insert project with retrieved idUser
                insertStmt = con.prepareStatement(insertSQL);
                insertStmt.setString(1, obj.getTitre());
                insertStmt.setString(2, obj.getDescription());
                insertStmt.setInt(3, idUser);

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new Exception("Project insertion failed. No rows affected.");
                }

                System.out.println("Project created successfully!");
            } else {
                throw new Exception("Error: Chef de projet not found or does not have the required role.");
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


    /**
     * Update an existing project.
     * The selected user must have the role 'chefprojet'.
     */
    @Override
    public void update(Projet obj) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ?";
        String checkProjectSQL = "SELECT COUNT(*) FROM projet WHERE idProjet = ?";
        String updateSQL = "UPDATE projet SET titre = ?, Description = ?, idUser = ? WHERE idProjet = ?";

        try (
                PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
                PreparedStatement checkStmt = con.prepareStatement(checkProjectSQL);
                PreparedStatement updateStmt = con.prepareStatement(updateSQL)
        ) {
            // Get User ID
            getUserStmt.setString(1, obj.getNom());
            getUserStmt.setString(2, obj.getPrenom());

            try (ResultSet rs = getUserStmt.executeQuery()) {
                if (rs.next()) {
                    int idUser = rs.getInt("id"); // Fetch user ID

                    // Check if the project exists
                    checkStmt.setInt(1, obj.getIdProjet());
                    try (ResultSet checkRs = checkStmt.executeQuery()) {
                        if (checkRs.next() && checkRs.getInt(1) == 0) {
                            throw new Exception("Project with ID " + obj.getIdProjet() + " does not exist.");
                        }
                    }

                    // Update the project
                    updateStmt.setString(1, obj.getTitre());
                    updateStmt.setString(2, obj.getDescription());
                    updateStmt.setInt(3, idUser);
                    updateStmt.setInt(4, obj.getIdProjet());

                    int rowsAffected = updateStmt.executeUpdate();
                    if (rowsAffected == 0) {
                        throw new Exception("No project updated. Ensure the project ID is correct.");
                    } else {
                        System.out.println("Project updated successfully!");
                    }
                } else {
                    throw new Exception("Chef de projet not found.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Database error occurred: " + e.getMessage(), e);
        }
    }



    /**
     * Delete a project by its ID.
     */
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM projet WHERE idProjet = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    /**
     * Fetch all projects, including the nom and prenom of the project manager.
     */
    @Override
    public List<Projet> getAll() throws Exception {
        String sql = "SELECT p.idProjet, p.titre, p.Description, p.idUser, u.nom AS nom, u.prenom AS prenom " +
                "FROM projet p " +
                "JOIN user u ON p.idUser = u.id";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<Projet> projets = new ArrayList<>();
        while (rs.next()) {
            Projet projet = new Projet();
            projet.setIdProjet(rs.getInt("idProjet"));
            projet.setTitre(rs.getString("titre"));
            projet.setDescription(rs.getString("Description"));
            projet.setidUser(rs.getInt("idUser"));
            projet.setNom(rs.getString("nom"));
            projet.setPrenom(rs.getString("prenom"));

            projets.add(projet);
        }
        return projets;
    }

    /**
     * Get a project by its ID.
     */
    public Projet getById(int id) throws Exception {
        String sql = "SELECT p.idProjet, p.titre, p.Description, p.idUser, u.nom AS nom, u.prenom AS prenom " +
                "FROM projet p " +
                "JOIN user u ON p.idUser = u.id " +
                "WHERE p.idProjet = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Projet projet = new Projet();
            projet.setIdProjet(rs.getInt("idProjet"));
            projet.setTitre(rs.getString("titre"));
            projet.setDescription(rs.getString("Description"));
            projet.setidUser(rs.getInt("idUser"));
            projet.setNom(rs.getString("nom"));
            projet.setPrenom(rs.getString("prenom"));
            return projet;
        }
        return null; // Return null if no project found
    }

    /**
     * Get all available project managers (for UI dropdown).
     * Only users with role 'chefprojet' are returned.
     */
    public List<String> getAllChefProjetNames() throws Exception {
        String sql = "SELECT nom, prenom FROM user WHERE role = 'chefprojet'";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<String> chefNames = new ArrayList<>();
        while (rs.next()) {
            chefNames.add(rs.getString("nom") + " " + rs.getString("prenom")); // Combine nom + prenom
        }
        return chefNames;
    }

}
