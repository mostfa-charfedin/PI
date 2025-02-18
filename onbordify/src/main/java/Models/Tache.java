package Models;

public class Tache {
    private int idTache;
    private String titre;
    private String Description;
    private int idProjet;
    private int idUser;
    public Tache() {
    }

    public Tache(String titre, String description, int idProjet, int idUser) {
        this.titre = titre;
        Description = description;
        this.idProjet = idProjet;
        this.idUser = idUser;
    }

    public Tache(int idTache, String titre, String description, int idProjet, int idUser) {
        this.idTache = idTache;
        this.titre = titre;
        Description = description;
        this.idProjet = idProjet;
        this.idUser = idUser;
    }

    public int getIdTache() {
        return idTache;
    }

    public void setIdTache(int idTache) {
        this.idTache = idTache;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    @Override
    public String toString() {
        return "User{" +
                "Description='" + Description + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}
