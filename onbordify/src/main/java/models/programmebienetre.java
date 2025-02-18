package models;

public class programmebienetre {


        private int idProgramme;
        private String titre;
        private String type;
        private String description;

        public programmebienetre(int idProgramme, String titre, String type, String description) {
            this.idProgramme = idProgramme;
            this.titre = titre;
            this.type = type;
            this.description = description;
        }

        public programmebienetre() {}

        public int getIdProgramme() {
            return idProgramme;
        }

        public void setIdProgramme(int idProgramme) {
            this.idProgramme = idProgramme;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return titre + " (" + type + ")";
        }
    }


