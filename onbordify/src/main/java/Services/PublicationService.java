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
        String query = "INSERT INTO publication (contenu, date, idUser) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, publication.getContenu());
            ps.setDate(2, publication.getDate());
            ps.setInt(3, publication.getIdUser());
            ps.executeUpdate();
            System.out.println("Publication added successfully!");
        } catch (SQLException e) {
            System.out.println("Add Error: " + e.getMessage());
        }
    }

    public List<Publication> getAll() {
        List<Publication> publications = new ArrayList<>();
        String query = "SELECT * FROM publication";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                Publication publication = new Publication();
                publication.setIdPublication(rs.getInt("idPublication"));
                publication.setContenu(rs.getString("contenu"));
                publication.setDate(rs.getDate("date"));
                publication.setIdUser(rs.getInt("idUser"));
                publications.add(publication);
            }
        } catch (SQLException e) {
            System.out.println("Display Error: " + e.getMessage());
        }
        return publications;
    }
    public void delete(int idPublication) {
        String query = "DELETE FROM publication WHERE idPublication = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idPublication);
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

    // Nouvelle méthode update
    public void update(Publication publication) {
        String query = "UPDATE publication SET contenu = ?, date = ?, idUser = ? WHERE idPublication = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, publication.getContenu());
            ps.setDate(2, publication.getDate());
            ps.setInt(3, publication.getIdUser());
            ps.setInt(4, publication.getIdPublication());
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
