package Models;

import java.sql.Date;
import java.util.List;

public class Publication {
    private int idPublication;
    private String contenu;
    private Date date;
    private int idUser;
    private List<Commentaire> commentaires;

    // Constructors
    public Publication() {
    }

    public Publication(int idPublication, String contenu, Date date, int idUser) {
        this.idPublication = idPublication;
        this.contenu = contenu;
        this.date = date;
        this.idUser = idUser;
    }

    // Getters and Setters
    public int getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}