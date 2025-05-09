package Models;

public class Commentaire {
    private int idCommentaire;
    private String titre;
    private String description;
    private String imagePath;
    private int idUser;
    private int idPublication; // Foreign key

    // Constructors
    public Commentaire() {
    }

    public Commentaire(int idCommentaire, String titre, String description, String imagePath, int idUser, int idPublication) {
        this.idCommentaire = idCommentaire;
        this.titre = titre;
        this.description = description;
        this.imagePath = imagePath;
        this.idUser = idUser;
        this.idPublication = idPublication;
    }

    // Getters and Setters
    public int getIdCommentaire() {
        return idCommentaire;
    }

    public void setIdCommentaire(int idCommentaire) {
        this.idCommentaire = idCommentaire;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
    }
}