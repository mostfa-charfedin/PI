package Services;

import Models.favoirs;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavorisService implements CrudInterface<Favoris> {
    private Connection connection;

    public FavorisService() {
        connection = MyDb.getMydb().getConnection();
    }

    public void create(favoirs fav) throws SQLException {
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
    public List<favoirs> getAll() throws Exception {
        String sql = "SELECT * FROM favoirs";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<favoirs> list = new ArrayList<>();
        while (rs.next()) {
            favoirs fav = new favoirs();
            fav.setidFavoirs(rs.getInt("idFavoirs"));
            fav.setIdRessource(rs.getInt("idResource"));

            fav.setTitreRessource(rs.getString("TitreRessource"));
            fav.setNote(rs.getInt("note"));
            list.add(fav);
        }
        return list;
    }

    // UPDATE
    @Override
    public void update(favoirs fav) throws Exception {
        String sql = "UPDATE Favoris SET TitreRessource = '" + fav.getTitreRessource() +
                "', note = '" + fav.getNote() +
                "' WHERE idFavoirs = " + fav.getidFavoirs();

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }

    // DELETE
    @Override
    public void delete(int idFavoirs) throws Exception {
        String sql = "DELETE FROM Favoris WHERE idFavoirs = " + idFavoirs;

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
    }
}

