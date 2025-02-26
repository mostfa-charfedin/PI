package TestMain;

import Models.programmebienetre;
import Services.programmebienetreService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        programmebienetreService service = new programmebienetreService();

        // Ajouter un programme
        Models.programmebienetre programme = new programmebienetre(1, "competition", "lucrative", "Session de yoga relaxante");
        try {
            service.create(programme);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Afficher tous les programmes
        try {
            List<Models.programmebienetre> programmes = service.getAll();
            programmes.forEach(p -> System.out.println(p.getTitre() + " - " + p.getDescription()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //update
        programme.setTitre("Yoga Avancé");
        try {
            service.update(programme);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Suppression
        try {
            service.delete(programme.getIdProgramme());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
