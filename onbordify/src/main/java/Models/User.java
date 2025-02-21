package Models;

import java.util.Date;

public class User {
    private int id ;
    private String nom ;
    private String prenom ;
    private String email ;
    private int cin;
    private Date dateNaissance ;
    private String password ;
    private Role role ;

    public User(){}

    public User(int id, String nom, String prenom, String email, int cin, Date dateNaissance, String password, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.password = password;
        this.role = role;
    }
    /*sans id pour l'affichage*/
    public User(String nom, String prenom, String email, int cin, Date dateNaissance, String password, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.password = password;
        this.role = role;
    }
/*sans password car admin ne peut pas changer mot de passe de l'utilisateur*/
    public User(String nom, String prenom, String email, int cin, Date dateNaissance, Role role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.cin = cin;
        this.dateNaissance = dateNaissance;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + nom + '\'' +
                ", lastName='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", cin='" + cin + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", role='" + role + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    @Override
    public int hashCode() {

        return super.hashCode();
    }
}
