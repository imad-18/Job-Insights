package data_analyse;

import db.Database;
import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ResultsDataHandler {
    private JFreeChart chart;

    public JFreeChart getChart() {
        return chart;
    }

    public void setChart(JFreeChart chart) {
        this.chart = chart;
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

    public void ResultsDataBetween(String type, String key1,int a , int c) {
        Database b = new Database();
        ArrayList<Annonce> json = b.BetweenSelection(type,key1,a,c);
        float TotalSize = (float) b.DBsize();

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
