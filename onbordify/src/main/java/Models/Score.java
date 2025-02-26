package Models;

public class Score {
    int id;
    int idUser;
    double score;

    public Score(int id, int idUser, double score) {
        this.id = id;
        this.idUser = idUser;
        this.score = score;
    }

    public Score(int idUser, double score) {
        this.idUser = idUser;
        this.score = score;
    }

    public Score() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
