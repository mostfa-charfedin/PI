package Services;

import Models.Question;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements CrudInterface<Question> {

    Connection connection;
    public QuestionService() {
        this.connection = MyDb.getInstance().getConnection();
    }
    @Override
    public void create(Question obj) throws Exception {
        String getQuizIdSQL = "SELECT idQuiz FROM Quiz WHERE nom = ?";
        String insertSQL = "INSERT INTO Question (question, idQuiz) VALUES (?, ?)";

        try (
                PreparedStatement getQuizIdStmt = connection.prepareStatement(getQuizIdSQL);
                PreparedStatement insertStmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            // Get the quiz ID based on the quiz name
            getQuizIdStmt.setString(1, obj.getnom());

            try (ResultSet rs = getQuizIdStmt.executeQuery()) {
                if (rs.next()) {
                    int idQuiz = rs.getInt("idQuiz");

                    // Insert new question
                    insertStmt.setString(1, obj.getQuestion());
                    insertStmt.setInt(2, idQuiz);
                    int rowsInserted = insertStmt.executeUpdate();

                    if (rowsInserted > 0) {
                        try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                obj.setIdQuestion(generatedKeys.getInt(1)); // Set the generated ID
                            }
                        }
                        System.out.println("Question created successfully!");
                    } else {
                        throw new SQLException("Question insertion failed.");
                    }
                } else {
                    throw new SQLException("Quiz with the specified name does not exist.");
                }
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
        String checkSQL = "SELECT idQuestion FROM Question WHERE idQuestion = ?";
        String deleteSQL = "DELETE FROM Question WHERE idQuestion = ?";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
                PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL)
        ) {
            // Check if the question exists before attempting deletion
            checkStmt.setInt(1, id);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    throw new SQLException("No question found with the given ID.");
                }
            }

            // Proceed with deletion
            deleteStmt.setInt(1, id);
            int rowsDeleted = deleteStmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Question deleted successfully!");
            } else {
                throw new SQLException("Failed to delete question.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting question: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Question> getAll() throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT q.idQuestion, q.question, q.idQuiz, quiz.nom FROM Question q JOIN Quiz quiz ON q.idQuiz = quiz.idQuiz";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                questionList.add(new Question(
                        rs.getInt("idQuestion"),
                        rs.getString("question"),
                        rs.getInt("idQuiz"),
                        rs.getString("nom")  // Quiz name
                ));
            }
        }
        return questionList;
    }

    public List<Question> getQuestionsByQuizId(int idQuiz) throws Exception {
        List<Question> questionList = new ArrayList<>();
        String query = "SELECT q.idQuestion, q.question, q.idQuiz, quiz.nom FROM Question q JOIN Quiz quiz ON q.idQuiz = quiz.idQuiz WHERE q.idQuiz = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idQuiz);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questionList.add(new Question(
                            rs.getInt("idQuestion"),
                            rs.getString("question"),
                            rs.getInt("idQuiz"),
                            rs.getString("nom")  // Quiz name
                    ));
                }
            }
        }
        return questionList;
    }

    public Question getById(int id) throws SQLException {
        String sql = "SELECT q.idQuestion, q.question, q.idQuiz, quiz.nom FROM Question q JOIN Quiz quiz ON q.idQuiz = quiz.idQuiz WHERE q.idQuestion = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Question(
                            rs.getInt("idQuestion"),
                            rs.getString("question"),
                            rs.getInt("idQuiz"),
                            rs.getString("nom")  // Quiz name
                    );
                }
            }
        }
        return null;
    }

    public Question getQuestionByText(String selectedQuestionText, int selectedQuizId) throws SQLException {
        String sql = "SELECT * FROM Question WHERE question = ? AND idQuiz = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, selectedQuestionText);
            stmt.setInt(2, selectedQuizId);

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
        return null; // Return null if no matching question is found
    }
    public int getQuizIdByName(String quizName) throws SQLException {
        String sql = "SELECT idQuiz FROM Quiz WHERE nom = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, quizName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idQuiz");
                }
            }
        }
        return -1; // Return -1 if quiz not found
    }


    public void deleteQuestionByText(String selectedQuestion, int selectedQuizId) {
        String query = "DELETE FROM question WHERE Question = ? AND idQuiz = ?";

        try (
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, selectedQuestion);
            pstmt.setInt(2, selectedQuizId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Question deleted successfully.");
            } else {
                System.out.println("No question found to delete.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error deleting question: " + e.getMessage());
        }
    }


    public int getQuestionIdByText(String selectedQuestion, int selectedQuizId) {
        int questionId = -1; // Default value if not found

        String query = "SELECT idQuestion FROM question WHERE question = ? AND idQuiz = ?";

        try (
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, selectedQuestion);
            statement.setInt(2, selectedQuizId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                questionId = resultSet.getInt("idQuestion");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionId; // Returns -1 if no matching question is found
    }
}
