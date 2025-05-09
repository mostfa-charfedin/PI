package Controllers;

import Models.*;
import Services.QuestionService;
import Services.ReponseService;
import Services.ScoreService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.MyDb;
import utils.UserSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizPopup {
    private Connection connection;
    private int score = 0;
    private int currentQuestionIndex = 0;
    UserSession session = UserSession.getInstance();
    int userId = session.getUserId();

    @FXML
    private Button submit;
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
    private ScoreService scoreService;  // Declare ScoreService

    public QuizPopup() {
        this.connection = MyDb.getMydb().getConnection();
        this.reponse = new ReponseService();
        // Initialize ScoreService here
        this.scoreService = new ScoreService();
        if (this.scoreService == null) {
            System.out.println("Error: ScoreService is not initialized properly.");
        } else {
            System.out.println("ScoreService initialized successfully.");
        }
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
                // Retrieve the answers for the current question
                List<Reponse> answers = reponse.getReponsesByQuestionId(questionId);
                CheckBox[] checkBoxes = {toggle1, toggle2, toggle3, toggle4};

                // Check if any of the checkboxes are selected and if it's the correct answer
                for (int i = 0; i < answers.size(); i++) {
                    if (checkBoxes[i].isSelected()) {
                        isCorrect = "correct".equalsIgnoreCase(answers.get(i).getStatut());
                        break;
                    }
                }

                // Update the score if the answer is correct
                if (isCorrect) {
                    score++;
                }

                // Temporarily disable the submit button and change its text to "Next Question"
                submit.setDisable(true);
                submit.setText("Next Question");

                // Wait a short period before proceeding to the next question or showing the final score
                new Thread(() -> {
                    try {
                        Thread.sleep(500); // Delay of 500ms to allow user to see the transition
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Proceed to the next question or show the final score
                    javafx.application.Platform.runLater(() -> {
                        submit.setDisable(false);
                        if (currentQuestionIndex < questions.size() - 1) {
                            // Move to the next question
                            currentQuestionIndex++;
                            displayQuestion(currentQuestionIndex); // Update the displayed question
                        } else {
                            // All questions answered, show final score
                            showFinalScore();
                            saveScoreToDatabase(); // Save the score to the database
                        }

                        // Reset button text to "Submit" for the next question
                        submit.setText("Submit");
                    });
                }).start();

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Method to save the score in the database using ScoreService
    private void saveScoreToDatabase() {
        try {
            // Check if scoreService is initialized correctly
            if (scoreService == null) {
                System.out.println("Error: ScoreService is not initialized.");
                return;  // Don't proceed if scoreService is not initialized
            }


            int quizId = quiz.getIdQuiz(); // Get the current quiz's ID

            // Create a Score object to be saved
            Score scoreObj = new Score();
            scoreObj.setIdUser(userId);
            scoreObj.setIdQuiz(quizId);
            scoreObj.setScore(score);

            // Use the ScoreService to save the score
            scoreService.create(scoreObj); // Call the create method from ScoreService

        } catch (Exception e) {
            System.err.println("Error saving score to database: " + e.getMessage());
            e.printStackTrace();
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
        try {
            // Load the Result interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/result.fxml"));
            Parent root = loader.load();

            // Get the controller of result.fxml
            result resultController = loader.getController();
            resultController.setScore(score); // Pass the score to the ResultController

            // Create a new Stage for the popup
            Stage stage = new Stage();
            stage.setTitle("Quiz Result");
            stage.setScene(new Scene(root, 700, 400));
            stage.setResizable(false); // Prevent resizing
            stage.initStyle(javafx.stage.StageStyle.UTILITY); // Optional: Removes minimize/maximize buttons
            stage.centerOnScreen(); // Center it on the screen

            // Show the popup
            stage.show();

            // Close the current quiz popup window
            Stage currentStage = (Stage) submit.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            System.err.println("Error loading result.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }


}