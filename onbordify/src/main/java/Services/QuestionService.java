package Services;

import modles.Question;
import utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements CrudInterface<Question> {

    private final Connection connection;

    public QuestionService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(Question obj) throws Exception {
        String checkIfExistsSQL = "SELECT idQuestion FROM Question WHERE question = ? AND idQuiz = ?";
        String insertSQL = "INSERT INTO Question (question, idQuiz) VALUES (?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkIfExistsSQL);
                PreparedStatement insertStmt = connection.prepareStatement(insertSQL)
        ) {
            checkStmt.setString(1, obj.getQuestion());
            checkStmt.setInt(2, obj.getIdQuiz());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("A question with this name already exists for the given quiz.");
                }
            }

            insertStmt.setString(1, obj.getQuestion());
            insertStmt.setInt(2, obj.getIdQuiz());
            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Question created successfully!");
            } else {
                throw new SQLException("Question insertion failed.");
            }
        }
    }

    @Override
    public void update(Question obj) throws Exception {
        String checkIfExistsSQL = "SELECT idQuestion FROM Question WHERE question = ? AND idQuiz = ? AND idQuestion != ?";
        String updateSQL = "UPDATE Question SET question = ? WHERE idQuestion = ?";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkIfExistsSQL);
                PreparedStatement updateStmt = connection.prepareStatement(updateSQL)
        ) {
            checkStmt.setString(1, obj.getQuestion());
            checkStmt.setInt(2, obj.getIdQuiz());
            checkStmt.setInt(3, obj.getIdQuestion());

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("A question with this name already exists in the same quiz.");
                }
            }

            updateStmt.setString(1, obj.getQuestion());
            updateStmt.setInt(2, obj.getIdQuestion());

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Question updated successfully!");
            } else {
                throw new SQLException("Question update failed.");
            }
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Question WHERE idQuestion = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Question deleted successfully!");
            } else {
                throw new SQLException("No question found with the given ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting question: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Question> getAll() throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM Question";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                questionList.add(new Question(
                        rs.getInt("idQuestion"),
                        rs.getString("question"),
                        rs.getInt("idQuiz")
                ));
            }
        }
        return questionList;
    }

    public List<Question> getQuestionsByQuizId(int idQuiz) throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM Question WHERE idQuiz = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idQuiz);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questionList.add(new Question(
                            rs.getInt("idQuestion"),
                            rs.getString("question"),
                            rs.getInt("idQuiz")
                    ));
                }
            }
        }
        return questionList;
    }

    public Question getById(int id) throws SQLException {
        String sql = "SELECT * FROM Question WHERE idQuestion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("idQuestion"),
                            rs.getString("question"),
                            rs.getInt("idQuiz")
                    );
                }
            }
        }
        return null;
    }
}
