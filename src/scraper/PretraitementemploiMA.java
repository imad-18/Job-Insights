package scraper;

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
        // Remplacer les intervalles par le format attendu
        String formatted = input
                .replace("Expérience entre ", "")       // Supprime le texte "Expérience entre"
                .replace(" ans et ", "-")              // Remplace " ans et " par "-"
                .replace(" ans", "")                   // Supprime " ans"
                .replace("Expérience > ", "")          // Remplace "Expérience > " par une chaîne vide
                .replace(" - ", " / ")                 // Remplace " - " par "/"
                .replace("> ", "");                    // Supprime les cas restants de "> "

        // Gérer les cas avec un seul intervalle
        if (!formatted.contains("/")) {
            formatted += "-+";
        }

        return formatted.trim(); // Supprime les espaces inutiles
    }



}
