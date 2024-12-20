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

		//secteur identifier
		String sector = Identifier.identifySector(tokens);
		if (!sector.equals("none")) {
			//data.Data("Secteur", sector);
			//data.ResultsData("Secteur", sector);
			return "secteur";
		}

		//experience identifier
		String secteur = Identifier.identifyExperience(tokens);
		if (!secteur.equals("none")) {
			//data.Data2("Secteur",secteur,"Experience");
			//data.ResultsData2("Experience",secteur);
			return "experience";
		}

		//etudeLevel identifier
		String etudeLevel = Identifier.identifyEtudeLevel(tokens);
		if (!etudeLevel.equals("none")) {
			//data.Data2("Secteur",secteur,"EtudeLevel");
			//data.ResultsData2("EtudeLevel",secteur);
			return "EtudeLevel";
		}

		//SiteName identifier
		String SiteName = Identifier.identifySiteName(tokens);
		if (!SiteName.equals("none")) {
			//data.Data("SiteName", SiteName);
			//data.ResultsData("SiteName", SiteName);
			return "SiteName";
		}

		//Cities identifier
		String city = Identifier.identifyCity(tokens);
		if (!city.equals("none")) {
			//data.Data("City",city)
			//data.ResultsData("City", city);
			return "location";
		}

		//salaire identifier
		salaire sal = Identifier.identifySalaire(tokens);
		if (!(sal  == null)) {
			//data.Data3("Salaire", sal.getSalaire1() , sal.getSalaire2());
			//data.ResultsData("Salaire", sal.getSalaire1());
			return "secteur";
		}

		//CompetenceRequise identifier
		String competence = Identifier.identifyEtudeLevel(tokens);
		if (!etudeLevel.equals("none")) {
			//data.Data2("Secteur",secteur,"CompetencesRequises");
			//data.ResultsData2("CompetencesRequises",secteur);
			return "EtudeLevel";
		}



		return "Je n'ai pas pu répondre à votre question. Vous pouvez répéter, s'il vous plaît.";
	}

}
