package Services;

import utils.MyDb;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.Reclamation;
import utils.UserSession;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements Services.CrudInterface<Reclamation> {
    static Connection conn = MyDb.getMydb().getConnection();

    UserSession session = UserSession.getInstance();
    int userId = session.getUserId();
    public void add(Reclamation reclamation) {
        String query = "INSERT INTO reclamation (commentaire, date, idUser, etat) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, reclamation.getCommentaire());
            ps.setDate(2, Date.valueOf(reclamation.getDate()));
            ps.setInt(3, userId);
            ps.setString(4, reclamation.getEtat());
            ps.executeUpdate();
            System.out.println("Reclamation added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    public List<Reclamation> getAll() {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setIdReclamation(rs.getInt("idReclamation"));
                reclamation.setCommentaire(rs.getString("commentaire"));
                reclamation.setDate(rs.getDate("date").toLocalDate());
                reclamation.setIdUser(rs.getInt("idUser"));
                reclamation.setEtat(rs.getString("etat"));
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return reclamations;
    }
    public static ObservableList<Reclamation> getAll2() {
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();
        String query = "SELECT * FROM reclamation";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Reclamation reclamation = new Reclamation();
                reclamation.setIdReclamation(rs.getInt("idReclamation"));
                reclamation.setCommentaire(rs.getString("commentaire"));
                reclamation.setDate(rs.getDate("date").toLocalDate());
                reclamation.setIdUser(rs.getInt("idUser"));
                reclamation.setEtat(rs.getString("etat"));
                reclamations.add(reclamation);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return reclamations;
    }




    @Override
    public void create(Reclamation obj) throws Exception {

    }

    public void update(Reclamation reclamation) {
        String query = "UPDATE reclamation SET commentaire=?, date=?, idUser=?, etat=? WHERE idReclamation=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, reclamation.getCommentaire());
            ps.setDate(2, Date.valueOf(reclamation.getDate()));
            ps.setInt(3, reclamation.getIdUser());
            ps.setString(4, reclamation.getEtat());
            ps.setInt(5, reclamation.getIdReclamation());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Reclamation updated successfully!");
            } else {
                System.out.println("No reclamation found with ID: " + reclamation.getIdReclamation());
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws Exception {

    }


    public void delete(Reclamation reclamation) {
        String query = "DELETE FROM reclamation WHERE idReclamation=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, reclamation.getIdReclamation());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reclamation deleted successfully!");
            } else {
                System.out.println("No reclamation found with ID: " + reclamation.getIdReclamation());
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }


    public void delete2(int reclamationId) {
        String query = "DELETE FROM reclamation WHERE idReclamation=?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reclamationId);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reclamation deleted successfully!");
            } else {
                System.out.println("No reclamation found with ID: " + reclamationId);
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    public Reclamation getReclamationById(int idReclamation) {
        Reclamation reclamation = null;
        String query = "SELECT * FROM reclamation WHERE idReclamation = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idReclamation);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                reclamation = new Reclamation();
                reclamation.setIdReclamation(rs.getInt("idReclamation"));
                reclamation.setCommentaire(rs.getString("commentaire"));
                reclamation.setDate(rs.getDate("date").toLocalDate());
                reclamation.setIdUser(rs.getInt("idUser"));
                reclamation.setEtat(rs.getString("etat"));
            }
        } catch (SQLException e) {
            System.out.println("Get Reclamation by ID Error: " + e.getMessage());
        }
        return reclamation;
    }
    public String getUserEmailById(int userId) {
        String email = null;
        String query = "SELECT email FROM user WHERE id = ?";  // Utilisez 'id' au lieu de 'idUser'
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Get User Email by ID Error: " + e.getMessage());
        }
        return email;
    }

    public void updateStatus(int id, String newStatus) {
        String query = "UPDATE reclamation SET etat = ? WHERE idReclamation = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

            // Récupérer l'email du propriétaire de la réclamation
            Reclamation reclamation = getReclamationById(id);
            String userEmail = getUserEmailById(reclamation.getIdUser());

            // Envoyer un email pour informer l'utilisateur du changement de statut
            if (userEmail != null) {
                EmailRecService emailService = new EmailRecService();
                String subject = "Mise à jour du statut de votre réclamation";
                String message = "Le statut de votre réclamation (ID: " + id + ") a été mis à jour à: " + newStatus;
                emailService.sendEmail(userEmail, message, subject);
            } else {
                System.out.println("❌ Aucun email trouvé pour l'utilisateur avec l'ID: " + reclamation.getIdUser());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}