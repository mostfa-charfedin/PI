package Model;

import java.util.Date;

public class Favoris {
    private int idFavoris;
    private int idRessource;
    private Date dateAjout;
    private String commentaire;
    private int note;

    public Favoris() {}

    public Favoris(int idFavoris, int idRessource, Date dateAjout, String commentaire, int note) {
        this.idFavoris = idFavoris;
        this.idRessource = idRessource;
        this.dateAjout = dateAjout;
        this.commentaire = commentaire;
        this.note = note;
    }

    // Getters et Setters
    public int getIdFavoris() {
        return idFavoris;
    }

    public void setIdFavoris(int idFavoris) {
        this.idFavoris = idFavoris;
    }

    public int getIdRessource() {
        return idRessource;
    }

    public void setIdRessource(int idRessource) {
        this.idRessource = idRessource;
    }

    public Date getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(Date dateAjout) {
        this.dateAjout = dateAjout;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Favoris{" +
                "idFavoris=" + idFavoris +
                ", idRessource=" + idRessource +
                ", dateAjout=" + dateAjout +
                ", commentaire='" + commentaire + '\'' +
                ", note=" + note +
                '}';
    }
}
