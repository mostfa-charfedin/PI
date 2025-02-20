package Models;

public class Projet {

    private int idProjet;
    private String titre;
    private String description;  // Changed to follow Java naming conventions
    private int idUser;
    private String nom;
    private String prenom;

    public Projet() {
    }

    // Constructor without ID (for creating a new project)
    public Projet(String titre, String description, String nom, String prenom) {
        this.titre = titre;
        this.description = description;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Constructor with all fields (for fetching from database)
    public Projet(int idProjet, String titre, String description,  String nom, String prenom) {
        this.idProjet = idProjet;
        this.titre = titre;
        this.description = description;

        this.nom = nom;
        this.prenom = prenom;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getidUser() {  // Fixed method name
        return idUser;
    }

    public void setidUser(int idUser) {  // Fixed method name
        this.idUser = idUser;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "idProjet=" + idProjet +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", idUser=" + idUser +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                '}';
    }


}
