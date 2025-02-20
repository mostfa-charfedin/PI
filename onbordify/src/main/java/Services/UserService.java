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
        String sql = "insert into user (nom, prenom, email, cin, dateNaissance, role, password) values(" +
                "'" + obj.getNom() + "','" + obj.getPrenom() + "','" + obj.getEmail()+ "','" + obj.getCin() +
                "','" + obj.getDateNaissance() +  "','" + Role.valueOf(obj.getRole().toString()) +  "','" + obj.generatePassword()  +"')";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        System.out.println(sql);
    }



    @Override
    public void update(User obj) throws SQLException {
        String sql = "update user set nom = ?, prenom = ?, email = ?," +
                " cin = ?, dateNaissance = ?, role = ?,  where id = ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, obj.getNom());
        stmt.setString(2, obj.getPrenom());
        stmt.setString(3, obj.getEmail());
        stmt.setInt(4, obj.getCin());
        stmt.setDate(5, (Date) obj.getDateNaissance());
        stmt.setString(6, obj.getRole().name());
        stmt.setInt(7, obj.getId());
        stmt.executeUpdate();
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