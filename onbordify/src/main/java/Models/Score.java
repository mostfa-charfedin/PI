package Models;

public class Score {
    int idUser;
    int idQuiz;
    double score;

    public Score() {
    }


    public Score(int idUser, int idQuiz, double score) {
        this.idUser = idUser;
        this.idQuiz = idQuiz;
        this.score = score;
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
