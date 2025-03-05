package Models;

import java.util.Date;

public class Favoris {
    private int idFavoris;
    private int idRessource;
    private String TitreRessource;
    private int note;

    public Favoris() {}

    public Favoris(int idFavoris, int idRessource, Date dateAjout, String commentaire, int note) {
        this.idFavoris = idFavoris;
        this.idRessource = idRessource;
        this.TitreRessource = TitreRessource;
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



    public String getTitreRessource() {
        return TitreRessource;
    }

    public void setTitreRessource(String TitreRessource) {
        this.TitreRessource = TitreRessource;
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
                ", TitreRessource ='" + TitreRessource + '\'' +
                ", note=" + note +
                '}';
    }
}
