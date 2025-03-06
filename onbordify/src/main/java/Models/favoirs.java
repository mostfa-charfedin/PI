package Models;

import java.util.Date;

public class favoirs {
    private int idFavoirs;
    private int idRessource;
    private String TitreRessource;
    private int note;

    public favoirs() {}

    public favoirs(int idFavoirs, int idRessource, Date dateAjout, String commentaire, int note) {
        this.idFavoirs = idFavoirs;
        this.idRessource = idRessource;
        this.TitreRessource = TitreRessource;
        this.note = note;
    }

    // Getters et Setters
    public int getidFavoirs() {
        return idFavoirs;
    }

    public void setidFavoirs(int idFavoirs) {
        this.idFavoirs = idFavoirs;
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
                "idFavoirs=" + idFavoirs +
                ", idRessource=" + idRessource +
                ", TitreRessource ='" + TitreRessource + '\'' +
                ", note=" + note +
                '}';
    }
}
