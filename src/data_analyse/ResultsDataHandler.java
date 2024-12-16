package data_analyse;

import db.Database;
import model.Annonce;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

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

    public void ResultsData(String type, String key1) {
        Database b = new Database();
        ArrayList<Annonce> allData = b.selectData();
        ArrayList<Annonce> data = b.SelectedData(type, key1);

        float aa = (float) data.size();
        float bb = (float) allData.size();

        int results = (int) ((aa / bb) * 100);
        System.out.println("Pourcentage d'offres de travail à " + key1 + " est " + results + "%");

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(key1, results);
        dataset.setValue("Autre", 100 - results);
        JFreeChart chart = ChartFactory.createPieChart(type + " Chart", dataset, true, true, false);
        this.setChart(chart);
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
