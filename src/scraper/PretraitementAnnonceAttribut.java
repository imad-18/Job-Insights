package scraper;

import db.JobDAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static db.JobDAO.loadKeywordsFromJson;

public class PretraitementAnnonceAttribut {

    //M_job
        //Date de publication
    public static String convertToDate(String input) {
        Calendar calendar = Calendar.getInstance();

        // Gérer les différents cas d'entrée
        if (input.matches("(\\d+) heures avant")) {
            int hoursAgo = Integer.parseInt(input.replaceAll("(\\d+) heures avant", "$1"));
            calendar.add(Calendar.HOUR_OF_DAY, -hoursAgo);
        } else if (input.matches("(\\d+) jour[s]? avant")) {
            int daysAgo = Integer.parseInt(input.replaceAll("(\\d+) jour[s]? avant", "$1"));
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        } else if (input.matches("(\\d+) semaine[s]? avant")) {
            int weeksAgo = Integer.parseInt(input.replaceAll("(\\d+) semaine[s]? avant", "$1"));
            calendar.add(Calendar.WEEK_OF_YEAR, -weeksAgo);
        } else if (input.matches("(\\d+) mois avant")) {
            int monthsAgo = Integer.parseInt(input.replaceAll("(\\d+) mois avant", "$1"));
            calendar.add(Calendar.MONTH, -monthsAgo);
        } else {
            return "Date invalide"; // Si aucun format ne correspond
        }

        // Formater la date dans le format "JJ/MM/YYYY"
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static String convertExperienceKeywords(String input) {
        // Expression régulière pour trouver les groupes de mots-clés et les plages d'années
        String regex = "([\\w\\séèàêû']+(?: / [\\w\\séèàêû']+)*)\\s*\\(de\\s*(\\d+)\\s*à\\s*(\\d+)\\s*ans\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // Stocker le résultat final
        List<String> results = new ArrayList<>();

        while (matcher.find()) {
            String keywordsGroup = matcher.group(1).trim(); // Extrait les mots-clés combinés
            String startYear = matcher.group(2);           // Début de la plage
            String endYear = matcher.group(3);             // Fin de la plage

            // Séparer les mots-clés par " / " et ne prendre que le dernier mot-clé
            String[] keywords = keywordsGroup.split(" / ");
            String keyword = keywords[keywords.length - 1].trim();

            // Ajouter le mot-clé avec la plage d'années formatée
            results.add(keyword + " | " + startYear + "-" + endYear);
        }

        // Joindre les résultats par une virgule
        return String.join(", ", results);
    }



    public static String standardizeEtudeLevelInput(String input) {
        // Remplacer les cas spécifiques
        input = input.trim()
                .replace("NIV BAC ET MOINS", "BAC-")
                .replace("BAC+5 et plus", "BAC+5+");

        // Diviser les niveaux par `/`, tout en conservant le bon format
        String[] levels = input.split("\\s*/\\s*");
        List<String> standardizedLevels = new ArrayList<>();

        for (String level : levels) {
            level = level.trim(); // Supprimer les espaces
            if (!standardizedLevels.contains(level)) { // Éviter les doublons
                standardizedLevels.add(level);
            }
        }

        // Joindre les résultats par `/`
        return String.join(" / ", standardizedLevels);
    }


    private static final HashMap<String, String> villeToRegionMap = new HashMap<>();

    static {
        // Ajouter les correspondances ville -> région
        villeToRegionMap.put("Casablanca", "Casablanca-Settat");
        villeToRegionMap.put("Mohammedia", "Casablanca-Settat");
        villeToRegionMap.put("El Jadida", "Casablanca-Settat");
        villeToRegionMap.put("Settat", "Casablanca-Settat");
        villeToRegionMap.put("Berrechid", "Casablanca-Settat");

        villeToRegionMap.put("Rabat", "Rabat-Salé-Kénitra");
        villeToRegionMap.put("Salé", "Rabat-Salé-Kénitra");
        villeToRegionMap.put("Kenitra", "Rabat-Salé-Kénitra");
        villeToRegionMap.put("Temara", "Rabat-Salé-Kénitra");
        villeToRegionMap.put("Sidi Slimane", "Rabat-Salé-Kénitra");
        villeToRegionMap.put("Sidi Kacem", "Rabat-Salé-Kénitra");

        villeToRegionMap.put("Fès", "Fès-Meknès");
        villeToRegionMap.put("Meknès", "Fès-Meknès");
        villeToRegionMap.put("Ifrane", "Fès-Meknès");
        villeToRegionMap.put("El Hajeb", "Fès-Meknès");
        villeToRegionMap.put("Taza", "Fès-Meknès");
        villeToRegionMap.put("Sefrou", "Fès-Meknès");
        villeToRegionMap.put("Boulemane", "Fès-Meknès");

        villeToRegionMap.put("Marrakech", "Marrakech-Safi");
        villeToRegionMap.put("Safi", "Marrakech-Safi");
        villeToRegionMap.put("Essaouira", "Marrakech-Safi");
        villeToRegionMap.put("Chichaoua", "Marrakech-Safi");
        villeToRegionMap.put("Youssoufia", "Marrakech-Safi");
        villeToRegionMap.put("Al Haouz", "Marrakech-Safi");

        villeToRegionMap.put("Tanger", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Tétouan", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Al Hoceïma", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Larache", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Chefchaouen", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Asilah", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("Fnideq", "Tanger-Tétouan-Al Hoceïma");
        villeToRegionMap.put("M'diq", "Tanger-Tétouan-Al Hoceïma");

        villeToRegionMap.put("Agadir", "Souss-Massa");
        villeToRegionMap.put("Inezgane", "Souss-Massa");
        villeToRegionMap.put("Ait Melloul", "Souss-Massa");
        villeToRegionMap.put("Tiznit", "Souss-Massa");
        villeToRegionMap.put("Taroudant", "Souss-Massa");
        villeToRegionMap.put("Chtouka Ait Baha", "Souss-Massa");

        villeToRegionMap.put("Oujda", "Oriental");
        villeToRegionMap.put("Nador", "Oriental");
        villeToRegionMap.put("Berkane", "Oriental");
        villeToRegionMap.put("Driouch", "Oriental");
        villeToRegionMap.put("Jerada", "Oriental");
        villeToRegionMap.put("Taourirt", "Oriental");
        villeToRegionMap.put("Guercif", "Oriental");

        villeToRegionMap.put("Béni Mellal", "Béni Mellal-Khénifra");
        villeToRegionMap.put("Khénifra", "Béni Mellal-Khénifra");
        villeToRegionMap.put("Khouribga", "Béni Mellal-Khénifra");
        villeToRegionMap.put("Azilal", "Béni Mellal-Khénifra");
        villeToRegionMap.put("Fquih Ben Salah", "Béni Mellal-Khénifra");

        villeToRegionMap.put("Errachidia", "Drâa-Tafilalet");
        villeToRegionMap.put("Ouarzazate", "Drâa-Tafilalet");
        villeToRegionMap.put("Zagora", "Drâa-Tafilalet");
        villeToRegionMap.put("Tinghir", "Drâa-Tafilalet");
        villeToRegionMap.put("Midelt", "Drâa-Tafilalet");

        villeToRegionMap.put("Laâyoune", "Laâyoune-Sakia El Hamra");
        villeToRegionMap.put("Boujdour", "Laâyoune-Sakia El Hamra");
        villeToRegionMap.put("Es-Semara", "Laâyoune-Sakia El Hamra");

        villeToRegionMap.put("Guelmim", "Guelmim-Oued Noun");
        villeToRegionMap.put("Tan-Tan", "Guelmim-Oued Noun");
        villeToRegionMap.put("Sidi Ifni", "Guelmim-Oued Noun");
        villeToRegionMap.put("Assa-Zag", "Guelmim-Oued Noun");

        villeToRegionMap.put("Dakhla", "Dakhla-Oued Ed-Dahab");
        villeToRegionMap.put("Aousserd", "Dakhla-Oued Ed-Dahab");
    }

    public static String getRegionsFromVilles(String villes) {
        if (villes == null || villes.trim().isEmpty()) {
            return ""; // Gérer les cas où la chaîne est null ou vide
        }

        // Diviser les villes par le séparateur "/"
        String[] villeList = villes.split("\\s*/\\s*");
        Set<String> regions = new LinkedHashSet<>(); // Utiliser un Set pour éviter les doublons et conserver l'ordre

        for (String ville : villeList) {
            // Normaliser la ville (ignorer les majuscules/minuscules et espaces inutiles)
            String normalizedVille = ville.trim().toLowerCase();

            // Rechercher la région correspondante
            boolean found = false;
            for (Map.Entry<String, String> entry : villeToRegionMap.entrySet()) {
                if (entry.getKey().toLowerCase().equals(normalizedVille)) {
                    regions.add(entry.getValue());
                    found = true;
                    break;
                }
            }

            // Si aucune région trouvée pour une ville, ajouter "Région inconnue"

        }

        // Joindre les régions trouvées par une virgule
        return String.join(", ", regions);
    }



    public static String transformerSalaire(String input) {
        // Vérifier si le texte est "A négocier"
        if (input.equalsIgnoreCase("A négocier")) {
            return "-1";
        }

        // Expression régulière pour extraire les chiffres, y compris avec des espaces
        String regex = "de\\s*([\\d\\s]+)\\s*dh\\s*à\\s*([\\d\\s]+)\\s*dh";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        // Si la chaîne correspond au format "de 4 000 dh à 6 000 dh"
        if (matcher.find()) {
            // Retirer les espaces des nombres
            String salaireMin = matcher.group(1).replaceAll("\\s", "");
            String salaireMax = matcher.group(2).replaceAll("\\s", "");
            return salaireMin + "-" + salaireMax;
        }

        // Retourner la chaîne d'origine si aucune correspondance
        return input;
    }





    public static String extracting_fonction(String title,String secteur ) {

        String fonction = secteur;

        Map<String, String> keywords = JobDAO.loadKeywordsFromJson("src/db/jobName_data.json");
        // Fusionner les deux paragraphes en un seul texte

        // Convertir le texte en tableau de mots
        String[] words = title.split("\\s+");

        // Parcourir chaque mot et vérifier s'il correspond à un mot-clé
        for (String word : words) {
            for (Map.Entry<String, String> entry : keywords.entrySet()) {
                Pattern pattern = Pattern.compile(entry.getValue());
                Matcher matcher = pattern.matcher(word);
                if (matcher.find()) {
                    fonction = entry.getKey();
                }
            }
        }
        return fonction;
    }




    // Fonction principale qui prend deux textes et les analyse
    public static Map<String, List<String>> parseTexts(String text1, String text2, String desc) {
        // Charger les mots-clés des fichiers JSON
        Map<String, String> softSkills = JobDAO.loadKeywordsFromJson("src/db/SoftSkills.json");
        Map<String, String> competencies = JobDAO.loadKeywordsFromJson("src/db/CompetenceRecom.json");
        Map<String, String> traits = JobDAO.loadKeywordsFromJson("src/db/TraitPersonn.json");
        Map<String, String> advantages = JobDAO.loadKeywordsFromJson("src/db/Avantage_Sociaux.json");

        // Sets pour chaque catégorie afin de garantir l'unicité
        Set<String> softSkillsSet = new HashSet<>();
        Set<String> competenciesSet = new HashSet<>();
        Set<String> traitsSet = new HashSet<>();
        Set<String> advantagesSet = new HashSet<>();

        // Combiner les trois textes pour les analyser ensemble
        String combinedText = text1 + " " + text2 + " " + desc;

        // Parcourir chaque catégorie et rechercher les mots-clés
        searchForKeywords(combinedText, softSkills, softSkillsSet);
        searchForKeywords(combinedText, competencies, competenciesSet);
        searchForKeywords(combinedText, traits, traitsSet);
        searchForKeywords(combinedText, advantages, advantagesSet);

        // Convertir les Sets en Listes et remplir le Map final avec les résultats
        Map<String, List<String>> results = new HashMap<>();
        results.put("Soft Skills", new ArrayList<>(softSkillsSet));
        results.put("Competencies", new ArrayList<>(competenciesSet));
        results.put("Personality Traits", new ArrayList<>(traitsSet));
        results.put("Social Benefits", new ArrayList<>(advantagesSet));

        return results;
    }

    // Méthode pour rechercher les mots-clés dans un texte en utilisant les expressions régulières
    private static void searchForKeywords(String text, Map<String, String> keywordsMap, Set<String> resultSet) {
        // Diviser le texte en mots pour traiter chaque mot séparément
        String[] words = text.split("\\s+"); // Divise le texte par les espaces, tabulations, etc.

        for (Map.Entry<String, String> entry : keywordsMap.entrySet()) {
            String keyword = entry.getKey();
            String regex = "\\b" + entry.getValue() + "\\b"; // Ajout de frontières de mots

            // Compiler l'expression régulière
            Pattern pattern = Pattern.compile(regex);

            for (String word : words) {
                Matcher matcher = pattern.matcher(word);

                // Si un mot correspondant est trouvé, on l'ajoute au Set
                if (matcher.find()) {
                    resultSet.add(keyword);

                    // Afficher le mot trouvé et le mot-clé correspondant
                    System.out.println("Mot trouvé : " + word + " | Mot-clé associé : " + keyword);
                }
            }
        }
    }

    }





