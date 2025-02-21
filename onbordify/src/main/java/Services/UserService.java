package Services;

import Models.Role;
import Models.User;
import utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements CrudInterface<User> {
    Connection con;

    public UserService() {
        this.con = MyDb.getMydb().getConnection();
    }

    @Override
    public void create(User obj) throws SQLException {
        String sql = "INSERT INTO user (nom, prenom, email, cin, dateNaissance, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, obj.getNom());
            stmt.setString(2, obj.getPrenom());
            stmt.setString(3, obj.getEmail());
            stmt.setInt(4, obj.getCin());
            if (obj.getDateNaissance() != null) {
                stmt.setDate(5, java.sql.Date.valueOf(obj.getDateNaissance().toString()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setString(6, obj.getRole().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Affiche l'erreur complète dans la console
            throw new SQLException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }


    @Override
    public void update(User obj) throws SQLException {
        String sql = "UPDATE user SET nom = ?, prenom = ?, email = ?, cin = ?, dateNaissance = ?, role = ? WHERE id = ?";
        System.out.println(obj);

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, obj.getNom());
            stmt.setString(2, obj.getPrenom());
            stmt.setString(3, obj.getEmail());
            stmt.setInt(4, obj.getCin());

            // Convertir LocalDate en java.sql.Date pour la base de données
            if (obj.getDateNaissance() != null) {
                stmt.setDate(5, java.sql.Date.valueOf(obj.getDateNaissance().toString()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            stmt.setString(6, obj.getRole().toString());
            stmt.setInt(7, obj.getId());

            int rowsUpdated = stmt.executeUpdate();
            System.out.println(rowsUpdated + " user updated.");
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e; // Propager l'exception pour la gestion des erreurs
        }
    }

    public void updatePassword(int id, String newPassword) throws SQLException {
        String sql = "update user set password = ? where id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, newPassword);
        stmt.setInt(2, id);
        stmt.executeUpdate();
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM user WHERE id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();
    }

    @Override
    public List<User> getAll() throws SQLException {
        String sql = "select * from user";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setCin(rs.getInt("cin"));
            user.setDateNaissance(rs.getDate("dateNaissance"));
            user.setRole(Role.valueOf(rs.getObject("role").toString()));
            users.add(user);
        }
        return users;
    }


}