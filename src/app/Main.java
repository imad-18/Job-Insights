package app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import data_analyse.NLPProcessor;
import db.Database;
import model.Annonce;

//date format YYYY-MM-DD

public class Main {
	public static void main(String[] args) throws IOException {
		
		
		
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);


        server.createContext("/api/offres", new Controller());

        // Lancer le serveur
        server.setExecutor(null); // Utilise le pool de threads par défaut
        server.start();
        System.out.println("Serveur démarré sur le port 8080");
		
		//1. Scrapping methode
		
		Rekrute_scraper s = new Rekrute_scraper();
		//scraper le site rekrute
		s.ScraperRekrute();
		//scraper le site emploi.ma
		//s.ScraperEmploiMa();
		//afficher les annonces scrapper
		s.afficheAnnonce();
		
		//2.Insertion des annonces dans la base
		
		Database b = new Database();
		b.insertData(s.getListeAnnonce());
		
		//3.recuperation des annonces de la base
		ArrayList<Annonce> a = b.selectData();
		
		//4. Creation de app
		
		JFrame frame = new JFrame("Aji tkhdm");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));
        Image icon = Toolkit.getDefaultToolkit().getImage(
                "C:\\Users\\najib\\eclipse-workspace\\maven-demo\\ressources\\briefcase.png"
            );
        frame.setIconImage(icon);
	public static void main(String[] args) {	
			//1.Insertion des annonces dans la base
			Database b = new Database();
			//b.insertData(s.getListeAnnonce());
				
			//2.recuperation des annonces de la base
			ArrayList<Annonce> a = b.selectData();
				
			//3.Creatin de NLP model
			NLPProcessor nlp = new NLPProcessor();
			
			//test
			nlp.test("Quels sont les offres dispnibles a Casablanca ?");
        
	} 
}