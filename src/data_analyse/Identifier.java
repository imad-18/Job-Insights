package data_analyse;

import java.util.List;

public class Identifier {
    private List<String> cities;
    private List<String> sectors;

    public Identifier(List<String> cities, List<String> sectors) {
        this.cities = cities;
        this.sectors = sectors;
    }

    public String identifyCity(String[] tokens) {
        return contains(tokens, cities);
    }

    public String identifySector(String[] tokens) {
        return contains(tokens, sectors);
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
}

