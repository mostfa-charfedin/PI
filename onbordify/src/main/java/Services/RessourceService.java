package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Models.Ressource;
import utils.MyDb;

public class RessourceService implements CrudInterface<Ressource> {
    private Connection connection;

    public RessourceService() {
        connection = MyDb.getMydb().getConnection();
    }

    // CREATE
    @Override
    public void create(Ressource res) throws Exception {
        String sql = "INSERT INTO onboardify.ressources (titre, type, description, lien, noteAverage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, res.getTitre());
            stmt.setString(2, res.getType());
            stmt.setString(3, res.getDescription());
            stmt.setString(4, res.getlien());
            stmt.setDouble(5, res.getNoteAverage());
            stmt.executeUpdate();
        }
    }

    // UPDATE
    @Override
    public void update(Ressource res) throws Exception {
        String sql = "UPDATE onboardify.ressources SET titre = ?, type = ?, description = ?, lien = ?, noteAverage = ? WHERE idResource = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, res.getTitre());
            stmt.setString(2, res.getType());
            stmt.setString(3, res.getDescription());
            stmt.setString(4, res.getlien());
            stmt.setDouble(5, res.getNoteAverage());
            stmt.setInt(6, res.getIdResource());
            stmt.executeUpdate();
        }
    }

    // DELETE
    @Override
    public void delete(int IdResource) throws Exception {
        String sql = "DELETE FROM onboardify.ressources WHERE idResource = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, IdResource);
            stmt.executeUpdate();
        }
    }

    // GET NOTE AVERAGE FROM EVALUATION
    public double getNoteFromEvaluation(int idResource) {
        String query = "SELECT AVG(note) AS avgNote FROM onboardify.evaluation WHERE idResource = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idResource);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avgNote");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // GET EVALUATION COUNT
    public int getEvaluationCount(int resourceId) {
        String sql = "SELECT COUNT(*) AS voteCount FROM onboardify.evaluation WHERE idResource = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, resourceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("voteCount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // READ ALL RESOURCES
    @Override
    public List<Ressource> getAll() throws Exception {
        String sql = "SELECT * FROM onboardify.ressources";
        List<Ressource> list = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ressource res = new Ressource();
                res.setIdResource(rs.getInt("idResource"));
                res.setTitre(rs.getString("titre"));
                res.setType(rs.getString("type"));
                res.setDescription(rs.getString("description"));
                res.setlien(rs.getString("lien"));
                list.add(res);
            }
        }
        return list;
    }

    // TOP 5 HIGH AVERAGE NOTE RESOURCES
    public List<Ressource> getResourcesWithHighAverageNote() throws Exception {
        String sql = "SELECT r.idResource, r.titre, COALESCE(AVG(e.note), 0) AS avgNote " +
                "FROM onboardify.ressources r " +
                "LEFT JOIN onboardify.evaluation e ON r.idResource = e.idResource " +
                "GROUP BY r.idResource " +
                "ORDER BY avgNote DESC " +
                "LIMIT 5";

        List<Ressource> resourcesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ressource ressource = new Ressource(
                        rs.getInt("idResource"),
                        0,
                        rs.getString("titre"),
                        "",
                        "",
                        "",
                        rs.getDouble("avgNote")
                );
                resourcesList.add(ressource);
            }
        }
        return resourcesList;
    }

    // TOP 5 LOW AVERAGE NOTE RESOURCES
    public List<Ressource> getResourcesWithLowAverageNote() throws Exception {
        String sql = "SELECT r.idResource, r.titre, COALESCE(AVG(e.note), 0) AS avgNote " +
                "FROM onboardify.ressources r " +
                "LEFT JOIN onboardify.evaluation e ON r.idResource = e.idResource " +
                "GROUP BY r.idResource " +
                "HAVING avgNote > 0 " +
                "ORDER BY avgNote ASC " +
                "LIMIT 5";

        List<Ressource> resourcesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ressource ressource = new Ressource(
                        rs.getInt("idResource"),
                        0,
                        rs.getString("titre"),
                        "",
                        "",
                        "",
                        rs.getDouble("avgNote")
                );
                resourcesList.add(ressource);
            }
        }
        return resourcesList;
    }

    // RESOURCES WITH AVERAGE NOTE
    public List<Ressource> getResourcesWithAverageNote() throws Exception {
        String sql = "SELECT r.idResource, r.titre, COALESCE(AVG(e.note), 0) AS avgNote " +
                "FROM onboardify.ressources r " +
                "LEFT JOIN onboardify.evaluation e ON r.idResource = e.idResource " +
                "GROUP BY r.idResource";

        List<Ressource> resourcesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Ressource ressource = new Ressource(
                        rs.getInt("idResource"),
                        0,
                        rs.getString("titre"),
                        "",
                        "",
                        "",
                        rs.getDouble("avgNote")
                );
                resourcesList.add(ressource);
            }
        }
        return resourcesList;
    }

    // TYPE DISTRIBUTION
    public Map<String, Integer> getResourceTypeDistribution() throws Exception {
        String sql = "SELECT type, COUNT(*) as count FROM onboardify.ressources GROUP BY type";
        Map<String, Integer> typeDistribution = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String type = rs.getString("type");
                int count = rs.getInt("count");
                typeDistribution.put(type, count);
            }
        }
        return typeDistribution;
    }

    // AVERAGE SCORE FOR TYPE
    public double getAverageScoreForType(String type) throws Exception {
        String sql = "SELECT COALESCE(AVG(e.note), 0) as avgScore " +
                "FROM onboardify.ressources r " +
                "LEFT JOIN onboardify.evaluation e ON r.idResource = e.idResource " +
                "WHERE r.type = ? " +
                "GROUP BY r.type";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avgScore");
            }
        }
        return 0.0;
    }
}