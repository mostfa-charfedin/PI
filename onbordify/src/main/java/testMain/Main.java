package testMain;

import modles.Quiz;
import Services.QuizService;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        QuizService quizService = new QuizService();

        // Create
        quizService.create("Quiz 1", new Date());
        quizService.create("Quiz 2", new Date());

        // Read
        System.out.println("All quizzes:");
        for (Quiz quiz : quizService.getAll()) {
            System.out.println(quiz);
        }

        // Update
        quizService.updateQuiz(1, "Updated Quiz 1", new Date());

        // Read after update
        System.out.println("After update:");
        for (Quiz quiz : quizService.getAllQuizzes()) {
            System.out.println(quiz);
        }

        // Delete
        quizService.deleteQuiz(2);

        // Read after delete
        System.out.println("After delete:");
        for (Quiz quiz : quizService.getAllQuizzes()) {
            System.out.println(quiz);
        }
    }
}
