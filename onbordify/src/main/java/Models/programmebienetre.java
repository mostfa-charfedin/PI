package Models;

public class programmebienetre {
    private int idProgramme;
    private String titre;
    private String type;
    private String description;
    private int idUser;       // Identifiant de l'utilisateur
    private String nom;       // Nom de l'utilisateur
    private String prenom;    // Prénom de l'utilisateur
    private String email;     // Email de l'utilisateur
    private String date_programme;  // Date du programme
    private int idRecompense; // ID de la récompense associée

    // Constructeur par défaut
    public programmebienetre() {
        this.idProgramme = 0;
        this.titre = "";
        this.type = "";
        this.description = "";
        this.idUser = 0;
        this.nom = "";
        this.prenom = "";
        this.email = "";
        this.date_programme = "";
        this.idRecompense = 0;
    }

    // Constructeur complet
    public programmebienetre(int idProgramme, String titre, String type, String description, String date_programme, int idUser) {
        this.idProgramme = idProgramme;
        this.titre = titre;
        this.type = type;
        this.description = description;
        this.date_programme = date_programme;
        this.idUser = idUser;
        this.nom = "";
        this.prenom = "";
        this.email = "";
        this.idRecompense = 0;
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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_programme() {
        return date_programme;
    }

    public void setDate_programme(String date_programme) {
        this.date_programme = date_programme;
    }

    public int getIdRecompense() {
        return idRecompense;
    }

    public void setIdRecompense(int idRecompense) {
        this.idRecompense = idRecompense;
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
                ", date_programme='" + date_programme + '\'' +
                ", idRecompense=" + idRecompense +
                '}';
    }
}
