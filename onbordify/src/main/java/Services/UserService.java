package Services;

import Models.Role;
import Models.User;
import javafx.scene.control.Alert;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.MyDb;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class UserService implements CrudInterface<User> {
    Connection con;
    EmailService emailService = new EmailService();
    String nonHashedPassword;

    public UserService() {
        this.con = MyDb.getMydb().getConnection();
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        this.nonHashedPassword = password.toString();
        return passwordEncoder.encode(password.toString());
    }

    public boolean login(String email, String password) {
        String sql = "SELECT password FROM user WHERE email = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword); // Comparaison avec le hash
            } else {
                System.out.println("Utilisateur non trouvé.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void create(User obj) throws SQLException {
        String sql = "INSERT INTO user (nom, prenom, email, cin, dateNaissance, role, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            String password = generateRandomPassword(10).toString();
            stmt.setString(7, password);
            stmt.executeUpdate();
            String emailBody = "Hello " + obj.getNom() + "." + " " + " </br> " + " " + " Account created succeffuly."
                    + " " + " </br> " + " " + "Email : " + obj.getEmail() + " " + " </br> " + " " +
                    " " + " </br> " + " " + "Paswword : " + nonHashedPassword + " " + " </br> " + " ";
            String subject = "Welcome email";
            emailService.sendEmail(obj.getEmail(), emailBody, subject);
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

    public Boolean updatePassword(String code, String newPassword) throws SQLException {

        String sql = "update user set password = ? where id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, passwordEncoder.encode(newPassword));
        stmt.setInt(2, findUserByCode(code).getId());
        stmt.executeUpdate();
        return true;
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


    public User findUserByEmail(String email) throws SQLException {
        String sql = "select * from user where email = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        User user = new User();
        while (rs.next()) {
            user.setId(rs.getInt("id"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setEmail(rs.getString("email"));
            user.setCin(rs.getInt("cin"));
            user.setDateNaissance(rs.getDate("dateNaissance"));
            user.setRole(Role.valueOf(rs.getObject("role").toString()));
        }
        return user;
    }

    public User findUserByCode(String code) throws SQLException {
        String sql = "SELECT * FROM code WHERE code = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("idUser"));
            return user;
        }
        return null; // No user found
    }


    public String generateCode(String email) throws SQLException {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            code.append(chars.charAt(random.nextInt(chars.length())));
        }

        String sql = "INSERT INTO code (code , idUser) VALUES (?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, code.toString());
            stmt.setInt(2,findUserByEmail(email).getId());
            stmt.executeUpdate();
            return code.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }


    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}