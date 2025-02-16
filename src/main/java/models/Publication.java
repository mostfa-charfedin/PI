package models;

public class Publication {
    // Attributs (champs)
    private int idPublication;
    private String body;
    private int idUser;

    // Constructeur par défaut
    public Publication() {}

    // Constructeur avec paramètres
    public Publication(int idPublication, String body, int idUser) {
        this.idPublication = idPublication;
        this.body = body;
        this.idUser = idUser;
    }

    // Getters et Setters
    public int getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(int idPublication) {
        this.idPublication = idPublication;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
        return "Publication{" +
                "idPublication=" + idPublication +
                ", body='" + body + '\'' +
                ", idUser=" + idUser +
                '}';
    }
}