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
    public List<String> experience;
    public List<String> competence;


    public Identifier(List<String> cities, List<String> sectors , List<String> etudeLevel
            , List<String> siteName, List<String> langue, List<String> Plus,List<String> Identifiers
            ,List<String> experience,List<String> competence) {
        this.cities = cities;
        this.sectors = sectors;
        this.etudeLevel = etudeLevel;
        this.siteName = siteName;
        this.langue = langue;
        this.Plus = Plus;
        this.Identifiers = Identifiers;
        this.experience = experience;
        this.competence = competence;
    }

    //columns identifier
    public String identifyIdentifiers2(String[] tokens){
        return contains2(tokens,Identifiers);
    }
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
        return contains(tokens,experience);
    }

    public String identifyEtudeLevel(String[] tokens) {
        return contains3(tokens,etudeLevel);
    }

    public String identifyCompetence(String[] tokens) {
        return contains(tokens,competence);
    }

    public String identifySiteName(String[] tokens) {
        return contains(tokens, siteName);
    }

    public String identifyLangue(String[] tokens) {
        return contains(tokens, langue);
    }

    public String identifySalaire(String[] tokens) {
        return salaire(tokens);
    }
    
    public String identifySalaire2(String[] tokens) {
        return salaire2(tokens);
    }

    //pour les question de plus (les plus , les meilleurs , demandee , etc)
    public Boolean identifyPlusSecteur(String[] tokens) {
        return containsBoolean(tokens,Plus);
    }
    
    private String salaire(String[] tokens) {
    	for (String token : tokens) {
            if (token.matches("-?\\d+")) {
                return token;
            }
        }
        return "none";
    };

    private String salaire2(String[] tokens) {
    	int i=0;
    	for (String token : tokens) {
            if (token.matches("-?\\d+")) {
            	if(i == 0) {
            		i++;
            	}else {
            		return token;
            	}  
            }
        }
        return "none";
    };
    
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
    
    private String contains2(String[] tokens, List<String> list) {
    	int i=0;
        for (String token : tokens) {
            for (String item : list) {
                if (token.contains(item)) {
                	if(i==0){
                		i++;
                	}else {
                		return item;
                	}
                }
            }
        }
        return "none";
    }
    
    private String contains3(String[] tokens, List<String> list) {
        for (String token : tokens) {
            for (String item : list) {
                if (token.equals(item)) {
                    return item;
                }
            }
        }
        return "none";
    }
    
    private String contains4(String[] tokens, List<String> list) {
    	int i=0;
        for (String token : tokens) {
            for (String item : list) {
                if (token.equals(item)) {
                	if(i==0){
                		i++;
                	}else {
                		return item;
                	}
                }
            }
        }
        return "none";
    }

    private Boolean containsBoolean(String[] tokens, List<String> list) {
        for (String token : tokens) {
            for (String item : list) {
                if (token.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

}

