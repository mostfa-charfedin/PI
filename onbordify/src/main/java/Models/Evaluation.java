package Models;

import java.util.Date;

public class Evaluation {
    private int idEvaluation;
    private int idUser ;
    private int idResource;
    private int note;
    private Date dateEvaluation;

    public Evaluation(int idUser , int idResource, int note) {
        this.idUser  = idUser ;
        this.idResource = idResource;
        this.note = note;
        this.dateEvaluation = new Date();
    }

    public int getIdEvaluation() { return idEvaluation; }
    public void setIdEvaluation(int idEvaluation) { this.idEvaluation = idEvaluation; }

    public int getidUser () { return idUser ; }
    public void setidUser (int idUser ) { this.idUser  = idUser ; }

    public int getIdResource() { return idResource; }
    public void setIdResource(int idResource) { this.idResource = idResource; }

    public int getNote() { return note; }
    public void setNote(int note) {
        if (note >= 1 && note <= 5) {
            this.note = note;
        } else {
            throw new IllegalArgumentException("La note doit être entre 1 et 5.");
        }
    }

    public Date getDateEvaluation() { return dateEvaluation; }
    public void setDateEvaluation(Date dateEvaluation) { this.dateEvaluation = dateEvaluation; }

    @Override
    public String toString() {
        return "Evaluation{" +
                "idEvaluation=" + idEvaluation +
                ", idUser =" + idUser  +
                ", idResource=" + idResource +
                ", note=" + note +
                ", dateEvaluation=" + dateEvaluation +
                '}';
    }
}
