package scraper;

import java.util.HashMap;

public class PretraitementemploiMA {


    public static String regulerDate(String startDate) {

        // Supprimer "Publiée le " pour extraire la date
        String datePart = startDate.replace("Publiée le ", "").trim();

        // Remplacer les points par des slashes

        return datePart.replace(".", "/");
    }

    public static String formatSecteur(String input) {
            // Supprimer les sauts de ligne et remplacer les séparateurs
            return input.replace("\n", " ")     // Remplace les sauts de ligne par des espaces
                    .replace("- ", "")      // Supprime les tirets suivis d'un espace
                    .replace(", ", " / ")   // Remplace les virgules et espaces par des slashes
                    .trim();                // Supprime les espaces inutiles au début et à la fin
        }


    public static String formatExperience(String input) {
        // Vérifier les mots-clés et appliquer les transformations correspondantes
        if (input.toLowerCase().contains("débutant")) {
            return "Débutant | 0-2";
        } else if (input.toLowerCase().contains("intermédiaire")) {
            return "Intermédiaire | 2-3";
        } else if (input.toLowerCase().contains("expert")) {
            return "Expert | 5+";
        }

        // Remplacer les intervalles par le format attendu
        String formatted = input
                .replace("Expérience entre ", "")       // Supprime le texte "Expérience entre"
                .replace(" ans et ", "-")              // Remplace " ans et " par "-"
                .replace(" ans", "")                   // Supprime " ans"
                .replace("Expérience > ", "")          // Remplace "Expérience > " par une chaîne vide
                .replace(" - ", " / ")                 // Remplace " - " par "/"
                .replace("> ", "");                    // Supprime les cas restants de "> "

        // Ajouter le symbole "+" uniquement pour les cas supérieurs à 5 ans
        if (input.contains("> 5") || input.toLowerCase().contains("expérience > 5")) {
            formatted += "+";
        }

        return formatted.trim(); // Supprime les espaces inutiles
    }


    public static String transformTextEtudeLevel(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        // Transformer tout en majuscules pour uniformité
        String modifiedText = input.toUpperCase();

        // Traiter "ET PLUS" pour ajouter un "+" directement au dernier niveau
        modifiedText = modifiedText.replace(" ET PLUS", "+");

        // Remplacer les tirets par des barres obliques
        modifiedText = modifiedText.replace(" - ", " / ");

        // Supprimer les espaces inutiles avant et après le texte
        modifiedText = modifiedText.trim();

        return modifiedText;
    }

    public static HashMap<String, String> parseLanguageLevel(String input) {
            // Créer une nouvelle HashMap pour stocker les résultats
            HashMap<String, String> languageLevelMap = new HashMap<>();

            if (input == null || input.isEmpty()) {
                // Si l'entrée est vide ou nulle, retourner une HashMap vide
                return languageLevelMap;
            }

            // Séparer la chaîne d'entrée en utilisant ">" comme séparateur
            String[] parts = input.split(">");
            if (parts.length == 2) {
                // Supprimer les espaces inutiles et ajouter les valeurs dans la HashMap
                languageLevelMap.put("langue", parts[0].trim());
                languageLevelMap.put("niveau", parts[1].trim());
            } else {
                // Si le format est incorrect, mettre des valeurs par défaut
                languageLevelMap.put("langue", "Inconnu");
                languageLevelMap.put("niveau", "Inconnu");
            }

            return languageLevelMap;
        }

    public static String formatRangeSalaire(String input) {
        // Supprimer les caractères non numériques et "DH"
        input = input.replaceAll("[^0-9- ]", "").trim();

        // Vérifier si l'input est vide ou mal formaté
        if (input.isEmpty() || !input.contains("-")) {
            return input;
        }

        // Séparer les deux valeurs par le tiret
        String[] parts = input.split("-");

        // Vérifier si nous avons bien deux parties
        if (parts.length != 2) {
            return input;
        }

        // Enlever les espaces et vérifier que les parties peuvent être converties en nombres
        String part1 = parts[0].trim().replaceAll(" ", "");
        String part2 = parts[1].trim().replaceAll(" ", "");

        if (!part1.matches("\\d+") || !part2.matches("\\d+")) {
            return input;
        }

        // Convertir en entiers
        int start = Integer.parseInt(part1);
        int end = Integer.parseInt(part2);

        // Retourner les valeurs sous le format désiré
        return start + "-" + end;
    }




}
