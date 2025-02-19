package org.example;


import Model.Ressource;
import Model.Favoris;
import Services.RessourceService;
import Services.FavorisService;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialisation des services
            RessourceService ressourceService = new RessourceService();
            FavorisService favorisService = new FavorisService();

            // Test Ressources
            System.out.println("=== Test Ressources ===");

            // Test CREATE
            Ressource ressource = new Ressource();
            // Remplir les champs de ressource selon votre structure
            ressourceService.create(ressource);
            System.out.println("Ressource créée");

            // Test READ
            System.out.println("\nListe des ressources :");
            for (Ressource r : ressourceService.getAll()) {
                System.out.println(r); // Assurez-vous d'avoir une méthode toString()
            }

            // Test Favoris
            System.out.println("\n=== Test Favoris ===");

            // Test CREATE Favoris
            Favoris favori = new Favoris();
            // Remplir les champs de favori selon votre structure
            favorisService.create(favori);
            System.out.println("Favori créé");

            // Test READ Favoris
            System.out.println("\nListe des favoris :");
            for (Favoris f : favorisService.getAll()) {
                System.out.println(f); // Assurez-vous d'avoir une méthode toString()
            }

        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}