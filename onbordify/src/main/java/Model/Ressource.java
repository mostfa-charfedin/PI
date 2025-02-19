package Model;

public class Ressource {
    private int id;
    private String titre;
    private String type;
    private String description;
    private String lien;

    public Ressource() {}

    public Ressource(int id, String titre, String type, String description, String lien) {
        this.id = id;
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.lien = lien;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", lien='" + lien + '\'' +
                '}';
    }

}
