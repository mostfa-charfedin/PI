package modles;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class Quiz {

    private int idQuiz;
    private  String nom;
    private Date dateCreation;
    private List<Question> questions;

    public Quiz() {};

    public Quiz(int idQuiz, List<Question> questions, Date dateCreation, String nom) {
        this.idQuiz = idQuiz;
        this.questions = questions;
        this.dateCreation = dateCreation;
        this.nom = nom;
    }


    public Quiz(int idQuiz, String nom, Date dateCreation) {
        this.idQuiz = idQuiz;
        this.nom = nom;
        this.dateCreation = dateCreation;
    }

    public Quiz(String nom, Date dateCreation) {
        this.nom = nom;
        this.dateCreation = dateCreation;
    }

    public Quiz(String nom, Date dateCreation, List<Question> questions) {
        this.nom = nom;
        this.dateCreation = dateCreation;
        this.questions = questions;
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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
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
