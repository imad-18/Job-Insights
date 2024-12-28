package data_analyse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Identifier {
    public List<String> cities;
    public List<String> sectors;
    public List<String> etudeLevel;
    public List<String> siteName;
    public List<String> langue;
    public List<String> Plus ;
    public List<String> Identifiers ;

    public Identifier(List<String> cities, List<String> sectors , List<String> etudeLevel
            , List<String> siteName, List<String> langue, List<String> Plus,List<String> Identifiers) {
        this.cities = cities;
        this.sectors = sectors;
        this.etudeLevel = etudeLevel;
        this.siteName = siteName;
        this.langue = langue;
        this.Plus = Plus;
        this.Identifiers = Identifiers;
    }

    //columns identifier
    public String identifyIdentifiers(String[] tokens){
        return contains(tokens,Identifiers);
    }

    //columns values identifiers
    public String identifyCity(String[] tokens) {
        return contains(tokens, cities);
    }

    public String identifySector(String[] tokens) {
        return contains(tokens, sectors);
    }

    public String identifyExperience(String[] tokens) {
        return contains(tokens,cities);
    }

    public String identifyEtudeLevel(String[] tokens) {
        return contains(tokens,etudeLevel);
    }

    public String identifyCompetence(String[] tokens) {
        return contains(tokens,cities);
    }

    public String identifySiteName(String[] tokens) {
        return contains(tokens, siteName);
    }

    public String identifyLangue(String[] tokens) {
        return contains(tokens, langue);
    }

    public String identifySalaire(String[] tokens) {
        return contains(tokens, Arrays.asList("salaire","Salaire"));
    }

    //pour les question de plus (les plus , les meilleurs , demandee , etc)
    public Boolean identifyPlusSecteur(String[] tokens) {
        return containsBoolean(tokens,Plus);
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

    private Boolean containsBoolean(String[] tokens, List<String> list) {
        for (String token : tokens) {
            for (String item : list) {
                if (token.contains(item)) {
                    return true;
                }
            }
        }
        return false;
    }

}

