package app;

import java.util.ArrayList;

import data_analyse.NLPProcessor;
import data_analyse.ResultsDataHandler;
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

		//3.Creatin de NLP model and testing
		NLPProcessor nlpProcessor = new NLPProcessor();
		ResultsDataHandler resultsDataHandler = new ResultsDataHandler();

		String userQuery = "Je cherche des offres à Casablanca";
		String[] tokens = nlpProcessor.tokenizeQuery(userQuery);
		String intent = nlpProcessor.identifyIntent(tokens);

		if ("location".equals(intent)) {
			System.out.println("Location NLP response");
		} else if ("secteur".equals(intent)) {
			System.out.println("Secteur NLP response");
		} else {
			System.out.println("Désolé, je n'ai pas compris votre question.");
		}
		//partie d interface graphiqur vas etre utiliser par imad pour affichage des graphe
		//resultsDataHandler.displayChart();


	}
}