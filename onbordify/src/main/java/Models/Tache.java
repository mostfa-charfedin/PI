package Models;

public class Tache {
    private int idTache;
    private String titre;
    private String Description;
    private int idProjet;
    private int idUser;
    private String nom;
    private String prenom;
    private String TitreProjet;
    private int date ;
    private String status;
    public Tache() {
    }

    public Tache(int idTache, String titre, String description, int idProjet, int idUser, String nom, String prenom, String titreProjet, int date, String status) {
        this.idTache = idTache;
        this.titre = titre;
        Description = description;
        this.idProjet = idProjet;
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        TitreProjet = titreProjet;
        this.date = date;
        this.status = status;
    }

    public Tache(int idTache, String titre , String description, int idProjet, int idUser, String nom, String prenom, String titreProjet) {
        this.idTache = idTache;
        TitreProjet = titreProjet;
        this.prenom = prenom;
        this.nom = nom;
        this.idUser = idUser;
        this.idProjet = idProjet;
        Description = description;
        this.titre = titre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Tache(String titre, String description, int idProjet, String nom, String prenom, int date) {
        this.titre = titre;
        this.Description = description;
        this.idProjet = idProjet;
        this.nom = nom;
        this.prenom = prenom;
        this.date = date;
    }

    public Tache(String titre, String nom, String prenom, String description) {
        this.titre = titre;
        this.nom = nom;
        this.prenom = prenom;
        Description = description;
    }

    public Tache(String titre, String description, int idProjet, int idUser, String nom, String prenom, String TitreProjet ) {
        this.titre = titre;
        this.Description = description;
        this.idProjet = idProjet;
        this.idUser = idUser;
        this.nom=nom;
        this.prenom=prenom;
        this.TitreProjet=TitreProjet;
    }

    public String getTitreProjet() {
        return TitreProjet;
    }

    public void setTitreProjet(String titreProjet) {
        TitreProjet = titreProjet;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "User{" +
                "Description='" + Description + '\'' +
                ", titre='" + titre + '\'' +
                '}';
    }
}
