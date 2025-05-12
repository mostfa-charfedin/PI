package Models;

import java.sql.Timestamp;

public class Commentaire {
    private int id;
    private String contenu;
    private int userId;
    private int posteId;
    private Timestamp createdAt;
    private String userNom;
    private String userPrenom;

    // Constructors
    public Commentaire() {}

    public Commentaire(String contenu, int userId, int posteId) {
        this.contenu = contenu;
        this.userId = userId;
        this.posteId = posteId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getPosteId() { return posteId; }
    public void setPosteId(int posteId) { this.posteId = posteId; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    public String getUserPrenom() {
        return userPrenom;
    }

    public void setUserPrenom(String userPrenom) {
        this.userPrenom = userPrenom;
    }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", contenu='" + contenu + '\'' +
                ", userId=" + userId +
                ", posteId=" + posteId +
                ", createdAt=" + createdAt +
                '}';
    }
}