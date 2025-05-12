package Services;

import Models.Tache;
import utils.MyDb;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TacheService implements CrudInterface<Tache> {
    private Connection con;

    public TacheService() {
        this.con = MyDb.getMydb().getConnection();
    }

    /**
     * Create a new task by selecting the user (nom, prenom) and project (titre).
     */


    @Override
    public void create(Tache obj) throws Exception {
        String getUserIdSQL = "SELECT id FROM user WHERE LOWER(nom) = LOWER(?) AND LOWER(prenom) = LOWER(?)";
        String insertSQL = "INSERT INTO tache (titre, description, idProjet, idUser, date) VALUES (?, ?, ?, ?, ?)";

        try (
                PreparedStatement getUserStmt = con.prepareStatement(getUserIdSQL);
                PreparedStatement insertStmt = con.prepareStatement(insertSQL)
        ) {
            // Step 1: Get User ID
            getUserStmt.setString(1, obj.getNom());
            getUserStmt.setString(2, obj.getPrenom());
            ResultSet userRs = getUserStmt.executeQuery();

            if (!userRs.next()) {
                throw new Exception("User not found. Ensure nom and prenom match exactly.");
            }
            int idUser = userRs.getInt("id");

            // Step 2: Use the provided project id directly
            int idProjet = obj.getIdProjet();
            if (idProjet == 0) {
                throw new Exception("Project id is not set.");
            }

            // Step 3: Insert Task
            insertStmt.setString(1, obj.getTitre());
            insertStmt.setString(2, obj.getDescription());
            insertStmt.setInt(3, idProjet);
            insertStmt.setInt(4, idUser);
            insertStmt.setInt(5, obj.getDate()); // Ensure date is an integer

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
        String updateSQL = "UPDATE tache SET titre = ?, description = ?, idProjet = ?, idUser = ?, date = ? WHERE idTache = ?";

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
            updateStmt.setInt(5, obj.getDate());
            updateStmt.setInt(6, obj.getIdTache());


            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new Exception("No task updated. Ensure task ID is correct.");
            }

            System.out.println("Task updated successfully!");
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
    }
    public void updateTaskStatus(int taskId, String newStatus) {
        try {
            String query;

            if ("done".equalsIgnoreCase(newStatus)) {
                query = "UPDATE tache SET status = ?, completed_at = ? WHERE idTache = ?";
            } else {
                query = "UPDATE tache SET status = ? WHERE idTache = ?";
            }

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, newStatus);

            if ("done".equalsIgnoreCase(newStatus)) {
                stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setInt(3, taskId);
            } else {
                stmt.setInt(2, taskId);
            }

            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error updating task status: " + e.getMessage());
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

    public List<Tache> getTasksForUserInProject(int userId, int projectId) {
        List<Tache> tasks = new ArrayList<>();
        try {
            String query = "SELECT t.idTache, t.titre, t.description, t.idProjet, t.idUser, " +
                    "u.nom, u.prenom, p.titre AS TitreProjet, t.date, t.status " +
                    "FROM tache t " +
                    "JOIN user u ON t.idUser = u.id " +  // Join to get user details
                    "JOIN projet p ON t.idProjet = p.idProjet " + // Join to get project title
                    "WHERE t.idUser = ? AND t.idProjet = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setInt(2, projectId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tasks.add(new Tache(
                        rs.getInt("idTache"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getInt("idProjet"),
                        rs.getInt("idUser"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("TitreProjet"),
                        rs.getInt("date"),  // Fetching date
                        rs.getString("status") // Fetching status
                ));
            }
        } catch (Exception e) {
            System.out.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
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

    public List<Tache> getAllByProject(int idProjet) throws Exception {
        String sql = "SELECT t.idTache, t.titre, t.description, t.idUser,t.date,t.status, " +
                "p.titre AS titreProjet, u.nom AS nom, u.prenom AS prenom " +
                "FROM tache t " +
                "JOIN projet p ON t.idProjet = p.idProjet " +
                "JOIN user u ON t.idUser = u.id " +
                "WHERE t.idProjet = ?";

        List<Tache> tasks = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, idProjet);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Tache task = new Tache();
                    task.setIdTache(rs.getInt("idTache"));
                    task.setTitre(rs.getString("titre"));
                    task.setDescription(rs.getString("description"));
                    task.setIdUser(rs.getInt("idUser"));
                    task.setTitreProjet(rs.getString("titreProjet")); // Only storing the project title
                    task.setNom(rs.getString("nom"));
                    task.setPrenom(rs.getString("prenom"));
                    task.setDate(rs.getInt("date"));
                    task.setStatus(rs.getString("status"));
                    tasks.add(task);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Database error: " + e.getMessage(), e);
        }
        return tasks;
    }


    public List<String> getAllUserNamesWithRoles() throws Exception {
        String sql = "SELECT nom, prenom, role FROM user";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<String> userNames = new ArrayList<>();
        while (rs.next()) {
            String nom = rs.getString("nom").trim();
            String prenom = rs.getString("prenom").trim();
            String role = rs.getString("role").trim();

            if (!nom.isEmpty() && !prenom.isEmpty() && !role.isEmpty()) {
                userNames.add(nom + " " + prenom + " (" + role + ")");
            }
        }
        return userNames;
    }




}
