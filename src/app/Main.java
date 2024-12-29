package app;

import java.util.ArrayList;

import data_analyse.NLPProcessor;
import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;

//date format YYYY-MM-DD

public class Main {
	public static void main(String[] args) {
		String question = "quelles sont les competences necessaire dans la ville de Rabat" ;
		NLPProcessor nlp = new NLPProcessor();
		String[] tokens = question.split("\\s+");
		nlp.identifyIntent(tokens);
		Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> DataResponse = nlp.QuestionResponse();
		
	}
	//public static Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> FrontResponse(String req){

	//	return DataResponse;
	//}
}