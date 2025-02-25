package models;

public class programmebienetre {
    private int idProgramme;
    private String titre;
    private String type;
    private String description;
    private int idUser;       // Identifiant de l'utilisateur
    private String nom;       // Nom de l'utilisateur
    private String prenom;    // Prénom de l'utilisateur
    private String email;      // Email de l'utilisateur

    // Constructeur par défaut
    public programmebienetre() {}

    // Constructeur complet
    public programmebienetre(int idProgramme, String titre, String type, String description, int idUser, String nom, String prenom, String email) {
        this.idProgramme = idProgramme;
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }

    // Getters et Setters
    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
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

    public int getidUser() {
        return idUser;
    }

    public void setidUser(int idUser) {
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

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ProgrammeBienetre{" +
                "idProgramme=" + idProgramme +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", idUser=" + idUser +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}