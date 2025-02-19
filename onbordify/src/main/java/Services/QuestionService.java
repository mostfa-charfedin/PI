package Services;

import modles.Question;
import utils.MyDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements CrudInterface<Question> {

     Connection connection;

    public QuestionService() {
        connection = MyDb.getInstance().getConnection();
    }


    @Override
    public void create(Question obj) throws Exception {
        //String sql = "insert into Question(question,idQuiz) values('" + obj.getQuestion() + "','" + obj.getIdQuiz() + "')";
       // Statement stmt = connection.createStatement();
       // stmt.executeUpdate(sql);
        String query = "INSERT INTO Question (question, idQuiz) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, obj.getQuestion());
            stmt.setInt(2, obj.getIdQuiz());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                obj.setIdQuestion(generatedKeys.getInt(1));
            }
        }


    }

    @Override
    public void update(Question obj) throws Exception {
        String query = "UPDATE Question SET question = ?, idQuiz = ? WHERE idQuestion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, obj.getQuestion());
            stmt.setInt(2, obj.getIdQuiz());
            stmt.setInt(3, obj.getIdQuestion());
            stmt.executeUpdate();
        }

    }

    @Override
    public void delete(int id) throws Exception {
        String query = "DELETE FROM Question WHERE idQuestion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

    }

    @Override
    public List<Question> getAll() throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM Question";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                questionList.add(new Question(rs.getInt("idQuestion"), rs.getString("question"), rs.getInt("idQuiz")));
            }
        }
        return questionList;}}

