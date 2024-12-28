package app;

import java.util.ArrayList;

import data_analyse.NLPProcessor;
import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;

//date format YYYY-MM-DD

public class Main {
	public static void main(String[] args) {
		String question = "quelles sont les offres dans le secteur d' Informatique" ;
		NLPProcessor nlp = new NLPProcessor();
		String[] tokens = nlp.tokenizeQuery(question);
		nlp.identifyIntent(tokens);
		//Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> DataResponse = nlp.QuestionResponse();
		//send DataResponse to front end for a response
	}
	//public static Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> FrontResponse(String req){

	//	return DataResponse;
	//}
}