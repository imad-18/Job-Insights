package db;

import gvjava.org.json.JSONException;
import gvjava.org.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JobDAO {

    public static Map<String, String> loadKeywordsFromJson(String filePath) {
        Map<String, String> keywords = new HashMap<>();
        try {
            FileReader reader = new FileReader(new File(filePath));
            int i;
            StringBuilder jsonContent = new StringBuilder();
            while ((i = reader.read()) != -1) {
                jsonContent.append((char) i);
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            // Parcourir les mots-clés et leurs expressions régulières
            Iterator<String> keys = jsonObject.keys();  // Utilisation de keys() pour obtenir un Iterator
            while (keys.hasNext()) {
                String key = keys.next();
                keywords.put(key, jsonObject.getString(key));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return keywords;
    }


}
