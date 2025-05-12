package Services;

import Models.Commentaire;
import Models.Publication;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PublicationService {
    static Connection conn = MyDb.getMydb().getConnection();

    public void add(Publication publication) {
        String query = "INSERT INTO poste (user_id, title, contenu, image, signaled, categories) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, publication.getUserId());
            ps.setString(2, publication.getTitle());
            ps.setString(3, publication.getContenu());
            ps.setString(4, publication.getImage());
            ps.setBoolean(5, publication.getSignaled() != null ? publication.getSignaled() : false);
            ps.setString(6, publication.getCategories());
            ps.executeUpdate();
            System.out.println("Publication added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    public List<Publication> getAll() {
        List<Publication> publications = new ArrayList<>();
        String query = "SELECT * FROM poste";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Publication publication = new Publication();
                publication.setId(rs.getInt("id"));
                publication.setUserId(rs.getInt("user_id"));
                publication.setTitle(rs.getString("title"));
                publication.setContenu(rs.getString("contenu"));
                publication.setImage(rs.getString("image"));
                publication.setSignaled(rs.getBoolean("signaled"));
                publication.setCategories(rs.getString("categories"));
                publications.add(publication);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return publications;
    }


    public void delete(int id) {
        String query = "DELETE FROM poste WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Publication deleted successfully!");
            } else {
                System.out.println("No publication found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Delete Error: " + e.getMessage());
        }
    }

    public void update(Publication publication) {
        String query = "UPDATE poste SET user_id = ?, title = ?, contenu = ?, image = ?, signaled = ?, categories = ? WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, publication.getUserId());
            ps.setString(2, publication.getTitle());
            ps.setString(3, publication.getContenu());
            ps.setString(4, publication.getImage());
            ps.setBoolean(5, publication.getSignaled() != null ? publication.getSignaled() : false);
            ps.setString(6, publication.getCategories());
            ps.setInt(7, publication.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Publication updated successfully!");
            } else {
                System.out.println("No publication found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Update Error: " + e.getMessage());
        }
    }
}