package modles;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int idQuestion;
    private String question;
    private int idQuiz; //ref au quiz a laquelle la question appartient
    private List<Reponse> reponses; //les reponses possibles
    private String nom ;
    public Question(){};

    public String getnom() {
        return nom;
    }

    public void setnom(String nom) {
        this.nom = nom;
    }

    public Question(int idQuestion, String question, int idQuiz) {
        this.idQuestion = idQuestion;
        this.question = question;
        this.idQuiz = idQuiz;
        this.reponses = new ArrayList<>();
    }

    public Question(int idQuestion, String question, int idQuiz, String nom) {
        this.idQuestion = idQuestion;
        this.question = question;
        this.idQuiz = idQuiz;
        this.nom = nom;
    }

    public Question(String question, String nom) {
        this.question = question;
        this.nom = nom;
        ;
    }
    public Question(String question) {
        this.question = question;
        this.reponses = new ArrayList<>();
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }


    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }

    public void addReponse(Reponse reponse) {
        this.reponses.add(reponse);
    }

    @Override
    public String toString() {
        return "Question{" +
                "idQuestion=" + idQuestion +
                ", question='" + question + '\'' +
                ", idQuiz=" + idQuiz +
                ", reponses=" + reponses +
                '}';
    }
}
