package Models;

import java.util.List;

public class Publication {
    private int id;
    private int userId;
    private String title;
    private String contenu;
    private String image;
    private Boolean signaled;
    private String categories; // JSON string
    private List<Commentaire> commentaires;

    // Constructors
    public Publication() {
    }

    public Publication(int id, int userId, String title, String contenu, String image, Boolean signaled, String categories) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.contenu = contenu;
        this.image = image;
        this.signaled = signaled;
        this.categories = categories;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getSignaled() {
        return signaled;
    }

    public void setSignaled(Boolean signaled) {
        this.signaled = signaled;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}