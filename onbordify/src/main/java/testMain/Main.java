package testMain;

import modles.Quiz;
import Services.QuizService;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        QuizService qq = new QuizService();
        Quiz q = new Quiz("quiz1","15 janvier 2025");
        try {
            // ss.create(s);
            //ss.update(s);
            //   System.out.println(ss.getAll());
            qq.delete(1);
            System.out.println(qq.getAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }}
