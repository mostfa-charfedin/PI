package service;

import Models.Commentaire;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {
    static Connection conn = MyDb.getInstance().getConnection();

    public void add(Commentaire commentaire) {
        String query = "INSERT INTO commentaire (titre, description, imagePath, idUser, idPublication) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, commentaire.getTitre());
            ps.setString(2, commentaire.getDescription());
            ps.setString(3, commentaire.getImagePath());
            ps.setInt(4, commentaire.getIdUser());
            ps.setInt(5, commentaire.getIdPublication());
            ps.executeUpdate();
            System.out.println("Commentaire added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    public List<Commentaire> getAll() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setIdCommentaire(rs.getInt("idCommentaire"));
                commentaire.setTitre(rs.getString("titre"));
                commentaire.setDescription(rs.getString("description"));
                commentaire.setImagePath(rs.getString("imagePath"));
                commentaire.setIdUser(rs.getInt("idUser"));
                commentaire.setIdPublication(rs.getInt("idPublication"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return commentaires;
    }
    public List<Commentaire> getCommentsByPublication(int publicationId) {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE idPublication = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, publicationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setIdCommentaire(rs.getInt("idCommentaire"));
                commentaire.setTitre(rs.getString("titre"));
                commentaire.setDescription(rs.getString("description"));
                commentaire.setImagePath(rs.getString("imagePath"));
                commentaire.setIdUser(rs.getInt("idUser"));
                commentaire.setIdPublication(rs.getInt("idPublication"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comments: " + e.getMessage());
        }
        return commentaires;
    }

    public void delete(int commentId) {
        String query = "DELETE FROM commentaire WHERE idCommentaire = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, commentId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comment deleted successfully!");
            } else {
                System.out.println("No comment found with ID: " + commentId);
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un commentaire
    public void update(Commentaire commentaire) {
        String query = "UPDATE commentaire SET titre = ?, description = ?, imagePath = ?, idUser = ?, idPublication = ? WHERE idCommentaire = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, commentaire.getTitre());
            ps.setString(2, commentaire.getDescription());
            ps.setString(3, commentaire.getImagePath());
            ps.setInt(4, commentaire.getIdUser());
            ps.setInt(5, commentaire.getIdPublication());
            ps.setInt(6, commentaire.getIdCommentaire());  // Utilisation de l'ID pour identifier quel commentaire mettre à jour
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comment updated successfully!");
            } else {
                System.out.println("No comment found with ID: " + commentaire.getIdCommentaire());
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }
}
