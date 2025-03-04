package Services ;

import Model.Ressource;
import utils.MyDb;
import Services.CrudInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RessourceService implements CrudInterface<Ressource> {
    private Connection connection;

    public RessourceService() {
        connection = MyDb.getInstance().getConnection();
    }

    // CREATE
    @Override
    public void create(Ressource res) throws Exception {
        String sql = "INSERT INTO onboardify.ressources (titre, type, description, lien) VALUES ('"
                + res.getTitre() + "', '" + res.getType() + "', '"
                + res.getDescription() + "', '" + res.getlien() + "')";
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }

    // UPDATE
    @Override
    public void update(Ressource res) throws Exception {
        String sql = "UPDATE onboardify.ressources SET " +
                "titre = '" + res.getTitre() + "', " +
                "type = '" + res.getType() + "', " +
                "description = '" + res.getDescription() + "', " +
                "lien = '" + res.getlien() + "' " +
                "WHERE idResource = " + res.getIdResource();

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }


    // DELETE
    @Override
    public void delete(int IdResource) throws Exception {
        String sql = "DELETE FROM onboardify.ressources WHERE idResource = " + IdResource;

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }


    // READ
    @Override
    public List<Ressource> getAll() throws Exception {
        String sql = "SELECT * FROM onboardify.ressources";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Ressource> list = new ArrayList<>();
        while (rs.next()) {
            Ressource res = new Ressource();

            res.setTitre(rs.getString("titre"));
            res.setType(rs.getString("type"));
            res.setDescription(rs.getString("description"));
            res.setlien(rs.getString("lien"));
            list.add(res);
        }
        return list;
    }
}
