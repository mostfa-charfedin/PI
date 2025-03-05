package Model;

import java.util.List;

public class Ressource {
    private int idResource;
    private int idUser ;
    private String titre;
    private String type;
    private String description;
    private String lien;
    private double noteAverage;
    private List<Evaluation> evaluations;

    public Ressource() {}

    public Ressource(int idResource, int idUser, String titre, String type, String description, String lien, double noteAverage) {
        this.idUser = idUser;
        this.idResource = idResource;
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.lien = lien;
        this.noteAverage = noteAverage;  // Ajout de la note moyenne
    }

    public int getIdResource() {
        return idResource;
    }

    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }

    public List<Evaluation> getEvaluations() {
        return evaluations;
    }
    public void setEvaluations(List<Evaluation> evaluations) {
        this.evaluations = evaluations;
    }
    public int getidUser () {
        return idUser ;
    }

    public void setidUser(int idUser ) {
        this.idUser  = idUser ;
    }
    public void setNoteAverage(double noteAverage) {
        this.noteAverage = noteAverage;
    }

    // Méthode pour obtenir la note moyenne
    public double getNoteAverage() {
        return noteAverage;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getlien() {
        return lien;
    }

    public void setlien(String url) {
        this.lien = url;
    }

    // Méthode pour obtenir le niveau de satisfaction
    public String getSatisfaction() {
        if (noteAverage >= 4.5) {
            return "Très Satisfaisant";
        } else if (noteAverage >= 3.5) {
            return "Satisfaisant";
        } else if (noteAverage >= 2.5) {
            return "Moyen";
        } else {
            return "Insatisfaisant";
        }
    }
    @Override
    public String toString() {
        return "Ressource{" +
                "idResource=" + idResource +
                "idUser =" + idUser  +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", lien='" + lien + '\'' +
                ", noteAverage=" + noteAverage +
                '}';
    }

}
