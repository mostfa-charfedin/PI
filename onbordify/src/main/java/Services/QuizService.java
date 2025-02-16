package Services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modles.Quiz;
import utils.MyDb;


public  class QuizService implements CrudInterface<Quiz> {
    Connection connection;

    public QuizService() {
        connection = MyDb.getInstance().getConnection();
    }


    @Override
    public void create(Quiz obj) throws Exception {
        String sql = "insert into Quiz(nom,dateCreation) values('" + obj.getNom() + "','" + obj.getDateCreation() + "')";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);

    }

    @Override
    public void update(Quiz obj) throws Exception {
        String sql = "update Quiz set nom = ?, dateCreation = ?, where id = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, obj.getNom());
        pstmt.setString(2, obj.getDateCreation());
        pstmt.setInt(3, obj.getIdQuiz());
        pstmt.executeUpdate();
    }


    @Override
    public void delete(int id) throws Exception {
        String query = "DELETE FROM Quiz WHERE idQuiz = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

    @Override
    public  List<Quiz> getAll() throws Exception {
        List<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM Quiz";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                quizList.add(new Quiz(rs.getInt("idQuiz"), rs.getString("nom"), rs.getString("dateCreation")));
            }
        }
        return quizList;
    }

}

