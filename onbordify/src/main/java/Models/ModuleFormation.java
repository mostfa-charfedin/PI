package Models;

import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;

public class ModuleFormation {

    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private ObservableList<User> membres;

    public ModuleFormation(String nom, Date dateDebut, Date dateFin, ObservableList<User> membres) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.membres = membres;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public ObservableList<User> getMembres() {
        return membres;
    }

    public void setMembres(ObservableList<User> membres) {
        this.membres = membres;
    }

    @Override
    public String toString() {
        return "ModuleFormation{" +
                "nom='" + nom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", membres=" + membres +
                '}';
    }
}