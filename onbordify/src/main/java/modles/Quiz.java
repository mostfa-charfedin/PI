package modles;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Quiz {

    private int idQuiz;
    private static String nom;
    private static String dateCreation;
    private List<Question> questions;

    public Quiz() {};

    //les questions de quiz
    public Quiz(int idQuiz, String nom, String dateCreation) {
        this.idQuiz = idQuiz;
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.questions = new ArrayList<>();
    }

    public Quiz(String nom, String dateCreation) {
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.questions = new ArrayList<>();
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public  void setIdQuiz(int idQuiz) {
       this.idQuiz = idQuiz;
    }

    public  String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public  String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }


    @Override
    public String toString() {
        return "Quiz{" +
                "idQuiz=" + idQuiz +
                ", nom='" + nom + '\'' +
                ", dateCreation='" + dateCreation + '\'' +
                ", questions=" + questions +
                '}';
    }
}
