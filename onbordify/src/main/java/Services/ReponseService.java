package Services;

import modles.Reponse; // Fixed import statement
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
        // SQL query to insert the response into the database
        String sql = "INSERT INTO reponse (Response, status) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the parameters for the PreparedStatement
            stmt.setString(1, reponse.getReponse()); // Set the answer text
            stmt.setString(2, reponse.getStatut());  // Set the status (correct or incorrect)
            //stmt.setInt(3, reponse.getIdQuestion()); // Set the associated question ID

            // Execute the update (insert the response)
            stmt.executeUpdate();
        } catch (SQLException e) {
            // Log the error or handle it according to your application needs
            System.err.println("Error inserting response: " + e.getMessage());
            // Optionally, rethrow the exception or handle more gracefully
            throw new SQLException("Failed to insert response into the database", e);
        }

    }


    @Override
    public void update(Reponse obj) throws SQLException {
        if (obj == null) {
            throw new SQLException("Reponse object cannot be null.");
        }

        String query = "UPDATE reponse SET reponse = ?, statut = ?, idQuestion = ? WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, obj.getReponse());
            stmt.setString(2, obj.getStatut());
            stmt.setInt(3, obj.getIdQuestion());
            stmt.setInt(4, obj.getIdReponse());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No rows were updated. Reponse with ID " + obj.getIdReponse() + " may not exist.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error while updating answer", e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reponse WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No rows were deleted. Reponse with ID " + id + " may not exist.");
            }
        } catch (SQLException e) {
            throw new SQLException("Error while deleting answer", e);
        }
    }

    @Override
    public List<Reponse> getAll() throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        // SQL query to join reponse and question tables
        String query = "SELECT r.idReponse , r.Response, r.status, r.idQuestion " +
                "FROM reponse r " +
                "JOIN question q ON r.idQuestion = q.idQuestion";

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
            throw new SQLException("Error while retrieving all answers with question details", e);
        }
        return reponseList;
    }

    public List<Reponse> getReponsesByQuestionId(int questionId) throws SQLException {
        List<Reponse> reponseList = new ArrayList<>();
        // SQL query to join reponse and question tables based on idQuestion
        String query = "SELECT r.idReponse, r.Response, r.status, r.idQuestion " +
                "FROM reponse r " +
                "JOIN question q ON r.idQuestion = q.idQuestion " +
                "WHERE r.idQuestion = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, questionId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reponseList.add(new Reponse(
                            rs.getInt("idReponse"),
                            rs.getString("reponse"),
                            rs.getString("statut"),
                            rs.getInt("idQuestion")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error while retrieving answers for question ID " + questionId, e);
        }
        return reponseList;
    }
}