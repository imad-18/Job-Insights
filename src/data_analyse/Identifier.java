package data_analyse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier {
    private List<String> cities;
    private List<String> sectors;
    private List<String> experience;
    private List<String> etudeLevel;
    private List<String> siteName;
    private List<String> langue;

    public Identifier(List<String> cities, List<String> sectors , List<String> experience , List<String> etudeLevel
            , List<String> siteName, List<String> langue) {
        this.cities = cities;
        this.sectors = sectors;
        this.experience = experience;
        this.etudeLevel = etudeLevel;
        this.siteName = siteName;
        this.langue = langue;
    }

    public String identifyCity(String[] tokens) {
        return contains(tokens, cities);
    }

    public String identifySector(String[] tokens) {
        return contains(tokens, sectors);
    }

    public String identifyExperience(String[] tokens) {
        return contains(tokens, experience);
    }

    public String identifyEtudeLevel(String[] tokens) {
        return contains(tokens, etudeLevel);
    }

    public String identifySiteName(String[] tokens) {
        return contains(tokens, siteName);
    }

    public String identifyLangue(String[] tokens) {
        return contains(tokens, langue);
    }

    public salaire identifySalaire(String[] tokens) {
        return containsSalaire(tokens);
    }

    private String contains(String[] tokens, List<String> list) {
        for (String token : tokens) {
            for (String item : list) {
                if (token.contains(item)) {
                    return item;
                }
            }
        }
        return "none";
    }

    private salaire containsSalaire(String[] tokens) {
        // Regular expression pour le montant des salaire
        String regex = "(\\d+(?:[\\.,]?\\d{1,2})?\\s?(€|USD|£|\\$)?|[\\$€£]?\\d+(?:[\\.,]?\\d{1,2})?)";
        int salaire1 = 0 , salaire2 = 0;
        int i = 0 ;
        for (String token : tokens) {
            if (token.contains("entre") && token.contains("et") && token.contains("salaire")) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(token);
                while (matcher.find()) {
                    if (i == 0) {
                        salaire1 = Integer.parseInt(matcher.group(1).replaceAll("[^0-9]", ""));
                        i++;
                    } else if (i == 1) {
                        salaire2 = Integer.parseInt(matcher.group(1).replaceAll("[^0-9]", ""));
                    } else {
                        break;
                    }
                }
                salaire s = new salaire("Salaire", salaire1, salaire2);
                return s;
            }
        }
        return null;
    }
}

