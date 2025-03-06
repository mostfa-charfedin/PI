package Services;

import Models.Ressource;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RessourceService implements CrudInterface<Ressource> {
    private Connection connection;

    public RessourceService() {
        connection = MyDb.getMydb().getConnection();
    }

    // CREATE (Utilisation de PreparedStatement pour éviter l'injection SQL)
    @Override

    public void create(Ressource res) throws Exception {
        String sql = "INSERT INTO onboardify.ressources (titre, type, description, lien, noteAverage) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, res.getTitre());
            stmt.setString(2, res.getType());
            stmt.setString(3, res.getDescription());
            stmt.setString(4, res.getlien());
            stmt.setDouble(5, res.getNoteAverage()); // Ajout de la note moyenne
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
            stmt.setDouble(5, res.getNoteAverage()); // Ajout de la note moyenne
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
    // Méthode pour récupérer la note d'une ressource depuis la table 'evaluation'
    public double getNoteFromEvaluation(int idResource) {
        String query = "SELECT note FROM evaluation WHERE idResource = ?"; // Interroger la table 'evaluation' pour la note
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idResource);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("note");  // Récupérer la note
            } else {
                return 0.0;  // Si aucune note n'est trouvée
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;  // Retourner 0 en cas d'erreur
        }
    }


    // READ
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

    // GET RESOURCES WITH HIGH AVERAGE NOTE
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


    // GET RESOURCES WITH LOW AVERAGE NOTE
    public List<Ressource> getResourcesWithLowAverageNote() throws Exception {
        String sql = "SELECT r.idResource, r.titre, COALESCE(AVG(e.note), 0) AS avgNote " +
                "FROM onboardify.ressources r " +
                "LEFT JOIN onboardify.evaluation e ON r.idResource = e.idResource " +
                "GROUP BY r.idResource " +
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
    // NEW: GET RESOURCES WITH AVERAGE NOTE (without filtering by high/low)
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
}
