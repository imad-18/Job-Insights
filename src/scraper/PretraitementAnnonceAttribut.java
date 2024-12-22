package scraper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PretraitementAnnonceAttribut {

    //M_job
        //Date de publication
        public static String convertToDate(String input) {
            // Extraire le nombre de jours à partir de la chaîne
            int daysAgo = extractDays(input);

            // Si le nombre de jours a été correctement extrait
            if (daysAgo != -1) {
                // Calculer la date actuelle moins le nombre de jours
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
                Date date = calendar.getTime();

                // Formater la date dans le format "JJ/MM/YYYY"
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                return sdf.format(date);
            }

            return "Date invalide"; // Si le format d'entrée est incorrect
        }

        public static int extractDays(String input) {
            // Vérifier si l'entrée contient "il y a X jours" et extraire X
            if (input.matches("(\\d+) jours avant Voir plus Postuler")) {
                // Utilisation d'une expression régulière pour extraire le nombre de jours
                String numberOfDays = input.replaceAll("(\\d+) jours avant Voir plus Postuler", "$1");
                return Integer.parseInt(numberOfDays);
            }
            // Si le format n'est pas celui attendu, retourner -1
            return -1;
        }


        //get fonction from description



}
