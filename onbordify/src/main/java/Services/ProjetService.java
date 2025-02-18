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
        // Step 1: Get the idUser from the user table using nom, prenom, and role
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ? AND role = 'chefprojet'";
        PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
        //getUserStmt.setString(1, obj.getNom());
        //getUserStmt.setString(2, obj.getPrenom());
        ResultSet rs = getUserStmt.executeQuery();

        if (rs.next()) {
            int idUser = rs.getInt("id");  // Get the user ID

            // Step 2: Insert project with retrieved idUser
            String insertSQL = "INSERT INTO projet (titre, Description, idUser) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(insertSQL);
            stmt.setString(1, obj.getTitre());
            stmt.setString(2, obj.getDescription());
            stmt.setInt(3, idUser);
            stmt.executeUpdate();
        } else {
            throw new Exception("Chef de projet not found or does not have the required role.");
        }
    }

    /**
     * Update an existing project.
     * The selected user must have the role 'chefprojet'.
     */
    @Override
    public void update(Projet obj) throws Exception {
        // Convert nom and prenom to idUser
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ? AND role = 'chefprojet'";
        PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
        //getUserStmt.setString(1, obj.getNom());
        //getUserStmt.setString(2, obj.getPrenom());
        ResultSet rs = getUserStmt.executeQuery();

        if (rs.next()) {
            int idUser = rs.getInt("id");

            // Update project with new idUser
            String sql = "UPDATE projet SET titre = ?, Description = ?, idUser = ? WHERE idProjet = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, obj.getTitre());
            stmt.setString(2, obj.getDescription());
            stmt.setInt(3, idUser);
            stmt.setInt(4, obj.getIdProjet());
            stmt.executeUpdate();
        } else {
            throw new Exception("Chef de projet not found or does not have the required role.");
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
            //projet.setNom(rs.getString("nom"));
            //projet.setPrenom(rs.getString("prenom"));

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
            //projet.Nom(rs.getString("nom"));
            //projet.Prenom(rs.getString("prenom"));
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
