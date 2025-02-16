package modles;

public class Reponse {
    private int idReponse;
    private String reponse;
    private boolean statut=false;
    private int idQuestion; // ref lel question

    public Reponse() {};

    public Reponse(int idReponse, String reponse,boolean statut, int idQuestion) {
        this.idReponse = idReponse;
        this.reponse = reponse;
        this.statut = statut;
        this.idQuestion = idQuestion;
    }

    public Reponse(String reponse ,boolean statut , int idQuestion ) {
        this.reponse = reponse;
        this.statut = statut;
        this.idQuestion = idQuestion;
    }

    public int getIdReponse() {
        return idReponse;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
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
                ", statut=" + statut +
                ", idQuestion=" + idQuestion +
                '}';
    }
}
