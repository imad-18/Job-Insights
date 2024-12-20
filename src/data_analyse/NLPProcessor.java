package data_analyse;

import java.util.ArrayList;
import java.util.Arrays;
import opennlp.tools.tokenize.SimpleTokenizer;


public class NLPProcessor {
	private SimpleTokenizer tokenizer;
	private Identifier Identifier;

	public NLPProcessor() {
		tokenizer = SimpleTokenizer.INSTANCE;
		this.Identifier = new Identifier(
				Arrays.asList("Agadir", "Casablanca", "El Jadida", "Fes", "Ifrane", "Kenitra", "Marrakech", "Meknes", "Nador", "Oujda", "Rabat", "Sale", "Tangier", "Tétouan", "Tiznit", "Guercif"),
				Arrays.asList("Agence pub", "Agroalimentaire", "Assurance", "Automobile", "Banque", "BTP", "Centre d'appel", "Chimie", "Communication", "Comptabilité", "Conseil", "Distribution", "Electro-mécanique", "Electronique", "Enseignement", "Energie", "Etudes", "Extraction", "Formation", "Gaz", "Génie Civil", "Hôtellerie", "Indifférent", "Informatique", "Marketing Direct", "Offshoring", "Papier", "Pharmacie", "Telecom", "Spatial"),
				Arrays.asList("Moins de 1 an","De 1 à 3 ans ", "De 3 à 5 ans ", "De 5 à 10 ans ","De 10 à 20 ans","Débuteur", "Plus de 20 ans"),
				Arrays.asList("Bac +1", "Bac +2","Bac +3", "Bac +4","Bac +5 et plus"),
				Arrays.asList("emploi.ma","rekrute","m-job.ma"),
				Arrays.asList("Français","Anglais","Espagol","Allemand","Italien","Portugais","Chinois","Japonais","Arabe","Russe")
		);
	}

	//Tokenization
	public String[] tokenizeQuery(String query) {
		return tokenizer.tokenize(query);
	}

	//Intent Recognition
	public String identifyIntent(String[] tokens) {
		ResultsDataHandler data = new ResultsDataHandler();
		//Cities identifier
		String city = Identifier.identifyCity(tokens);
		if (!city.equals("none")) {
			//data.ResultsData("location", city);
			return "location";
		}

		//secteur identifier
		String sector = Identifier.identifySector(tokens);
		if (!sector.equals("none")) {
			//data.ResultsData("Secteur", sector);
			return "secteur";
		}

		//experience identifier
		String experience = Identifier.identifyExperience(tokens);
		if (!experience.equals("none")) {
			//data.ResultsData("Experience", experience);
			return "experience";
		}

		//etudeLevel identifier
		String etudeLevel = Identifier.identifyEtudeLevel(tokens);
		if (!etudeLevel.equals("none")) {
			//data.ResultsData("EtudeLevel", etudeLevel);
			return "EtudeLevel";
		}

		//SiteName identifier
		String SiteName = Identifier.identifySiteName(tokens);
		if (!SiteName.equals("none")) {
			//data.ResultsData("SiteName", SiteName);
			return "SiteName";
		}

		//langue identifier
		String langue = Identifier.identifyLangue(tokens);
		if (!langue.equals("none")) {
			//data.ResultsData("Langue", SiteName);
			return "langue";
		}

		//langue identifier
		salaire sal = Identifier.identifySalaire(tokens);
		if (!(sal == null)) {
			//data.ResultsData("Salaire", sal.);
			return "langue";
		}

		return "Je n'ai pas pu répondre à votre question. Vous pouvez répéter, s'il vous plaît.";
	}

}
