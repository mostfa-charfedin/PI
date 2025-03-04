package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class configuration {
    // Liste élargie des mots à filtrer
    public static final List<String> MOTS_INTERDITS = new ArrayList<>(Arrays.asList(
            // Insultes courantes
            "idiot", "imbécile", "abruti", "crétin", "débile", "incapable", "minable", "raté", "médiocre",
            "bouffon", "clown", "guignol", "tocard", "baltringue", "looser", "loser", "bon-à-rien", "nullard",
            "incompétent", "stupide", "pitoyable", "pathétique", "niais", "crétin", "tocard", "âne", "bête",
            "faiblard", "mou", "branleur", "paresseux", "gogol", "dégénéré", "simplet", "dodu", "gros", "maigrichon",

            // Mots vulgaires et obscènes
            "merde", "putain", "salope", "bordel", "connard", "connasse", "fils de pute", "enculé", "enfoiré",
            "gros con", "grosse conne", "bâtard", "trou du cul", "chiant", "casse-couilles", "emmerdeur", "emmerdeuse",
            "pute", "saloperie", "chiotte", "merdique", "crotte", "tarlouze", "tapette", "pédé", "sodomite", "pouffiasse",
            "catin", "pétasse", "crasseux", "dégueulasse", "clochard", "clodo", "morveux", "vermine", "fiente", "déchet",

            // Termes discriminatoires
            "raciste", "sexiste", "homophobe", "islamophobe", "xénophobe", "antisémite", "nazi", "suprémaciste",
            "misogyne", "extrémiste", "facho", "sale arabe", "sale juif", "sale noir", "singe", "porc", "sale blanc",
            "bougnoule", "rouquin", "noiraud", "raton", "pédé", "lesbienne", "tchoin", "métèque", "gitan", "clanpin",
            "sauvage", "gros lard", "chintok", "youpin", "bouffeur de curé", "bouffeur de cochons", "indigène",
            "colonisateur", "dégénéré", "anormal", "chien", "différent", "attardé", "mongolien", "handicapé", "débile mental",

            // Mots liés à la violence et au crime
            "criminel", "violeur", "pédophile", "meurtrier", "assassin", "tueur", "casseur", "terroriste",
            "pirate", "esclavagiste", "psychopathe", "démoniaque", "bourreau", "profiteur", "charlatan", "escroc",
            "menteur", "manipulateur", "traître", "hypocrite", "arrogant", "prétentieux", "voleur", "bandit",
            "agresseur", "arnaqueur", "magouilleur", "dealer", "mafieux", "sataniste", "usurpateur", "tortionnaire",
            "dictateur", "corrompu", "profiteur", "tricheur", "exploiteur", "parasite", "menteur pathologique",
            "charognard", "marchand de sommeil", "harceleur", "salaud", "ordure", "saleté", "fumier",

            // Mots liés aux comportements toxiques
            "jaloux", "envieux", "fourbe", "mesquin", "radin", "pingre", "égoïste", "opportuniste", "hypocrite",
            "arrogant", "orgueilleux", "vaniteux", "méprisant", "condescendant", "autoritaire", "tyrannique",
            "insupportable", "narcissique", "pervers", "malveillant", "cynique", "calculateur", "dominant",
            "envahissant", "bruyant", "pot-de-colle", "commère", "harceleur", "menteur professionnel",
            "profiteur", "pleurnichard", "victimiste", "capricieux", "manipulateur", "jalousard", "odieux",
            "toxique", "envahisseur", "dragueur lourdingue", "infidèle", "poltron", "lâche", "vulgaire", "blasphémateur",

            // Mots à connotation sexuelle et grossière
            "baiser", "sucer", "branler", "gicler", "sodomie", "bite", "queue", "couilles", "chatte", "nichons",
            "seins", "tétons", "cul", "anus", "fellation", "gode", "vagin", "clito", "érection", "éjaculation",
            "pornographique", "hardcore", "masturbation", "pénétration", "cocu", "orgasme", "partouze", "putain de merde",
            "grosse salope", "plan cul", "boobs", "sperme", "juter", "téma", "coït", "défoncer", "fornication",
            "échangisme", "prostitution", "camionneuse", "salope de luxe", "escort", "plan à trois", "pornstar",
            "culotte", "string", "sextape", "strip-tease", "porno", "vidéo X", "cybersexe", "voyeur", "exhibitionniste",

            // Insultes sur l'intelligence et le physique
            "gros porc", "grosse truie", "vieux débris", "laid", "moches", "dégueu", "tronche", "gueule", "poilu",
            "boutonneux", "faiblard", "rachitique", "poids plume", "cassé", "bossu", "borgne", "camé", "toxicomane",
            "alcoolo", "ivrogne", "soûlard", "mal rasé", "gueule de bois", "vautour", "charogne", "cul-de-jatte",
            "boiteux", "tortue", "bulldog", "chimpanzé", "gorille", "rat d'égout", "crapaud", "ver de terre",
            "crocodile", "lézard", "grosse vache", "squelette ambulant", "balai", "croûton", "raclure", "ronchon",
            "papillon de nuit", "chauve-souris", "chien galeux", "sale bête", "monstre", "mutant", "extraterrestre"
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
