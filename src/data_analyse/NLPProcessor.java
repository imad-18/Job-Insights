package data_analyse;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

import db.Database;
import model.Annonce;
import opennlp.tools.tokenize.SimpleTokenizer;


public class NLPProcessor {
	private SimpleTokenizer tokenizer ;
	private static final List<String> cities = Arrays.asList(
			"Agadir", "Casablanca", "El Jadida", "Fes", "Ifrane", "Kenitra", "Marrakech", "Meknes", "Nador", 
    		"Oujda", "Rabat", "Sale", "Tangier", "Tétouan", "Tiznit", "Guercif"
	);
	private static final List<String> secteur = Arrays.asList("Agence pub", "Agroalimentaire", "Assurance", "Automobile", "Banque", "BTP", "Centre d'appel", "Chimie", 
    		"Communication", "Comptabilité", "Conseil", "Distribution", "Electro-mécanique", "Electronique", "Enseignement", "Energie", "Etudes", "Extraction", "Formation", 
    		"Gaz", "Génie Civil", "Hôtellerie", "Indifférent", "Informatique", "Marketing Direct", "Offshoring", "Papier", "Pharmacie", "Telecom", "Spatial");
	private JFreeChart Chart ;
	
	public JFreeChart getChart() {
		return Chart;
	}

	public void setChart(JFreeChart Chart) {
		this.Chart = Chart;
	}

	public NLPProcessor() {
		tokenizer = SimpleTokenizer.INSTANCE ;
	}
	
	public String[] tokenizeQuery (String Query) {
		return tokenizer.tokenize(Query);
	}
	
	private Pair2 containsCity(String[] tokens, List<String> list) {
        for (String token : tokens) {	
        	for(String item : list) {
        		if (token.contains(item)) {
        			Pair2 pair = new Pair2(true, item);
                    return pair;
                }
        	}         
        }
        Pair2 pair = new Pair2(true, "none");
        return pair;
    }
	
	private boolean contains(String[] tokens, String word) {
        for (String token : tokens) {
            if (token.contains(word)) {
                return true;
            }
        }
        return false;
    }
	
	public String identifyIntent(String[] tokens) {
        if ((contains(tokens, "à") || contains(tokens, "a"))  && containsCity(tokens, cities).getKey()) {
        	String key1 = containsCity(tokens, cities).getValue();
        	ResultsData("location",key1);
            return "location";
        }else if (contains(tokens, "secteur") && containsCity(tokens, secteur).getKey()) {
        	String key2 = containsCity(tokens, secteur).getValue();
        	ResultsData("Secteur",key2);
        	return "secteur";
        }
        return "Je n'ai pas pu repondre a votre question . vous pouvez repeter svp .";
    }
	
	public void ResultsData(String type , String key1) {
		Database b = new Database();
		ArrayList<Annonce> AllData = b.selectData();
		ArrayList<Annonce> data = b.SelectedData(type, key1);
		
		float aa = (float) data.size() ;
		float bb = (float) AllData.size();
		
		int results = (int)((aa/bb)*100);
		System.out.println("Pourcentage d'offre de travail a "+key1+" est "+results+"%");
		DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue(key1, results);
        dataset.setValue("Autre", 100 - results);
        JFreeChart chart = ChartFactory.createPieChart(type+" Chart", dataset, true, true, false);
		this.setChart(chart);
	}
	
	public void test(String userQuery) {
		//tokenizing and identifying the Intent
		String[] tokens = tokenizeQuery(userQuery);
		String Intent = identifyIntent(tokens);
		if("location".equals(Intent)) {
			System.out.println("Location NLP response");	
		} else if("secteur".equals(Intent)){
			System.out.println("secteur NLP response ");
		}else {
			System.out.println("Sorry, I couldn't understand your question.");
		}	
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
	     return;
	}
	
}
