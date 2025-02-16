package models;
import java.time.LocalDate;

public class Reclamation {
    // Attributs (champs)
    private int idReclamation;
    private String commentaire;
    private LocalDate date;
    private int idUser;
    private boolean etat;

    // Constructeur par défaut
    public Reclamation() {}

    // Constructeur avec paramètres
    public Reclamation(int idReclamation, String commentaire, LocalDate date, int idUser, boolean etat) {
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

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
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