package Models;

public class Avis {
    private int id;
    private int idProgramme;
    private int idUser;
    private int rating;
    private String commentaire;

    // Constructeur
    public Avis(int idProgramme, int idUser, int rating, String commentaire) {
        this.idProgramme = idProgramme;
        this.idUser = idUser;
        this.rating = rating;
        this.commentaire = commentaire;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getRating() {
        return rating;
    }

    public void setrating(int rating) {
        this.rating = rating;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}