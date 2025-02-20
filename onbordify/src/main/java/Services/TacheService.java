package Services;

import Models.Tache;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TacheService implements CrudInterface<Tache> {
    private Connection con;

    public TacheService() {
        this.con = MyDb.getInstance().getConnection();
    }

    /**
     * Create a new task by selecting the user (nom, prenom) and project (titre).
     */
    @Override
    public void create(Tache obj) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ?";
        String getProjectIdSQL = "SELECT idProjet FROM projet WHERE titre = ?";
        String insertSQL = "INSERT INTO tache (titre, description, idProjet, idUser) VALUES (?, ?, ?, ?)";

        try (
                PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
                PreparedStatement getProjectStmt = con.prepareStatement(getProjectIdSQL);
                PreparedStatement insertStmt = con.prepareStatement(insertSQL)
        ) {
            // Step 1: Get User ID
            getUserStmt.setString(1, obj.getNom());
            getUserStmt.setString(2, obj.getPrenom());
            ResultSet userRs = getUserStmt.executeQuery();

            if (!userRs.next()) {
                throw new Exception("User not found.");
            }
            int idUser = userRs.getInt("id");

            // Step 2: Get Project ID
            getProjectStmt.setString(1, obj.getTitreProjet());
            ResultSet projectRs = getProjectStmt.executeQuery();

            if (!projectRs.next()) {
                throw new Exception("Project not found.");
            }
            int idProjet = projectRs.getInt("idProjet");

            // Step 3: Insert task
            insertStmt.setString(1, obj.getTitre());
            insertStmt.setString(2, obj.getDescription());
            insertStmt.setInt(3, idProjet);
            insertStmt.setInt(4, idUser);

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new Exception("Task insertion failed.");
            }

            System.out.println("Task created successfully!");
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Update an existing task.
     */
    @Override
    public void update(Tache obj) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE nom = ? AND prenom = ?";
        String getProjectIdSQL = "SELECT idProjet FROM projet WHERE titre = ?";
        String updateSQL = "UPDATE tache SET titre = ?, description = ?, idProjet = ?, idUser = ? WHERE idTache = ?";

        try (
                PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
                PreparedStatement getProjectStmt = con.prepareStatement(getProjectIdSQL);
                PreparedStatement updateStmt = con.prepareStatement(updateSQL)
        ) {
            // Get User ID
            getUserStmt.setString(1, obj.getNom());
            getUserStmt.setString(2, obj.getPrenom());
            ResultSet userRs = getUserStmt.executeQuery();

            if (!userRs.next()) {
                throw new Exception("User not found.");
            }
            int idUser = userRs.getInt("id");

            // Get Project ID
            getProjectStmt.setString(1, obj.getTitreProjet());
            ResultSet projectRs = getProjectStmt.executeQuery();

            if (!projectRs.next()) {
                throw new Exception("Project not found.");
            }
            int idProjet = projectRs.getInt("idProjet");

            // Update task
            updateStmt.setString(1, obj.getTitre());
            updateStmt.setString(2, obj.getDescription());
            updateStmt.setInt(3, idProjet);
            updateStmt.setInt(4, idUser);
            updateStmt.setInt(5, obj.getIdTache());

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("No task updated. Ensure task ID is correct.");
            }

            System.out.println("Task updated successfully!");
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Delete a task by its ID.
     */
    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM tache WHERE idTache = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new Exception("Task deletion failed. Task ID may not exist.");
            }
            System.out.println("Task deleted successfully!");
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Fetch all tasks, including the project title and user name.
     */
    @Override
    public List<Tache> getAll() throws Exception {
        String sql = "SELECT t.idTache, t.titre, t.description, t.idProjet, t.idUser, " +
                "p.titre AS titreProjet, u.nom AS nom, u.prenom AS prenom " +
                "FROM tache t " +
                "JOIN projet p ON t.idProjet = p.idProjet " +
                "JOIN user u ON t.idUser = u.id";

        List<Tache> tasks = new ArrayList<>();
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Tache task = new Tache();
                task.setIdTache(rs.getInt("idTache"));
                task.setTitre(rs.getString("titre"));
                task.setDescription(rs.getString("description"));
                task.setIdProjet(rs.getInt("idProjet"));
                task.setIdUser(rs.getInt("idUser"));
                task.setTitreProjet(rs.getString("titreProjet"));
                task.setNom(rs.getString("nom"));
                task.setPrenom(rs.getString("prenom"));

                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
        return tasks;
    }

    /**
     * Get a task by its ID.
     */
    public Tache getById(int id) throws Exception {
        String sql = "SELECT t.idTache, t.titre, t.description, t.idProjet, t.idUser, " +
                "p.titre AS titreProjet, u.nom AS nom, u.prenom AS prenom " +
                "FROM tache t " +
                "JOIN projet p ON t.idProjet = p.idProjet " +
                "JOIN user u ON t.idUser = u.id " +
                "WHERE t.idTache = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Tache task = new Tache();
                task.setIdTache(rs.getInt("idTache"));
                task.setTitre(rs.getString("titre"));
                task.setDescription(rs.getString("description"));
                task.setIdProjet(rs.getInt("idProjet"));
                task.setIdUser(rs.getInt("idUser"));
                task.setTitreProjet(rs.getString("titreProjet"));
                task.setNom(rs.getString("nom"));
                task.setPrenom(rs.getString("prenom"));

                return task;
            }
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
        return null; // Return null if no task found
    }
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
    public List<Tache> getTasksByProjectId(int projectId) throws Exception {
        String sql = "SELECT t.idTache, t.titre, t.Description, t.idProjet, t.idUser, " +
                "u.nom, u.prenom, p.titre AS TitreProjet " +
                "FROM tache t " +
                "JOIN user u ON t.idUser = u.id " +
                "JOIN projet p ON t.idProjet = p.idProjet " +
                "WHERE t.idProjet = ?";

        List<Tache> tasks = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, projectId); // Set the project ID parameter
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Tache task = new Tache();
                task.setIdTache(rs.getInt("idTache"));
                task.setTitre(rs.getString("titre"));
                task.setDescription(rs.getString("Description"));
                task.setIdProjet(rs.getInt("idProjet"));
                task.setIdUser(rs.getInt("idUser"));
                task.setNom(rs.getString("nom"));
                task.setPrenom(rs.getString("prenom"));
                task.setTitreProjet(rs.getString("TitreProjet"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            throw new Exception("Failed to fetch tasks for project ID " + projectId + ": " + e.getMessage());
        }

        return tasks;
    }
}
