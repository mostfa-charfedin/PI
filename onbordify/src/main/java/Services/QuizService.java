package Services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modles.Quiz;
import utils.MyDb;

public  class QuizService implements CrudInterface<Quiz> {


    private final Connection connection;

    public QuizService() {
        this.connection = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(Quiz quiz) throws SQLException {
        String checkIfQuizExistsSQL = "SELECT idQuiz FROM Quiz WHERE nom = ?";
        String insertSQL = "INSERT INTO Quiz (nom, dateCreation) VALUES (?, ?)";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(checkIfQuizExistsSQL);
                PreparedStatement insertStmt = connection.prepareStatement(insertSQL)
        ) {
            // Step 1: Check if the quiz already exists
            checkStmt.setString(1, quiz.getNom());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("Error: A quiz with this name already exists.");
                }
            }

            // Step 2: Insert the new quiz
            insertStmt.setString(1, quiz.getNom());
            insertStmt.setString(2, quiz.getDateCreation());

            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted == 0) {
                throw new SQLException("Quiz insertion failed. No rows affected.");
            }

            System.out.println("Quiz created successfully!");
        }
    }

    @Override
    public void update(Quiz quiz) throws SQLException {
        String sql = "UPDATE Quiz SET nom = ?, dateCreation = ? WHERE idQuiz = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, quiz.getNom());
            pstmt.setString(2, quiz.getDateCreation());
            pstmt.setInt(3, quiz.getIdQuiz());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Quiz update failed. No rows affected.");
            }

            System.out.println("Quiz updated successfully!");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Quiz WHERE idQuiz = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("Quiz deletion failed. No quiz found with the given ID.");
            }

            System.out.println("Quiz deleted successfully!");
        }
    }

    public void deleteByName(String nom) throws SQLException {
        String sql = "DELETE FROM Quiz WHERE nom = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No quiz found with the given name.");
            }

            System.out.println("Quiz deleted successfully!");
        }
    }

    @Override
    public List<Quiz> getAll() throws SQLException {
        List<Quiz> quizList = new ArrayList<>();
        String query = "SELECT * FROM Quiz";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                quizList.add(new Quiz(
                        rs.getInt("idQuiz"),
                        rs.getString("nom"),
                        rs.getString("dateCreation")
                ));
            }
        }

        return quizList;
    }

    public Quiz getById(int id) throws SQLException {
        String sql = "SELECT * FROM Quiz WHERE idQuiz = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Quiz(
                            rs.getInt("idQuiz"),
                            rs.getString("nom"),
                            rs.getString("dateCreation")
                    );
                }
            }
        }

        return null; // If no quiz is found with the given ID
    }
}



