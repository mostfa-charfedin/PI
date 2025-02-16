package models;

public class Commentaire {
    // Attributs (champs)
    private int idCommentaire;
    private String message;
    private int idPublication;
    private int idUser;

    // Constructeur par défaut
    public Commentaire() {}

    // Constructeur avec paramètres
    public Commentaire(int idCommentaire, String message, int idPublication, int idUser) {
        this.idCommentaire = idCommentaire;
        this.message = message;
        this.idPublication = idPublication;
        this.idUser = idUser;
    }

    // Getters et Setters
    public int getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(int idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    // Méthode toString pour afficher l'objet
    @Override
    public String toString() {
        return "Commentaire{" +
                "idCommentaire=" + idCommentaire +
                ", message='" + message + '\'' +
                ", idPublication=" + idPublication +
                ", idUser=" + idUser +
                '}';
    }
}