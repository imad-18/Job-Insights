package data_analyse;

import java.util.Arrays;
import opennlp.tools.tokenize.SimpleTokenizer;


public class NLPProcessor {
	private SimpleTokenizer tokenizer;
	private Identifier Identifier;

	public NLPProcessor() {
		tokenizer = SimpleTokenizer.INSTANCE;
		this.Identifier = new Identifier(
				Arrays.asList("Agadir", "Casablanca", "El Jadida", "Fes", "Ifrane", "Kenitra", "Marrakech", "Meknes", "Nador", "Oujda", "Rabat", "Sale", "Tangier", "Tétouan", "Tiznit", "Guercif"),
				Arrays.asList("Agence pub", "Agroalimentaire", "Assurance", "Automobile", "Banque", "BTP", "Centre d'appel", "Chimie", "Communication", "Comptabilité", "Conseil", "Distribution", "Electro-mécanique", "Electronique", "Enseignement", "Energie", "Etudes", "Extraction", "Formation", "Gaz", "Génie Civil", "Hôtellerie", "Indifférent", "Informatique", "Marketing Direct", "Offshoring", "Papier", "Pharmacie", "Telecom", "Spatial")
		);
	}

	public String[] tokenizeQuery(String query) {
		return tokenizer.tokenize(query);
	}

	public String identifyIntent(String[] tokens) {
		ResultsDataHandler data = new ResultsDataHandler();
		String city = Identifier.identifyCity(tokens);
		if (!city.equals("none")) {
			data.ResultsData("location", city);
			return "location";
		}

		String sector = Identifier.identifySector(tokens);
		if (!sector.equals("none")) {
			data.ResultsData("secteur", sector);
			return "secteur";
		}

		return "Je n'ai pas pu répondre à votre question. Vous pouvez répéter, s'il vous plaît.";
	}

}
