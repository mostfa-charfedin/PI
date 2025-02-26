package Models;

public class Reponse {
    private int idReponse;
    private String reponse;
    private String statut;
    private int idQuestion; // Reference to the question

    public Reponse() {}

    public Reponse(int idReponse, String reponse, String statut, int idQuestion) {
        this.idReponse = idReponse;
        this.reponse = reponse;
        this.statut = statut;
        this.idQuestion = idQuestion;
    }

    public Reponse(String reponse, String statut, int idQuestion) {
        this.reponse = reponse;
        this.statut = statut;
        this.idQuestion = idQuestion;
    }

    public Reponse(String reponse, String statut) {
        this.reponse = reponse;
        this.statut = statut;
    }

    // Getters and Setters
    public int getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(int idReponse) {
        this.idReponse = idReponse;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "idReponse=" + idReponse +
                ", reponse='" + reponse + '\'' +
                ", statut='" + statut + '\'' +
                ", idQuestion=" + idQuestion +
                '}';
    }
}