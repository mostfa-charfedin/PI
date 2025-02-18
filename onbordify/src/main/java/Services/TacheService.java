package Services;

import Models.Tache;
import utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TacheService implements CrudInterface<Tache> {
    Connection con;

    public TacheService() {
        this.con = MyDb.getInstance().getConnection();
    }

    @Override
    public void create(Tache obj) throws Exception {
        String sql = "INSERT INTO tache (titre, Description, idProjet, idUser) VALUES (?, ?, ?, ?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, obj.getTitre());
        stmt.setString(2, obj.getDescription());
        stmt.setInt(3, obj.getIdProjet());
        stmt.setInt(4, obj.getIdUser());
        stmt.executeUpdate();
    }

    @Override
    public void update(Tache obj) throws Exception {
        String sql = "UPDATE tache SET titre = ?, Description = ?, idProjet = ?, idUser = ? WHERE idTache = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, obj.getTitre());
        stmt.setString(2, obj.getDescription());
        stmt.setInt(3, obj.getIdProjet());
        stmt.setInt(4, obj.getIdUser());
        stmt.setInt(5, obj.getIdTache());
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM tache WHERE idTache = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    @Override
    public List<Tache> getAll() throws Exception {
        String sql = "SELECT * FROM tache";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Tache> taches = new ArrayList<>();

        while (rs.next()) {
            Tache tache = new Tache();
            tache.setIdTache(rs.getInt("idTache"));
            tache.setTitre(rs.getString("titre"));
            tache.setDescription(rs.getString("Description"));
            tache.setIdProjet(rs.getInt("idProjet"));
            tache.setIdUser(rs.getInt("idUser"));
            taches.add(tache);
        }

        return taches;
    }

    // Get all tasks for a specific project (one-to-many relationship)
    public List<Tache> getTasksByProject(int projectId) throws Exception {
        String sql = "SELECT * FROM tache WHERE idProjet = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, projectId);
        ResultSet rs = stmt.executeQuery();
        List<Tache> taches = new ArrayList<>();

        while (rs.next()) {
            Tache tache = new Tache();
            tache.setIdTache(rs.getInt("idTache"));
            tache.setTitre(rs.getString("titre"));
            tache.setDescription(rs.getString("Description"));
            tache.setIdProjet(rs.getInt("idProjet"));
            tache.setIdUser(rs.getInt("idUser"));
            taches.add(tache);
        }

        return taches;
    }
}
