package Services;

import utils.MyDb;
import java.sql.*;
import java.time.LocalDate;

public class EvaluationService {
    private Connection connection;

    public EvaluationService() {
        connection = MyDb.getMydb().getConnection();
    }

    // 🟢 1️⃣ Ajouter ou Mettre à Jour une Évaluation
    public void ajouterOuMettreAJourEvaluation(int idResource, int idUser, double note) {
        try {
            // Vérifier si la ressource existe avant d'insérer une évaluation
            String checkQuery = "SELECT COUNT(*) FROM ressources WHERE idResource = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, idResource);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                System.out.println("⚠ ERREUR : La ressource avec ID " + idResource + " n'existe pas !");
                return; // Arrêter l'exécution
            }

            // Si la ressource existe, insérer la note
            String query = "INSERT INTO evaluation (idResource, idUser, note, dateEvaluation) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE note = VALUES(note), dateEvaluation = VALUES(dateEvaluation)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, idResource);
            stmt.setInt(2, idUser);
            stmt.setDouble(3, note);
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            stmt.executeUpdate();

            System.out.println("✅ Note enregistrée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 🟢 2️⃣ Calculer la Moyenne des Notes d’une Ressource
    public double calculerMoyenneNote(int idResource) {
        String query = "SELECT AVG(note) AS moyenne FROM evaluation WHERE idResource = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idResource);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("moyenne");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors du calcul de la moyenne des notes : " + e.getMessage());
        }
        return 0.0; // Retourne 0 si aucune note trouvée
    }

    // 🟢 3️⃣ Générer Automatiquement un Message de Satisfaction
    public String genererMessageConseil(int idResource) {
        double moyenne = calculerMoyenneNote(idResource);
        double satisfaction = (moyenne / 5) * 100;

        if (satisfaction > 80) {
            return "👍 Excellent travail ! Continuez à proposer ce type de ressources !";
        } else if (satisfaction >= 50) {
            return "🧐 Quelques ajustements pourraient améliorer ces ressources.";
        } else {
            return "⚠️ Besoin d’amélioration ! Analysez les retours des utilisateurs pour optimiser cette catégorie.";
        }
    }
}
