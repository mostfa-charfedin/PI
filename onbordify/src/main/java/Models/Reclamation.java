package Models;
import java.time.LocalDate;

public class Reclamation {
    // Attributs (champs)
    private int idReclamation;
    private String commentaire;
    private LocalDate date;
    private int idUser;
    private String etat; // Changed to String to match ENUM values

    // Constructeur par défaut
    public Reclamation() {}

    public Reclamation( int idUser, LocalDate date, String commentaire,String etat) {

        this.idUser = idUser;
        this.date = date;
        this.commentaire = commentaire;
        this.etat = etat; // Added this line to initialize the etat field to the provided value.
    }

    // Constructeur avec paramètres
    public Reclamation(int idReclamation, String commentaire, LocalDate date, int idUser) {
        this.idReclamation = idReclamation;
        this.commentaire = commentaire;
        this.date = date;
        this.idUser = idUser;
        this.etat = etat;
    }

    // Getters et Setters
    public int getIdReclamation() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation = idReclamation;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getEtat() { return etat; }
    public void setEtat(String etat) {
        if (etat.equals("Resolved") || etat.equals("Pending") || etat.equals("Rejected")) {
            this.etat = etat;
        } else {
            throw new IllegalArgumentException("Invalid status: " + etat);
        }
    }

    // Méthode toString pour afficher l'objet
    @Override
    public String toString() {
        return "Reclamation{" +
                "idReclamation=" + idReclamation +
                ", commentaire='" + commentaire + '\'' +
                ", date=" + date +
                ", idUser=" + idUser +
                ", etat=" + etat +
                '}';
    }
}