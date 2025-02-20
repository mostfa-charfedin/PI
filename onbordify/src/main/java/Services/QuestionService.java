package Services;

import modles.Question;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements CrudInterface<Question> {

     Connection connection;

    public QuestionService() {
        connection = MyDb.getInstance().getConnection();
    }


    @Override
    public void create(Question obj) throws Exception {
        String checkIfQuestionExistsSQL = "SELECT idQuestion FROM Question WHERE question = ? AND idQuiz = ?";
        String insertSQL = "INSERT INTO Question (question, idQuiz) VALUES (?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkIfQuestionExistsSQL);
                PreparedStatement insertStmt = connection.prepareStatement(insertSQL)
        ) {
            // Step 1: Check if the question already exists for the specific quiz
            checkStmt.setString(1, obj.getQuestion());
            checkStmt.setInt(2, obj.getIdQuiz());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("Error: A question with this name already exists for the given quiz.");
                }
            }

            // Step 2: Insert the new question
            insertStmt.setString(1, obj.getQuestion());
            insertStmt.setInt(2, obj.getIdQuiz());

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Question insertion failed. No rows affected.");
            }

            System.out.println("Question created successfully!");
        }
    }

    @Override
    public void update(Question obj) throws Exception {
        String sql = "UPDATE Question SET question = ?, idQuiz = ? WHERE idQuestion = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, obj.getQuestion());
            pstmt.setInt(2, obj.getIdQuiz());
            pstmt.setInt(3, obj.getIdQuestion());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Question update failed. No rows affected.");
            }

            System.out.println("Question updated successfully!");
        }

    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM Question WHERE idQuestion = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Question deletion failed. No question found with the given ID.");
            }

            System.out.println("Question deleted successfully!");
        }

    }

    @Override
    public List<Question> getAll() throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT * FROM Question";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                questionList.add(new Question(
                        rs.getInt("idQuestion"),
                        rs.getString("question"),
                        rs.getInt("idQuiz")
                ));
            }
        }        return questionList;
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

        return null; // If no question is found with the given ID
    }
}



