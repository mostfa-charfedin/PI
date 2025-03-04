package Model;

import java.util.List;

public class Ressource {
    private int idResource;
    private int idUser ;
    private String titre;
    private String type;
    private String description;
    private String lien;
    private List<Evaluation> evaluations;

    public Ressource() {}

    public Ressource(int idResource, int idUser , String titre, String type, String description, String lien) {
        this.idUser  = idUser ;
        this.idResource = idResource;
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.lien = lien;
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

    public void setId(int idUser ) {
        this.idUser  = idUser ;
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
    @Override
    public String toString() {
        return "Ressource{" +
                "idResource=" + idResource +
                "idUser =" + idUser  +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", lien='" + lien + '\'' +
                '}';
    }

}
