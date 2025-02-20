package Services;

import Model.Favoris;
import Services.CrudInterface;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavorisService implements CrudInterface<Favoris> {
    private Connection connection;

    public FavorisService() {
        connection = MyDb.getInstance().getConnection();
    }

    public void create(Favoris fav) throws SQLException {
        // SQL query to insert a new record into Favoris without idResource
        String sql = "INSERT INTO Favoris (TitreRessource, note) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Set the values for TitreRessource and note
            stmt.setString(1, fav.getTitreRessource());
            stmt.setInt(2, fav.getNote());

            // Execute the query
            stmt.executeUpdate();
        }
    }




    // READ
    @Override
    public List<Favoris> getAll() throws Exception {
        String sql = "SELECT * FROM Favoris";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Favoris> list = new ArrayList<>();
        while (rs.next()) {
            Favoris fav = new Favoris();
            fav.setIdFavoris(rs.getInt("idFavoris"));
            fav.setIdRessource(rs.getInt("idResource"));

            fav.setTitreRessource(rs.getString("TitreRessource"));
            fav.setNote(rs.getInt("note"));
            list.add(fav);
        }
        return list;
    }

    // UPDATE
    @Override
    public void update(Favoris fav) throws Exception {
        String sql = "UPDATE Favoris SET TitreRessource = '" + fav.getTitreRessource() +
                "', note = '" + fav.getNote() +
                "' WHERE idFavoris = " + fav.getIdFavoris();

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }

    // DELETE
    @Override
    public void delete(int idFavoris) throws Exception {
        String sql = "DELETE FROM Favoris WHERE idFavoris = " + idFavoris;

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }
}

