package Services;

import Models.Score;

import utils.MyDb;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreService implements CrudInterface<Score> {
    Connection con;
    EmailService emailService = new EmailService();
    String nonHashedPassword;

    public ScoreService() {
        this.con = MyDb.getMydb().getConnection();
    }


    @Override
    public void create(Score obj) throws Exception {
        String query = "INSERT INTO Score (idQuiz, idUser, score) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, obj.getIdQuiz());
            stmt.setInt(2, obj.getIdUser());
            stmt.setDouble(3, obj.getScore());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(Score obj) throws Exception {
        String query = "UPDATE Score SET score = ? WHERE idQuiz = ? AND idUser = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDouble(1, obj.getScore());
            stmt.setInt(2, obj.getIdQuiz());
            stmt.setInt(3, obj.getIdUser());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int idUser) throws Exception {
        String query = "DELETE FROM Score WHERE idUser = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete(int idQuiz, int idUser) throws Exception {
        String query = "DELETE FROM Score WHERE idQuiz = ? AND idUser = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idQuiz);
            stmt.setInt(2, idUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Float getScore(int idQuiz, int idUser) {
        String query = "SELECT score FROM Score WHERE idQuiz = ? AND idUser = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, idQuiz);
            stmt.setInt(2, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Score> getAll() throws SQLException {
        String sql = "select * from score";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Score> scors = new ArrayList<>();
        while (rs.next()) {
            Score score = new Score();
            score.setIdUser(rs.getInt("idUser"));
            score.setIdQuiz(rs.getInt("idQuiz"));
            score.setScore(rs.getDouble("score"));
            scors.add(score);
        }
        return scors;
    }



}
