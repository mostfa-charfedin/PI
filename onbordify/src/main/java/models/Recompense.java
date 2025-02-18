package models;

public class Recompense {
    private int idRecompense;
    private String type_Recompense;
    private String dateAttribution;
    private String statutdeRecompense;
    private int idProgramme;  // Association avec ProgrammeBienEtre

    public Recompense(int idRecompense, String type_Recompense, String dateAttribution, String statutdeRecompense, int idProgramme) {
        this.idRecompense = idRecompense;
        this.type_Recompense = type_Recompense;
        this.dateAttribution = dateAttribution;
        this.statutdeRecompense = statutdeRecompense;
        this.idProgramme = idProgramme;
    }

    public int getIdRecompense() {
        return idRecompense;
    }
    public String getType_Recompense() {
        return type_Recompense;
    }
    public String getDateAttribution() {
        return dateAttribution;
    }
    public String getStatutdeRecompense() {
        return statutdeRecompense;
    }
    public int getIdProgramme() {
        return idProgramme;
    }

    public void setIdRecompense(int idRecompense) {
        this.idRecompense = idRecompense;
    }
    public void setType_Recompense(String type_Recompense) {
        this.type_Recompense = type_Recompense;
    }
    public void setDateAttribution(String dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
    public void setStatutdeRecompense(String statutdeRecompense) {
        this.statutdeRecompense = statutdeRecompense;
    }
    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }
}
