package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class configuration {
    // Liste élargie des mots à filtrer
    public static final List<String> MOTS_INTERDITS = new ArrayList<>(Arrays.asList(
            // Insultes courantes
            "idiot", "imbécile", "abruti", "crétin", "débile", "incapable", "minable", "raté", "médiocre",
             "badword1", "badword2", "badword3", "badword4", "badword5", "badword6",
            // Mots vulgaires et obscènes

            // Termes discriminatoires
            "raciste", "nazi",
            // Mots liés à la violence et au crime
            "criminel", "violeur", "pédophile", "meurtrier", "assassin", "tueur", "casseur", "terroriste"
                ));

    // Méthode pour ajouter un mot interdit dynamiquement
    public static void ajouterMotInterdit(String mot) {
        if (!MOTS_INTERDITS.contains(mot)) {
            MOTS_INTERDITS.add(mot);
        }
    }

    // Méthode pour supprimer un mot interdit
    public static void supprimerMotInterdit(String mot) {
        MOTS_INTERDITS.remove(mot);
    }

    // Méthode pour afficher la liste des mots interdits (utile pour le debug)
    public static void afficherMotsInterdits() {
        System.out.println("Liste des mots interdits : " + MOTS_INTERDITS);
    }
}