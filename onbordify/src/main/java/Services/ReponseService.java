package Services;

import modles.Reponse;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements CrudInterface<Reponse> {

    private final Connection connection;

    public ReponseService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(Reponse obj) throws Exception {
        String query = "INSERT INTO Reponse (reponse, statut, idQuestion) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, obj.getReponse());
            stmt.setString(2, obj.getStatut());
            stmt.setInt(3, obj.getIdQuestion());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setIdReponse(generatedKeys.getInt(1));
            }
        }
    }

    @Override
    public void update(Reponse obj) throws Exception {
        String query = "UPDATE Reponse SET reponse = ?, statut = ?, idQuestion = ? WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, obj.getReponse());
            stmt.setString(2, obj.getStatut());
            stmt.setInt(3, obj.getIdQuestion());
            stmt.setInt(4, obj.getIdReponse());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String query = "DELETE FROM Reponse WHERE idReponse = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Reponse> getAll() throws Exception {
        List<Reponse> reponseList = new ArrayList<>();
        String query = "SELECT * FROM Reponse";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Reponse reponse = new Reponse(
                        rs.getInt("idReponse"),
                        rs.getString("reponse"),
                        rs.getString("statut"),
                        rs.getInt("idQuestion")
                );
                reponseList.add(reponse);
            }
        }
        return reponseList;
    }
}