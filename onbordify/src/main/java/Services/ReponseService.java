package Services;

import Models.Reponse;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements CrudInterface<Reponse> {

    private final Connection connection;

    public ReponseService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    // CREATE
    public void create(Reponse reponse) throws SQLException {
        String sql = "INSERT INTO reponse (Response, status, idQuestion) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reponse.getReponse());
            stmt.setString(2, reponse.getStatut());
            stmt.setInt(3, reponse.getIdQuestion());

            stmt.executeUpdate();
            System.out.println("Réponse insérée avec succès.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la réponse: " + e.getMessage());
            throw new SQLException("Échec de l'insertion de la réponse dans la base de données", e);
        }
    }

    // UPDATE
    @Override
    public void update(Reponse obj) throws SQLException {
        if (obj == null) {
            throw new SQLException("L'objet Reponse ne peut pas être null.");
        }

        String query = "UPDATE reponse SET reponse = ?, statut = ?, idQuestion = ? WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, obj.getReponse());
            stmt.setString(2, obj.getStatut());
            stmt.setInt(3, obj.getIdQuestion());
            stmt.setInt(4, obj.getIdReponse());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Aucune mise à jour effectuée. ID de réponse non trouvé: " + obj.getIdReponse());
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la mise à jour de la réponse", e);
        }
    }

    // DELETE
    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reponse WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Aucune suppression effectuée. ID de réponse non trouvé: " + id);
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la suppression de la réponse", e);
        }
    }

    // GET ALL RESPONSES
    @Override
    public List<Reponse> getAll() throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        String query = "SELECT idReponse, reponse, statut, idQuestion FROM reponse";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reponseList.add(new Reponse(
                        rs.getInt("idReponse"),
                        rs.getString("reponse"),
                        rs.getString("statut"),
                        rs.getInt("idQuestion")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération de toutes les réponses", e);
        }
        return reponseList;
    }

    // GET RESPONSES BY QUESTION ID
    public List<Reponse> getReponsesByQuestionId(int questionId) throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        String query = "SELECT idReponse, Response, status, idQuestion FROM reponse WHERE idQuestion = ?";


        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reponseList.add(new Reponse(
                            rs.getInt("idReponse"),
                            rs.getString("Response"),
                            rs.getString("status"),
                            rs.getInt("idQuestion")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Erreur lors de la récupération des réponses pour la question ID " + questionId, e);
        }
        return reponseList;
    }
}
