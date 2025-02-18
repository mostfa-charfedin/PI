package Models;

public class Projet {

        private int idProjet;
        private String titre;
        private String Description;
        private int idUser;

        public Projet() {
        }

    public Projet(String titre, String description, int idUser) {
        this.titre = titre;
        Description = description;
        this.idUser = idUser;
    }

    public Projet(int idProjet, String titre, String description, int idUser) {
        this.idProjet = idProjet;
        this.titre = titre;
        Description = description;
        this.idUser = idUser;
    }

    public int getIdProjet() {
        return idProjet;
    }

    public void setIdProjet(int idProjet) {
        this.idProjet = idProjet;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getidUser() {
        return idUser;
    }

    public void setidUser(int idUser) {
        this.idUser = idUser;
    }

    @Override
        public String toString() {
            return "User{" +
                    "Description='" + Description + '\'' +
                    ", titre='" + titre + '\'' +
                    '}';
        }
    }

