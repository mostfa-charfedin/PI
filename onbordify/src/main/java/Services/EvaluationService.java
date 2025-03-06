package Services;

import utils.MyDb;
import java.sql.*;
import java.time.LocalDate;

public class EvaluationService {
    private Connection connection;

    public EvaluationService() {
        connection = MyDb.getMydb().getConnection();
    }

    public void ajouterOuMettreAJourEvaluation(int idResource, int id, double note) {
        try {
            // Vérification CORRIGÉE (sans 's' à ressource)
            String checkQuery = "SELECT COUNT(*) FROM ressource WHERE idResource = ?";
            // ... (le reste reste inchangé)

            // Mise à jour de la moyenne dans la table ressource
            double moyenne = calculerMoyenneNote(idResource);
            String updateQuery = "UPDATE ressource SET noteAverage = ? WHERE idResource = ?";
            try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                updateStmt.setDouble(1, moyenne);
                updateStmt.setInt(2, idResource);
                updateStmt.executeUpdate();
                System.out.println("✅ noteAverage mis à jour !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 🟢 2️⃣ Calculer la Moyenne des Notes d’une Ressource
    public double calculerMoyenneNote(int idResource) {
        String query = "SELECT AVG(note) AS moyenne FROM evaluation WHERE idResource = ?"; // ⚠️ Erreur typo ici ?
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idResource);
            System.out.println("[DEBUG] Requête exécutée : " + stmt.toString()); // Affiche la requête complète
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double moyenne = rs.getDouble("moyenne");
                System.out.println("[DEBUG] Moyenne calculée : " + moyenne);
                return moyenne;
            } else {
                System.out.println("[DEBUG] Aucune note trouvée pour idResource=" + idResource);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage()); // Affiche le message d'erreur complet
        }
        return 0.0;
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
