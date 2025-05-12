package Services;

import Models.Commentaire;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentaireService {
    static Connection conn = MyDb.getMydb().getConnection();

    public void add(Commentaire commentaire) {
        String query = "INSERT INTO comment (user_id, poste_id, created_at, contenu) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, commentaire.getUserId());
            ps.setInt(2, commentaire.getPosteId());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, commentaire.getContenu());
            ps.executeUpdate();
            System.out.println("Comment added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    public List<Commentaire> getAll() {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM comment";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setUserId(rs.getInt("user_id"));
                commentaire.setPosteId(rs.getInt("poste_id"));
                commentaire.setCreatedAt(rs.getTimestamp("created_at"));
                commentaire.setContenu(rs.getString("contenu"));
                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return commentaires;
    }

    public List<Commentaire> getCommentsByPublication(int publicationId) {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT c.*, u.nom, u.prenom FROM comment c " +
                "JOIN user u ON c.user_id = u.id " +
                "WHERE c.poste_id = ? ORDER BY c.created_at DESC";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, publicationId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commentaire commentaire = new Commentaire();
                commentaire.setId(rs.getInt("id"));
                commentaire.setUserId(rs.getInt("user_id"));
                commentaire.setPosteId(rs.getInt("poste_id"));
                commentaire.setCreatedAt(rs.getTimestamp("created_at"));
                commentaire.setContenu(rs.getString("contenu"));

                // Debug user names
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                System.out.println("DEBUG: Fetched comment by user: " + prenom + " " + nom);

                // Ajoutez ces lignes pour récupérer le nom et prénom
                commentaire.setUserNom(rs.getString("nom"));
                commentaire.setUserPrenom(rs.getString("prenom"));

                commentaires.add(commentaire);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving comments: " + e.getMessage());
        }
        return commentaires;
    }

    public void delete(int commentId) {
        String query = "DELETE FROM comment WHERE id = ?";
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

    public void update(Commentaire commentaire) {
        String query = "UPDATE comment SET contenu = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, commentaire.getContenu());
            ps.setInt(2, commentaire.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Comment updated successfully!");
            } else {
                System.out.println("No comment found with ID: " + commentaire.getId());
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }
}