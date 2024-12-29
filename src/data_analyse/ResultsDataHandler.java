package data_analyse;

import db.Database;
import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;
import java.util.ArrayList;

public class ResultsDataHandler {

    //1st case

    public ArrayList<Annonce> Data(String Column, String Values) {
        Database b = new Database();
        ArrayList<Annonce> dataList = b.SelectedData(Column,Values);
        return dataList ;
    }

    public ArrayList<Pair<String, Integer>> Chart(String type, String key1) {
        Database b = new Database();
        ArrayList<Pair<String,Integer>> json = b.CountSelection(type,key1);
        int Rest = 100 - (int) (json.get(0).getRight()+json.get(1).getRight()+json.get(2).getRight()
                +json.get(3).getRight()+json.get(4).getRight());
        //add the others to the array of the chart
        Pair<String,Integer> Others = Pair.of("Others",Rest);
        json.add(Others);

        return json;
    }

    //2nd case

    public ArrayList<Annonce> Data2(String Column ,String Column1 , String Values) {
        Database b = new Database();
        ArrayList<Annonce> dataList = b.SelectedData2(Column,Column1,Values);
        return dataList ;
    }

    public ArrayList<Pair<String, Integer>> Chart2(String type, String type1 , String key1) {
        Database b = new Database();
        ArrayList<Pair<String,Integer>> json = b.CountSelection2(type,type1,key1);
        int Reste = 100 - (int) (json.get(0).getRight()+json.get(1).getRight()+json.get(2).getRight()
                +json.get(3).getRight()+json.get(4).getRight());
        float Resultat = 0;
        for (Pair<String,Integer> item : json){
            if(item.getLeft().contains(key1)){
                Resultat = item.getRight();
                break ;
            }
        }
        //add the others to the array of the chart
        Pair<String,Integer> Others = Pair.of("Others",Reste);
        json.add(Others);
        //reponse a envoye a l utilisateur
        if(type.equals("Secteur")){
            System.out.println("Il se voit que vous voulez savoir les jobs dont le " + type + " est "+Resultat+" %");
        }

        return json;
    }
}
