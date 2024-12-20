package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import model.Annonce;

public class Database {

	    // Database connection details
	    private String url = "jdbc:mysql://localhost:3306/javaproject";
	    private String user = "root";
	    private String password = "";

		//INSERTION DANS LA BASE
	    public void insertData(ArrayList<Annonce> listAnnonce) {
	        // JDBC objects
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;

	        // la requeette d insertion
	        String insertQuery = "INSERT INTO annonce (title, location, description, StartDate, EndDate, PostsNum, Secteur, Fonction, Experience, EtudeLevel, "
	        		+ "ContratDetails) VALUES (?, ?, ?, STR_TO_DATE(REGEXP_REPLACE(?, '[.]', '/'), '%d/%m/%Y'), STR_TO_DATE(REGEXP_REPLACE(?, '[.]', '/'), '%d/%m/%Y'),"
	        		+ " ?, ?, ?, ?, ?, ?)";
	        try {
	        	// Loading the connection
				Class.forName("com.mysql.cj.jdbc.Driver");
				
				// creation de la connexion avec url mdp et username
		        connection = DriverManager.getConnection(url, user, password);
		        System.out.println("Connected to the database!");
		        
			} catch (Exception e) {
				e.printStackTrace();
			}
	        
	        ArrayList test = this.selectData();
	        if(test.size() == 0) {
	        	int i = listAnnonce.size();
	            for(Annonce item : listAnnonce) {
	            	try {
	                    preparedStatement = connection.prepareStatement(insertQuery);
	                    
	                    preparedStatement.setString(1, item.getTitle());
	                    preparedStatement.setString(2, item.getLocation());
	                    preparedStatement.setString(3, item.getDescription());
	                    preparedStatement.setString(4, item.getStartDate());
	                    preparedStatement.setString(5, item.getEndDate());
	                    preparedStatement.setInt(6, item.getPostsNum());
	                    preparedStatement.setString(7, item.getSecteur());
	                    preparedStatement.setString(8, item.getFonction());
	                    preparedStatement.setString(9, item.getExperience());
	                    preparedStatement.setString(10, item.getEtudeLevel());
	                    preparedStatement.setString(11, item.getContracDetails());

	                    // Step 4: Execute the query
	                    int rowsInserted = preparedStatement.executeUpdate();
	                    if (rowsInserted > i) {
	                        System.out.println("A new row ("+i+") was inserted successfully!");
	                        i++;
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        else {
	        	System.out.println("rows already added ...");
	        }
	        // Step 5: Close the resources
	        try {
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        
	    }

		//SELECTION DE LA BASE
	    public ArrayList<Annonce> selectData() {
	    	ArrayList<Annonce> a = new ArrayList<Annonce>();
	    	// JDBC objects
	        Connection connection = null;
	        PreparedStatement preparedStatement = null;
	        //requete de selection 
	        String query = "SELECT * FROM annonce";
	    	try {
	    		//preparation a la connection 
	    		Class.forName("com.mysql.cj.jdbc.Driver");
	    		
	    		//Connection
	    		connection = DriverManager.getConnection(url,user,password);
	    		System.out.println("Connected to the database!");
	    		
	    		
	    	}catch(Exception e){
	    		System.out.println("Connection failed");
	    	}
	    	try {
	    		preparedStatement = connection.prepareStatement(query);
	    		// Executing the query
	            ResultSet resultSet = preparedStatement.executeQuery();
	            while(resultSet.next()){
	            	Annonce test = new Annonce(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)
	            			,resultSet.getString(6),resultSet.getInt(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),
	            			resultSet.getString(12));
	            	a.add(test);
	            }
	    	}
	    	catch(Exception e) {
	    		System.out.println("Selection failed");
	    	}
	    	
	    	// Step 5: Close the resources
	        try {
	            if (preparedStatement != null) preparedStatement.close();
	            if (connection != null) connection.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    	return a;
	    	
	    }

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
	            	Annonce test = new Annonce(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)
	            			,resultSet.getString(6),resultSet.getInt(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),
	            			resultSet.getString(12));
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

		//SELECTION DE LA BASE AVEC UNE CONDITION BEETWEEN SUR UNE DES COLOGNE
		public ArrayList<Annonce> BetweenSelection(String attribut , String token,int c, int b){
			ArrayList<Annonce> a = new ArrayList<Annonce>();
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			String query = "SELECT * FROM annonce WHERE "+attribut+" BETWEEN "+c+" AND "+b;
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
					Annonce test = new Annonce(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)
						,resultSet.getString(6),resultSet.getInt(7),resultSet.getString(8),resultSet.getString(9),resultSet.getString(10),resultSet.getString(11),
						resultSet.getString(12));
					a.add(test);
				}
			}
			catch(Exception e) {
				System.out.println("Between Selection failed");
			}

			try {
				if (preparedStatement != null) preparedStatement.close();
				if (connection != null) connection.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return a;
		}

		//LA TAILLE DE LA BASE DE DONNES
		public Integer DBsize(){
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

		//SELECTION DE LA BASE POUR LES DIAGRAMMES JFREECHART
		public ArrayList<Pair<String,Integer>> CountSelection(String attribut , String token){
			Boolean tokenParsed = false;
			int i = 0 ;
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
						Pair<String,Integer> element = Pair.of(resultSet.getString(1),resultSet.getInt(2));
						pairTable.add(element);
						if(element.getLeft().contains(token)){
							tokenParsed = true;
						}
					}
					if(i==4){
						if(tokenParsed){
							Pair<String,Integer> element = Pair.of(resultSet.getString(1),resultSet.getInt(2));
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
}
