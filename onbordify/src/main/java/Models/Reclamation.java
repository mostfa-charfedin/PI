package Models;

import java.time.LocalDateTime;

public class Reclamation {
    private int id;
    private int userId;
    private Integer respondedById;
    private String subject;
    private String content;
    private Boolean status; // true=resolved, false=rejected, null=pending
    private String response;
    private LocalDateTime createdAt;

    public Reclamation() {}

    public Reclamation(int userId, String subject, String content) {
        this.userId = userId;
        this.subject = subject;
        this.content = content;
        this.createdAt = LocalDateTime.now();
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

    public Integer getRespondedById() {
        return respondedById;
    }

    public void setRespondedById(Integer respondedById) {
        this.respondedById = respondedById;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", userId=" + userId +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}