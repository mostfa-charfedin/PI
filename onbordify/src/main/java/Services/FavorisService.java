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

    public void create(Favoris fav) throws Exception {
        String dateAjoutStr = (fav.getDateAjout() != null)
                ? "'" + new Date(fav.getDateAjout().getTime()) + "'"
                : "NULL";

        String sql = "INSERT INTO Favoris (idResource, date, commentaire, note) VALUES ('"
                + fav.getIdRessource() + "', " + dateAjoutStr
                + ", '" + fav.getCommentaire() + "', '" + fav.getNote() + "')";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sql);
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
            fav.setDate(rs.getDate("date"));
            fav.setCommentaire(rs.getString("commentaire"));
            fav.setNote(rs.getInt("note"));
            list.add(fav);
        }
        return list;
    }

    // UPDATE
    @Override
    public void update(Favoris fav) throws Exception {
        String sql = "UPDATE Favoris SET commentaire = ?, note = ? WHERE idFavoris = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, fav.getCommentaire());
        pstmt.setInt(2, fav.getNote());
        pstmt.setInt(3, fav.getIdFavoris());
        pstmt.executeUpdate();
    }

    // DELETE
    @Override
    public void delete(int idFavoris) throws Exception {
        String sql = "DELETE FROM Favoris WHERE idFavoris = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, idFavoris);
        pstmt.executeUpdate();
    }
}
