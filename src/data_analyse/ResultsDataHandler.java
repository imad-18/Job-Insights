package data_analyse;

import db.Database;
import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ResultsDataHandler {
    private JFreeChart chart;

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
    }

    public ArrayList<Annonce> Data(String type, String key1) {
        Database b = new Database();
        ArrayList<Annonce> dataList = b.SelectedData(type,key1);
        return dataList ;
    }
    public ArrayList<Annonce> Data3(String type, int a , int c) {
        Database b = new Database();
        ArrayList<Annonce> dataList = b.BetweenSelection(type,a,c);
        return dataList ;
    }
    public String Data2(String type, String key1,String type2) {
        Database b = new Database();
        String data = b.SelectedData2(type,key1,type2);
        return data ;
    }

    public void ResultsData(String type, String key1) {
        Database b = new Database();
        ArrayList<Pair<String,Integer>> json = b.CountSelection(type,key1);
        float TotalSize = (float) b.DBsize();
        float Reste = TotalSize - (float) (json.get(0).getRight()+json.get(1).getRight()+json.get(2).getRight()
                +json.get(3).getRight()+json.get(4).getRight());
        float Resultat = 0;
        for (Pair<String,Integer> item : json){
            if(item.getLeft().contains(key1)){
                Resultat = (float) item.getRight();
                break ;
            }
        }
        Resultat = (Resultat/TotalSize)*100 ;
        System.out.println("Pourcentage d'offres de travail à " + key1 + " est "+Resultat+" %");
    }

    public void ResultsData2(String type,String token) {
        Database b = new Database();
        ArrayList<Pair<String,Integer>> json = b.CountSelection2(type,token);
        float TotalSize = (float) b.DBsize();
        float Reste = TotalSize - (float) (json.get(0).getRight()+json.get(1).getRight()+json.get(2).getRight()
                +json.get(3).getRight()+json.get(4).getRight());
        float Resultat = 0;
        Resultat = (Resultat/TotalSize)*100 ;
        System.out.println("Pourcentage d'offres de travail est "+Resultat+" %");
    }

    public void displayChart() {
        JFrame frame = new JFrame("Aji tkhdm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));
        Image icon = Toolkit.getDefaultToolkit().getImage(
                "C:\\Users\\najib\\eclipse-workspace\\maven-demo\\ressources\\briefcase.png"
        );
        frame.setIconImage(icon);
        ChartPanel chartPanel = new ChartPanel(this.getChart());
        frame.add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }
}
