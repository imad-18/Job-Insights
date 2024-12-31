package db;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import rmi_api.Annonce;


public class Database {

	private Connection connection;

	private String url = "jdbc:mysql://localhost:3306/job_insight";
	private String user = "root";
	private String password = "";

	public void insert(int value) {
		// Vérification de la validité de la valeur
		if (value < 1 || value > 5) {
			System.out.println("Erreur : la valeur doit être comprise entre 1 et 5.");
			return;
		}

		// Requête SQL pour insérer une valeur (sans spécifier l'ID auto-incrémenté)
		String query = "INSERT INTO star (value) VALUES (?)";

		try (Connection connection = DriverManager.getConnection(url, user, password);
			 PreparedStatement statement = connection.prepareStatement(query)) {

			// Insérer la valeur dans la requête
			statement.setInt(1, value);

			// Exécuter la requête
			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Valeur insérée avec succès : " + value);
			} else {
				System.out.println("Insertion échouée pour la valeur : " + value);
			}

		} catch (SQLException e) {
			// Afficher une erreur plus détaillée
			System.err.println("Erreur lors de l'insertion de la valeur : " + value);
			e.printStackTrace();
		}
	}


	public List<Integer> getAll() {
		List<Integer> values = new ArrayList<>();
		// Requête SQL pour récupérer toutes les valeurs
		String query = "SELECT value FROM star"; // Assurez-vous que la colonne `value` existe

		try (Connection connection = DriverManager.getConnection(url, user, password);
			 PreparedStatement statement = connection.prepareStatement(query);
			 ResultSet resultSet = statement.executeQuery()) {

			// Parcourir les résultats et ajouter les valeurs à la liste
			while (resultSet.next()) {
				values.add(resultSet.getInt("value"));
			}

		} catch (SQLException e) {
			System.err.println("Erreur lors de la récupération des valeurs.");
			e.printStackTrace();
		}
		return values;
	}


	// Constructor to initialize the database connection
	public Database(String dbUrl, String username, String password) throws SQLException {
		try {
			connection = DriverManager.getConnection(dbUrl, username, password);
		} catch (SQLException e) {
			throw new SQLException("Failed to connect to the database: " + e.getMessage());
		}
	}

	// Method to insert an Annonce object into the database
	public void insertAnnonce(Annonce annonce) throws SQLException {
		String query = "INSERT INTO annonce (title, description, startDate, endDate, postsNum, secteur, fonction, " +
				"experience, etudeLevel, contratDetails, url, siteName, adresseEntreprise, siteWebEntreprise, " +
				"nomEntreprise, descriptionEntreprise, region, city, industry, traitsPersonnalite, competencesRequises, " +
				"softSkills, competencesRecommandees, langue, niveauLangue, salaire, avantagesSociaux, teletravail) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, annonce.getTitle());
			stmt.setString(2, annonce.getDescription());
			stmt.setString(3, annonce.getStartDate());
			stmt.setString(4, annonce.getEndDate());
			stmt.setInt(5, annonce.getPostsNum());
			stmt.setString(6, annonce.getSecteur());
			stmt.setString(7, annonce.getFonction());
			stmt.setString(8, annonce.getExperience());
			stmt.setString(9, annonce.getEtudeLevel());
			stmt.setString(10, annonce.getContratDetails());
			stmt.setString(11, annonce.getUrl());
			stmt.setString(12, annonce.getSiteName());
			stmt.setString(13, annonce.getAdresseEntreprise());
			stmt.setString(14, annonce.getSiteWebEntreprise());
			stmt.setString(15, annonce.getNomEntreprise());
			stmt.setString(16, annonce.getDescriptionEntreprise());
			stmt.setString(17, annonce.getRegion());
			stmt.setString(18, annonce.getCity());
			stmt.setString(19, annonce.getIndustry());
			stmt.setString(20, annonce.getTraitsPersonnalite());
			stmt.setString(21, annonce.getCompetencesRequises());
			stmt.setString(22, annonce.getSoftSkills());
			stmt.setString(23, annonce.getCompetencesRecommandees());
			stmt.setString(24, annonce.getLangue());
			stmt.setString(25, annonce.getNiveauLangue());
			stmt.setString(26, annonce.getSalaire());
			stmt.setString(27, annonce.getAvantagesSociaux());
			stmt.setString(28, annonce.getTeletravail());
			stmt.executeUpdate();
		}
	}


	// Method to retrieve all Annonce objects from the database
	public List<Annonce> getAllAnnonces() throws SQLException {

		System.out.println("getAllAnnonces call") ;

		String query = "SELECT * FROM annonce";
		List<Annonce> annonces = new ArrayList<>();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Annonce annonce = new Annonce(
						rs.getInt("id"),
						rs.getString("title"),
						rs.getString("description"),
						rs.getString("startDate"),
						rs.getString("endDate"),
						rs.getInt("postsNum"),
						rs.getString("secteur"),
						rs.getString("fonction"),
						rs.getString("experience"),
						rs.getString("etudeLevel"),
						rs.getString("contratDetails"),
						rs.getString("url"),
						rs.getString("siteName"),
						rs.getString("adresseEntreprise"),
						rs.getString("siteWebEntreprise"),
						rs.getString("nomEntreprise"),
						rs.getString("descriptionEntreprise"),
						rs.getString("region"),
						rs.getString("city"),
						rs.getString("industry"),
						rs.getString("traitsPersonnalite"),
						rs.getString("competencesRequises"),
						rs.getString("softSkills"),
						rs.getString("competencesRecommandees"),
						rs.getString("langue"),
						rs.getString("niveauLangue"),
						rs.getString("salaire"),
						rs.getString("avantagesSociaux"),
						rs.getString("teletravail")
				);
				annonces.add(annonce);
			}
		}

		return annonces;
	}

	// Method to update an existing Annonce in the database
	public void updateAnnonce(Annonce annonce) throws SQLException {
		String query = "UPDATE annonce SET title = ?, description = ?, startDate = ?, endDate = ?, postsNum = ?, " +
				"secteur = ?, fonction = ?, experience = ?, etudeLevel = ?, contratDetails = ?, url = ?, siteName = ?," +
				" adresseEntreprise = ?, siteWebEntreprise = ?, nomEntreprise = ?, descriptionEntreprise = ?, region = ?, " +
				"city = ?, industry = ?, traitsPersonnalite = ?, competencesRequises = ?, softSkills = ?," +
				" competencesRecommandees = ?, langue = ?, niveauLangue = ?, salaire = ?, avantagesSociaux = ?, " +
				"teletravail = ? WHERE id = ?";

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setString(1, annonce.getTitle());
			stmt.setString(2, annonce.getDescription());
			stmt.setString(3, annonce.getStartDate());
			stmt.setString(4, annonce.getEndDate());
			stmt.setInt(5, annonce.getPostsNum());
			stmt.setString(6, annonce.getSecteur());
			stmt.setString(7, annonce.getFonction());
			stmt.setString(8, annonce.getExperience());
			stmt.setString(9, annonce.getEtudeLevel());
			stmt.setString(10, annonce.getContratDetails());
			stmt.setString(11, annonce.getUrl());
			stmt.setString(12, annonce.getSiteName());
			stmt.setString(13, annonce.getAdresseEntreprise());
			stmt.setString(14, annonce.getSiteWebEntreprise());
			stmt.setString(15, annonce.getNomEntreprise());
			stmt.setString(16, annonce.getDescriptionEntreprise());
			stmt.setString(17, annonce.getRegion());
			stmt.setString(18, annonce.getCity());
			stmt.setString(19, annonce.getIndustry());
			stmt.setString(20, annonce.getTraitsPersonnalite());
			stmt.setString(21, annonce.getCompetencesRequises());
			stmt.setString(22, annonce.getSoftSkills());
			stmt.setString(23, annonce.getCompetencesRecommandees());
			stmt.setString(24, annonce.getLangue());
			stmt.setString(25, annonce.getNiveauLangue());
			stmt.setString(26, annonce.getSalaire());
			stmt.setString(27, annonce.getAvantagesSociaux());
			stmt.setString(28, annonce.getTeletravail());
			stmt.setInt(29, annonce.getId());
			stmt.executeUpdate();
		}
	}

	//LA TAILLE DE LA BASE DE DONNES
	public int DBsize(){
		int i = 0 ;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT COUNT(*) FROM annonce";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");


		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				i = resultSet.getInt(1);
				break;
			}
		}
		catch(Exception e) {
			System.out.println("Selection failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return i;
	}

	//1st Case

	//SELECTION DE LA BASE AVEC UNE CONDITION SUR UNE DES COLOGNE
	public ArrayList<Annonce> SelectedData(String attribut , String token){
		ArrayList<Annonce> a = new ArrayList<Annonce>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * FROM annonce WHERE "+attribut+" LIKE ?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");


		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + token + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				//ERREUR C EST JUSTE A CAUSE DU CHANGEMENT DE LA BASE DE DONNES
				Annonce test = new Annonce(resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5),
						resultSet.getInt(6),
						resultSet.getString(7),
						resultSet.getString(8),
						resultSet.getString(9),
						resultSet.getString(10),
						resultSet.getString(11),
						resultSet.getString(12),
						resultSet.getString(13),
						resultSet.getString(14),
						resultSet.getString(15),
						resultSet.getString(16),
						resultSet.getString(17),
						resultSet.getString(18),
						resultSet.getString(19),
						resultSet.getString(20),
						resultSet.getString(21),
						resultSet.getString(22),
						resultSet.getString(23),
						resultSet.getString(24),
						resultSet.getString(25),
						resultSet.getString(26),
						resultSet.getString(27),
						resultSet.getString(28),
						resultSet.getString(29));
				a.add(test);
			}
		}
		catch(Exception e) {
			System.out.println("Selection error failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return a;
	}

	//SELECTION DE LA BASE POUR LES DIAGRAMMES JFREECHART
	public ArrayList<Pair<String,Integer>> CountSelection(String attribut , String token){
		Boolean tokenParsed = false;
		int i = 0 ;
		int TotalSize = DBsize();
		ArrayList<Pair<String,Integer>> pairTable = new ArrayList<Pair<String,Integer>>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT "+attribut+" , COUNT(*) as row_count FROM annonce GROUP BY "+attribut+" ORDER BY row_count DESC";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");


		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				if(i<4){
					Pair<String,Integer> element = Pair.of(resultSet.getString(1),(int) ((resultSet.getInt(2) / (double) TotalSize) * 100));
					pairTable.add(element);
					if(element.getLeft().contains(token)){
						tokenParsed = true;
					}
				}
				if(i==4){
					if(tokenParsed){
						Pair<String,Integer> element = Pair.of(resultSet.getString(1),(int) ((resultSet.getInt(2) / (double) TotalSize) * 100));
						pairTable.add(element);
						break;
					}
					else{
						tokenParsed = resultSet.getString(1).contains(token);
					}
				}
				i ++ ;
			}
			if(pairTable.size() < 5){
				Pair<String,Integer> element = Pair.of(token,0);
				pairTable.add(element);
			}
		}
		catch(Exception e) {
			System.out.println("Selection error 2 failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pairTable;
	}

	//2nd Case

	//SELECTION DE LA BASE AVEC UNE CONDITION SUR UNE DES COLOGNE
	public ArrayList<Annonce> SelectedData2(String column , String attribut , String token){
		ArrayList<Annonce> a = new ArrayList<Annonce>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * FROM annonce WHERE "+attribut+" LIKE ?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");

		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + token + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Annonce test = new Annonce(
						resultSet.getInt(1),
						resultSet.getString(2),
						resultSet.getString(3),
						resultSet.getString(4),
						resultSet.getString(5),
						resultSet.getInt(6),
						resultSet.getString(7),
						resultSet.getString(8),
						resultSet.getString(9),
						resultSet.getString(10),
						resultSet.getString(11),
						resultSet.getString(12),
						resultSet.getString(13),
						resultSet.getString(14),
						resultSet.getString(15),
						resultSet.getString(16),
						resultSet.getString(17),
						resultSet.getString(18),
						resultSet.getString(19),
						resultSet.getString(20),
						resultSet.getString(21),
						resultSet.getString(22),
						resultSet.getString(23),
						resultSet.getString(24),
						resultSet.getString(25),
						resultSet.getString(26),
						resultSet.getString(27),
						resultSet.getString(28),
						resultSet.getString(29)
				);
				a.add(test);
			}
		}
		catch(Exception e) {
			System.out.println("Selection failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return a;
	}

	//SELECTION DE LA BASE POUR LES DIAGRAMMES JFREECHART
	public ArrayList<Pair<String,Integer>> CountSelection2(String Column , String attribut , String token){
		int i = 0 ;
		int TotalSize = DBsize();
		ArrayList<Pair<String,Integer>> pairTable = new ArrayList<Pair<String,Integer>>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT " + Column + ", COUNT(*) as row_count FROM annonce WHERE " + attribut + " LIKE ?  GROUP BY " + Column + " ORDER BY row_count DESC";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");


		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + token + "%");
			ResultSet resultSet = preparedStatement.executeQuery();

			while(resultSet.next()){
				if(i<5) {
					Pair<String,Integer> element = Pair.of(resultSet.getString(1),(int) ((resultSet.getInt(2) / (double) TotalSize) * 100));
					pairTable.add(element);
				}else {
					break;
				}

			}
		}
		catch(Exception e) {
			System.out.println("Selection failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pairTable;
	}
	//3rd Case

	//SELECTION DE LA BASE AVEC UNE CONDITION SUR UNE DES COLOGNE
	public ArrayList<Annonce> SelectedData3(String column , String attribut , String token){
		ArrayList<Annonce> a = new ArrayList<Annonce>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * FROM annonce WHERE "+attribut+" LIKE ?";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");

		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + token + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				Annonce test = new Annonce(
						resultSet.getInt("id"),
						resultSet.getString("title"),
						resultSet.getString("description"),
						resultSet.getString("StartDate"),
						resultSet.getString("EndDate"),
						resultSet.getInt("PostsNum"),
						resultSet.getString("Secteur"),
						resultSet.getString("Fonction"),
						resultSet.getString("Experience"),
						resultSet.getString("EtudeLevel"),
						resultSet.getString("ContratDetails"),
						resultSet.getString("URL"),
						resultSet.getString("SiteName"),
						resultSet.getString("Adresse d'entreprise"),
						resultSet.getString("Site web d'entreprise"),
						resultSet.getString("Nom d'entreprise"),
						resultSet.getString("Description d'entreprise"),
						resultSet.getString("Region"),
						resultSet.getString("City"),
						resultSet.getString("Industry"),
						resultSet.getString("TraitsPersonnalite"),
						resultSet.getString("CompetencesRequises"),
						resultSet.getString("SoftSkills"),
						resultSet.getString("CompetencesRecommandees"),
						resultSet.getString("Langue"),
						resultSet.getString("NiveauLangue"),
						resultSet.getString("Salaire"),
						resultSet.getString("AvantagesSociaux"),
						resultSet.getString("Teletravail")
				);
				a.add(test);
			}
		}
		catch(Exception e) {
			System.out.println("Selection failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return a;
	}

	//SELECTION DE LA BASE POUR LES DIAGRAMMES JFREECHART
	public ArrayList<Pair<String,Integer>> CountSelection3(String Column , String attribut , String token){
		Boolean tokenParsed = false;
		int i = 0 ;
		int TotalSize = DBsize();
		ArrayList<Pair<String,Integer>> pairTable = new ArrayList<Pair<String,Integer>>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT "+Column+" , COUNT(*) as row_count FROM annonce WHERE "+attribut+" LIKE ? "+token+"GROUP BY "+Column+" ORDER BY row_count DESC";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			connection = DriverManager.getConnection(url,user,password);
			System.out.println("Connected to the database!");


		}catch(Exception e){
			System.out.println("Connection failed");
		}
		try {
			preparedStatement = connection.prepareStatement(query);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				if(i<4){
					Pair<String,Integer> element = Pair.of(resultSet.getString(0),(int) ((resultSet.getInt(1)/TotalSize)*100));
					pairTable.add(element);
					if(element.getLeft().contains(token)){
						tokenParsed = true;
					}
				}
				if(i==4){
					if(tokenParsed){
						Pair<String,Integer> element = Pair.of(resultSet.getString(0),(int) ((resultSet.getInt(1)/TotalSize)*100));
						pairTable.add(element);
						break;
					}
					else{
						tokenParsed = resultSet.getString(0).contains(token);
					}
				}
				i ++ ;
			}
			if(pairTable.size() < 5){
				Pair<String,Integer> element = Pair.of(token,0);
				pairTable.add(element);
			}
		}
		catch(Exception e) {
			System.out.println("Selection failed");
		}

		try {
			if (preparedStatement != null) preparedStatement.close();
			if (connection != null) connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return pairTable;
	}

	public ResultSet executeQuery(String sqlQuery) throws SQLException {
		ResultSet resultSet = null;
		try {
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(sqlQuery);
		} catch (SQLException e) {
			throw new SQLException("Failed to execute query: " + e.getMessage());
		}
		return resultSet;
	}

	// Close the database connection
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}


	public double getAverageAnnoncesPerDay(String siteName) throws SQLException {
		String query = "SELECT StartDate FROM annonce WHERE SiteName = ?";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false); // Validation stricte du format

		Map<String, Integer> annoncesParJour = new HashMap<>();
		int totalAnnonces = 0;

		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			// Définir le paramètre de la requête
			stmt.setString(1, siteName);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String startDate = rs.getString("StartDate");

					try {
						// Valider que la date est au format "JJ/MM/YYYY"
						dateFormat.parse(startDate);

						// Ajouter au comptage pour ce jour
						annoncesParJour.put(startDate, annoncesParJour.getOrDefault(startDate, 0) + 1);
						totalAnnonces++;
					} catch (ParseException e) {
						// Ignorer les dates non valides
						System.out.println("Date ignorée (format invalide) : " + startDate);
					}
				}
			}
		}

		// Calculer la moyenne des annonces par jour
		return annoncesParJour.size() > 0 ? (double) totalAnnonces / annoncesParJour.size() : 0.0;
	}

	public double getPercentageBySiteName(String siteName) throws SQLException {
		String queryTotal = "SELECT COUNT(*) AS total FROM annonce";
		String queryBySiteName = "SELECT COUNT(*) AS countBySite FROM annonce WHERE SiteName = ?";

		int totalAnnonces = 0;
		int annoncesPourSite = 0;

		try (PreparedStatement stmtTotal = connection.prepareStatement(queryTotal);
			 PreparedStatement stmtBySiteName = connection.prepareStatement(queryBySiteName)) {

			// Obtenir le nombre total d'enregistrements
			try (ResultSet rsTotal = stmtTotal.executeQuery()) {
				if (rsTotal.next()) {
					totalAnnonces = rsTotal.getInt("total");
				}
			}

			// Obtenir le nombre d'enregistrements pour le site spécifié
			stmtBySiteName.setString(1, siteName);
			try (ResultSet rsBySite = stmtBySiteName.executeQuery()) {
				if (rsBySite.next()) {
					annoncesPourSite = rsBySite.getInt("countBySite");
				}
			}
		}

		// Calculer le pourcentage
		return totalAnnonces > 0 ? (double) annoncesPourSite / totalAnnonces * 100 : 0.0;
	}





}
