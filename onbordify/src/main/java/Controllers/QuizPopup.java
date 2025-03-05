package Controllers;

import Models.Quiz;
import Models.Question;
import Models.Reponse;
import Services.QuestionService;
import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.MyDb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizPopup {
    private Connection connection;
    private int score = 0;
    private int currentQuestionIndex = 0;

    @FXML
    private TextField answer1;
    @FXML
    private TextField answer2;
    @FXML
    private TextField answer3;
    @FXML
    private TextField answer4;
    @FXML
    private Label result;
    @FXML
    private AnchorPane mypane;
    @FXML
    private ImageView next;
    @FXML
    private Label question_field;
    @FXML
    private Label Quiz_name;
    @FXML
    private CheckBox toggle1;
    @FXML
    private CheckBox toggle2;
    @FXML
    private CheckBox toggle3;
    @FXML
    private CheckBox toggle4;

    private Quiz quiz;
    private List<Question> questions;
    private ReponseService reponse;

    public QuizPopup() {
        this.connection = MyDb.getMydb().getConnection();
        this.reponse = new ReponseService();
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        Quiz_name.setText("Quiz: " + quiz.getNom());
        this.questions = fetchQuestionsByQuizId(quiz.getIdQuiz());

        if (questions != null && !questions.isEmpty()) {
            displayQuestion(currentQuestionIndex);
        } else {
            System.out.println("No questions found for this quiz.");
        }
    }

    private List<Question> fetchQuestionsByQuizId(int quizId) {
        List<Question> questionList = new ArrayList<>();
        QuestionService questionService = new QuestionService();

        try {
            questionList = questionService.getQuestionsByQuizId(quizId);
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }
        return questionList;
    }

    @FXML
    private void submit(ActionEvent event) {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            int questionId = currentQuestion.getIdQuestion();
            boolean isCorrect = false;

            try {
                List<Reponse> answers = reponse.getReponsesByQuestionId(questionId);
                CheckBox[] checkBoxes = {toggle1, toggle2, toggle3, toggle4};

                for (int i = 0; i < answers.size(); i++) {
                    if (checkBoxes[i].isSelected()) {
                        isCorrect = "correct".equalsIgnoreCase(answers.get(i).getStatut());
                        break;
                    }
                }

                if (isCorrect) {
                    score++;
                    result.setText("✅ Correct! Score: " + score);
                } else {
                    result.setText("❌ Wrong! Score: " + score);
                }

                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex);
                } else {
                    showFinalScore();
                }

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void displayQuestion(int index) {
        if (index < questions.size()) {
            Question question = questions.get(index);
            question_field.setText(question.getQuestion()); // Set question text in TextField

            try {
                List<Reponse> answers = reponse.getReponsesByQuestionId(question.getIdQuestion());

                // Store answers in text fields instead of checkboxes
                TextField[] answerFields = {answer1, answer2, answer3, answer4};
                CheckBox[] checkBoxes = {toggle1, toggle2, toggle3, toggle4};

                for (int i = 0; i < answerFields.length; i++) {
                    if (i < answers.size()) {
                        answerFields[i].setText(answers.get(i).getReponse());
                        answerFields[i].setVisible(true);
                        checkBoxes[i].setVisible(true);
                        checkBoxes[i].setSelected(false);
                    } else {
                        answerFields[i].setText("");
                        answerFields[i].setVisible(false);
                        checkBoxes[i].setVisible(false);
                    }
                }
            } catch (SQLException e) {
                System.err.println("Error loading answers: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showFinalScore() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Completed");
        alert.setHeaderText("Congratulations!");
        alert.setContentText("Your final score is: " + score);
        alert.showAndWait();
    }
}
