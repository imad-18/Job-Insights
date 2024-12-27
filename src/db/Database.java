package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Annonce;

public class Database {

	private Connection connection;

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

	// Close the database connection
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}
}
