package Models;

import java.time.LocalDate;

public class Recompense {
    private int idRecompense;
    private String type_Recompense;
    private LocalDate dateAttribution;
    private String statusRecompence;
    private int idProgramme;  // Association avec ProgrammeBienEtre

    public Recompense(int idRecompense, String type_Recompense, LocalDate dateAttribution, String statusRecompence, int idProgramme) {
        this.idRecompense = idRecompense;
        this.type_Recompense = type_Recompense;
        this.dateAttribution = dateAttribution;
        this.statusRecompence = statusRecompence;
        this.idProgramme = idProgramme;
    }

    public int getIdRecompense() {
        return idRecompense;
    }
    public String getType_Recompense() {
        return type_Recompense;
    }
    public LocalDate getDateAttribution() {
        return dateAttribution;
    }
    public String getStatusRecompence() {
        return statusRecompence;
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
    public void setDateAttribution(LocalDate dateAttribution) {
        this.dateAttribution = dateAttribution;
    }
    public void setStatusRecompence(String statusRecompence) {
        this.statusRecompence = statusRecompence;
    }
    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }
}
