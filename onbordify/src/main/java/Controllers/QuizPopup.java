package Controllers;

import Models.Quiz;
import Models.Question;
import Models.Reponse;
import Services.QuestionService;
import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.MyDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizPopup {
    private Connection connection;
    @FXML
    private TextField answer1;
    @FXML
    private TextField answer2;
    @FXML
    private TextField answer3;
    @FXML
    private TextField answer4;

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
    private int currentQuestionIndex = 0;
    private List<Question> questions;
    private ReponseService reponse;

    public QuizPopup() {
        this.connection = MyDb.getMydb().getConnection();
        this.reponse = new ReponseService(); // Initialize the ReponseService
    }

    // Set quiz and fetch questions
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        System.out.println("Quiz ID: " + quiz.getIdQuiz()); // Vérifie l'ID du quiz

        // Set the quiz name in the Quiz_name label
        Quiz_name.setText("Quiz: " + quiz.getNom()); // Assuming quiz.getNom() returns the quiz name

        this.questions = fetchQuestionsByQuizId(quiz.getIdQuiz());

        if (questions != null && !questions.isEmpty()) {
            System.out.println("Number of questions fetched: " + questions.size());
            displayQuestion(currentQuestionIndex); // Display the first question
        } else {
            System.out.println("No questions found for this quiz.");
        }
    }

    private List<Question> fetchQuestionsByQuizId(int quizId) {
        List<Question> questionList = new ArrayList<>();
        QuestionService questionService = new QuestionService(); // Use the QuestionService

        try {
            // Fetch questions for the given quiz ID using the QuestionService
            questionList = questionService.getQuestionsByQuizId(quizId);
            System.out.println("Number of questions fetched: " + questionList.size());
            for (Question question : questionList) {
                System.out.println("Found question: " + question.getQuestion());
            }
        } catch (Exception e) {
            System.out.println("Error fetching questions: " + e.getMessage());
        }

        return questionList;
    }

    private List<Reponse> fetchResponsesByQuestionId(int questionId) {
        List<Reponse> responseList = new ArrayList<>();
        ReponseService reponseService = new ReponseService(); // Use the ReponseService

        try {
            // Fetch responses for the given question ID using the ReponseService
            responseList = reponseService.getReponsesByQuestionId(questionId);
        } catch (SQLException e) {
            System.out.println("Error fetching responses: " + e.getMessage());
        }

        return responseList;
    }

    // Display question and responses based on the question ID
    private void displayQuestion(int index) {
        if (index >= 0 && index < questions.size()) {
            Question question = questions.get(index);

            // Set the question text
            question_field.setText(question.getQuestion());

            try {
                // Fetch the responses for the current question from the database
                List<Reponse> answers = fetchResponsesByQuestionId(question.getIdQuestion());

                // Set the answers in the TextFields
                answer1.setText(answers.size() > 0 ? answers.get(0).getReponse() : "");
                answer2.setText(answers.size() > 1 ? answers.get(1).getReponse() : "");
                answer3.setText(answers.size() > 2 ? answers.get(2).getReponse() : "");
                answer4.setText(answers.size() > 3 ? answers.get(3).getReponse() : "");

                // Reset toggle selections based on the answer status
                toggle1.setSelected(answers.size() > 0 && answers.get(0).getStatut().equals("True"));
                toggle2.setSelected(answers.size() > 1 && answers.get(1).getStatut().equals("True"));
                toggle3.setSelected(answers.size() > 2 && answers.get(2).getStatut().equals("True"));
                toggle4.setSelected(answers.size() > 3 && answers.get(3).getStatut().equals("True"));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading answers for the question.");
            }
        } else {
            System.out.println("No more questions to display.");
        }
    }

    private void closePopup() {
        Stage stage = (Stage) mypane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void submit(ActionEvent event) {
        if (currentQuestionIndex < questions.size()) {
            Question currentQuestion = questions.get(currentQuestionIndex);
            int questionId = currentQuestion.getIdQuestion();

            int selectedAnswerId = -1;

            try {
                List<Reponse> answers = reponse.getReponsesByQuestionId(questionId);

                // Identify the selected response
                if (toggle1.isSelected() && answers.size() > 0) {
                    selectedAnswerId = answers.get(0).getIdReponse();
                } else if (toggle2.isSelected() && answers.size() > 1) {
                    selectedAnswerId = answers.get(1).getIdReponse();
                } else if (toggle3.isSelected() && answers.size() > 2) {
                    selectedAnswerId = answers.get(2).getIdReponse();
                } else if (toggle4.isSelected() && answers.size() > 3) {
                    selectedAnswerId = answers.get(3).getIdReponse();
                }

                // Save response to the database if an answer is selected
                if (selectedAnswerId != -1) {
                    reponse.saveUserResponse(selectedAnswerId, questionId);
                    System.out.println("Response saved successfully!");
                } else {
                    System.out.println("No answer selected!");
                    return;
                }

                // Move to the next question
                if (currentQuestionIndex < questions.size() - 1) {
                    currentQuestionIndex++;
                    displayQuestion(currentQuestionIndex); // Display the next question
                } else {
                    System.out.println("Quiz completed!");
                    closePopup();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}