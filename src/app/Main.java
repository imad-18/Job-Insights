package app;

import java.util.ArrayList;

import data_analyse.NLPProcessor;
import db.Database;
import model.Annonce;

//date format YYYY-MM-DD

public class Main {
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