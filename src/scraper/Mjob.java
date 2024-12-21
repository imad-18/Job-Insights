package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class Mjob {
    static String title;
    static String searchedProfile;
    static String publishDate;
    static String secteur;
    static String experienceYears;
    static String StudyLevel;
    static String contractType;
    static String annonceLink;
    static String company_name;
    static String descriptionDentreprise;
    static String city;
    static String industry;
    static String softskills;
    static String langues;
    static String salary;
    static String avantagesSociaux;

    public static String getTitle() {
        return title;
    }

    public static String getSearchedProfile() {
        return searchedProfile;
    }

    public static String getPublishDate() {
        return publishDate;
    }

    public static String getSecteur() {
        return secteur;
    }

    public static String getExperienceYears() {
        return experienceYears;
    }

    public static String getStudyLevel() {
        return StudyLevel;
    }

    public static String getContractType() {
        return contractType;
    }

    public static String getAnnonceLink() {
        return annonceLink;
    }

    public static String getCompany_name() {
        return company_name;
    }

    public static String getDescriptionDentreprise() {
        return descriptionDentreprise;
    }

    public static String getCity() {
        return city;
    }

    public static String getIndustry() {
        return industry;
    }

    public static String getSoftskills() {
        return softskills;
    }

    public static String getLangues() {
        return langues;
    }

    public static String getSalary() {
        return salary;
    }

    public static String getAvantagesSociaux() {
        return avantagesSociaux;
    }

    public String getMjob() {
        return mjob;
    }

    String mjob = "https://www.m-job.ma/recherche";

    public void mjobscrapping(){
        try {
            Document document = Jsoup.connect(mjob).get();
            Elements jobItems = document.select("div.offer-box");


            for (Element jobItem : jobItems) {
                Element titleElement = jobItem.selectFirst("h3.offer-title");
                if (titleElement != null) {
                    title = titleElement.text();
                    System.out.println("1- Title: "+title);
                }else {
                    System.out.println("No title found");
                }

                Element locationElement = jobItem.selectFirst(".location");
                if (locationElement != null) {
                    city = locationElement.text();
                    System.out.println("2- Location: "+city);
                }else {
                    System.out.println("No location found");
                }

                Element offerBody = jobItem.selectFirst("div.offer-body");
                if (offerBody != null) {
                /*Element descriptionElement = offerBody.select("p").first();
                String description = descriptionElement.text();
                System.out.println("Description: " + description);*/

                    Element skillsElement = offerBody.select("p").get(1);
                    String skills = skillsElement.text();
                    System.out.println("Skills: " + skills);
                }else{
                    System.out.println("No description or skills found");
                }

                Element publishDateElement = jobItem.selectFirst(".date-buttons");
                if (publishDateElement != null) {
                    publishDate = publishDateElement.text();
                    System.out.println("3- Date de publication: " + publishDate);
                }

                Element voirplusElement = jobItem.selectFirst("div.form-group > a");
                if (voirplusElement != null) {
                    // Get the href attribute value
                    annonceLink = voirplusElement.attr("href"); //hrefvalue
                    System.out.println("Niiice: " +annonceLink);

                    Document document1 = Jsoup.connect(annonceLink).get();

                    Element listDetails = document1.selectFirst("ul.list-details");

                    assert listDetails != null;
                    Element companyElement = listDetails.select("li > h3").first();
                    company_name = companyElement.text();
                    System.out.println("4- Société: " + company_name);

                    Element contractTypeElement = listDetails.select("li > h3").get(1);
                    contractType = contractTypeElement.text();
                    System.out.println("5- Contract type: " + contractType);

                    Element salaryElement = listDetails.select("li > h3").get(2);
                    salary = salaryElement.text();
                    System.out.println("6- Salary: " + salary);


                    // Select parent div with class 'the-content'
                    Element otherInfos = document1.selectFirst("div.the-content");



                    // Safely check if the parent div exists
                    if (otherInfos != null) {
                        // Select all <div> elements within the parent
                        Elements divElements = otherInfos.select("div");

                        //first div containing company desc + avantages sociaux
                        /**/Element companyDescription = divElements.get(0);


                        // Select all <p> elements inside the sub-div
                        Elements paragraphs = companyDescription.select("p");
                        Elements listElements = companyDescription.select("ul > li");



                        // Define keywords to search for
                        Map<String, String> extractedData = new HashMap<>(); // Store data by keyword
                        String[] keywords = {"Avantages sociaux", "AMO", "CNSS", "navettes", "rémunération"};

                        // Check if list elements exist
                        if (!listElements.isEmpty()) {
                            System.out.println("Fetching data from <li> elements:");
                            System.out.println("7- Avantages sociaux: ");
                            for (Element listItem : listElements) {
                                avantagesSociaux = listItem.text();
                                for (String keyword : keywords) {
                                    if (avantagesSociaux.toLowerCase().contains(keyword.toLowerCase())) { // Match keyword ignoring case
                                        extractedData.put(keyword, extractedData.getOrDefault(keyword, "") + "\n" + avantagesSociaux);
                                        System.out.println("-> " + avantagesSociaux);
                                        break; // Stop checking this list item for other keywords
                                    }
                                }
                                /*String listItemText = listItem.text();
                                System.out.println("List Items: "+listItemText);*/
                            }
                        } else if (!paragraphs.isEmpty()) { // If <li> elements are not found, fetch <p> elements
                            System.out.println("Fetching data from <p> elements:");
                            System.out.println("7- Avantages sociaux: ");
                            for (Element paragraph : paragraphs) {
                                // Get the HTML of the paragraph and replace <br> tags with newline characters

                                // Convert the processed HTML back to plain text
                                avantagesSociaux = paragraph.html().replace("<br>", "\n");
                                //avantagesSociaux = paragraph.text();
                                for (String keyword : keywords) {
                                    if (avantagesSociaux.toLowerCase().contains(keyword.toLowerCase())) { // Match keyword ignoring case
                                        extractedData.put(keyword, avantagesSociaux); // Store matching avantagesSociaux
                                        System.out.println("->" + avantagesSociaux);
                                        break; // Stop checking other keywords for this paragraph
                                    }
                                }
                                /*String paragraphText = paragraph.text();
                                System.out.println(paragraphText);*/
                            }
                        } else {
                            System.out.println("No <p> or <li> elements found.");
                        }

                        // Output extracted data
                        /*for (String keyword : keywords) {
                            if (extractedData.containsKey(keyword)) {
                                System.out.println("-->" + keyword + ": " + extractedData.get(keyword));
                            } else {
                                System.out.println(keyword + ": Not found");
                            }
                        }*/


                        String[] softSkillsKeywords = {"Capacité", "aisance", "excellente", "maîtrise", "communiquer", "écoute"};

                        //2nd div containing searched profile
                        Element profileRecherche = divElements.get(3);
                        Elements paragraphs2 = profileRecherche.select("p");
                        System.out.println("8- Profile recherché: ");
                        for (int i = 0; i < paragraphs2.size(); i++) {
                            searchedProfile = paragraphs2.get(i).text();
                            softskills = paragraphs2.get(i).text();
                            System.out.println(searchedProfile);
                            for (String keyword : softSkillsKeywords) {
                                if (softskills.toLowerCase().contains(keyword.toLowerCase())) {
                                    System.out.println("-.-> " + softskills);
                                }
                            }
                        }

                        /*if (!paragraphs2.isEmpty()){
                            System.out.println("<---->Soft skills:");
                            for (Element myParagraph : paragraphs2) {
                                String myParagraphText = myParagraph.text();
                                for (String keyword : softSkillsKeywords) {
                                    if (myParagraphText.toLowerCase().contains(keyword.toLowerCase())) {
                                        System.out.println("-> " + myParagraphText);
                                    }
                                }
                            }
                        }*/

                        // Select all <h3> elements
                        Elements headings = otherInfos.select("h3.heading");

                        // Loop through each <h3> element to find the one you're targeting
                        for (Element heading : headings) {
                            String headingText = heading.text();

                            if (headingText.equals("Le recruteur :")) {
                                //DEScription d'entreprise
                                Element companyDescriptionText = heading.nextElementSibling();

                                assert companyDescriptionText != null;
                                descriptionDentreprise = companyDescriptionText.text();
                                System.out.println("9- Description d'entreprise: " + descriptionDentreprise);

                            }
                            else if (headingText.equals("Secteur(s) d'activité :")){
                                Element activityAreaElement = heading.nextElementSibling(); // Index starts from 0
                                
                                assert activityAreaElement != null;
                                secteur = activityAreaElement.text();
                                System.out.println("10- ActivityArea: " + secteur);

                            } else if (headingText.equals("Métier(s) :")) {
                                Element industryElement = heading.nextElementSibling();
                                assert industryElement != null;
                                industry = industryElement.text();
                                System.out.println("11- Metier: " + industry);
                                
                            } else if (headingText.equals("Niveau d'expériences requis :")) {
                                Element requiredStudyYearsElement = heading.nextElementSibling();

                                assert requiredStudyYearsElement != null;
                                experienceYears = requiredStudyYearsElement.text();
                                System.out.println("12- Niveau d'expériences requis: " +experienceYears);
                            }
                            else if (headingText.equals("Niveau d'études exigé :")) {
                                Element requiredStudyYearsElement = heading.nextElementSibling();

                                assert requiredStudyYearsElement != null;
                                StudyLevel = requiredStudyYearsElement.text();
                                System.out.println("13- Niveau d'études exigé: " +StudyLevel);

                            }
                            else if (headingText.equals("Langue(s) exigée(s) :")) { // Replace with your target text
                                // Get the next sibling div after the <h3> element
                                Element siblingDiv = heading.nextElementSibling();

                                assert siblingDiv != null;
                                langues = siblingDiv.text();
                                System.out.println("14- Langages: " + langues);
                                break; // Exit the loop after finding the target heading
                            }
                        }

                        System.out.println("-----------------");
                    } else {
                        System.out.println("Parent div with class 'the-content' not found.");
                    }

                } else {
                    System.out.println("Anchor link not found!");
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Mjob mjob = new Mjob();
        mjob.mjobscrapping();


        // Sample data (replace with your actual data)
        String title = "Example Title";
        String searchedProfile = "Description of job";
        String secteur = "IT";
        String experienceYears = "3 years";
        String StudyLevel = "Bachelor";
        String contractType = "Full-Time";
        String annonceLink = "http://example.com/job";
        String company_name = "Example Company";
        String descriptionDentreprise = "Leading tech company";
        String city = "City Name";
        String industry = "Technology";
        String softskills = "Teamwork, Communication";
        String langues = "English";
        String salary = "5000";
        String avantagesSociaux = "Health insurance";
        // Scraped data

        // Database credentials
        String url = "jdbc:mysql://localhost:3306/javaproject"; // Database URL
        String username = "root"; // Your MySQL username
        String password = ""; // Your MySQL password

        // SQL query to insert data (with unused columns set to NULL)
        /*String sql = "INSERT INTO annonce (title, city, publishDate, company_name, contractType, salary, secteur, industry, experienceYears, StudyLevel, langues, avantagesSociaux, searchedProfile, softskills, extraField1, extraField2) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL)";*/

        String sql = "INSERT INTO annonce (`title`, `description`, `Secteur`, `Experience`, `EtudeLevel`, `ContratDetails`, `URL`, `Nom d'entreprise`, `Description d'entreprise`, `City`, `Industry`, `SoftSkills`, `Langue`, `Salaire`, `AvantagesSociaux`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sql_query = "INSERT INTO `annonce`(`id`, `title`, `description`, `StartDate`, `EndDate`, `PostsNum`, `Secteur`, `Fonction`, `Experience`, `EtudeLevel`, `ContratDetails`, `URL`, `SiteName`, `Adresse d'entreprise`, `Site web d'entreprise`, `Nom d'entreprise`, `Description d'entreprise`, `Region`, `City`, `Industry`, `TraitsPersonnalite`, `CompetencesRequises`, `SoftSkills`, `CompetencesRecommandees`, `Langue`, `NiveauLangue`, `Salaire`, `AvantagesSociaux`, `Teletravail`) " +
                "VALUES ('[value-1]','[value-2]','[value-3]','[value-4]','[value-5]','[value-6]','[value-7]','[value-8]','[value-9]','[value-10]','[value-11]','[value-12]','[value-13]','[value-14]','[value-15]','[value-16]','[value-17]','[value-18]','[value-19]','[value-20]','[value-21]','[value-22]','[value-23]','[value-24]','[value-25]','[value-26]','[value-27]','[value-28]','[value-29]')";
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // Create a prepared statement
            PreparedStatement statement = connection.prepareStatement(sql);

            // Set the values for the placeholders (?)
            // Use data from the Mjob object
            /*statement.setString(1, mjob.getTitle());                   // title
            statement.setString(2, mjob.getSearchedProfile());         // description
            statement.setString(3, mjob.getSecteur());                 // Secteur
            statement.setString(4, mjob.getExperienceYears());         // Experience
            statement.setString(5, mjob.getStudyLevel());              // EtudeLevel
            statement.setString(6, mjob.getContractType());            // ContratDetails
            statement.setString(7, mjob.getAnnonceLink());             // URL
            statement.setString(8, mjob.getCompany_name());             // Nom d'entreprise
            statement.setString(9, mjob.getDescriptionDentreprise());  // Description d'entreprise
            statement.setString(10, mjob.getCity());                   // City
            statement.setString(11, mjob.getIndustry());               // Industry
            statement.setString(12, mjob.getSoftskills());             // SoftSkills
            statement.setString(13, mjob.getLangues());                // Langue
            statement.setString(14, mjob.getSalary());                 // Salaire
            statement.setString(15, mjob.getAvantagesSociaux());       // AvantagesSociaux
*/
            // Set the values for the placeholders
            statement.setString(1, title);                    // title
            statement.setString(2, searchedProfile);          // description
            statement.setString(3, secteur);                 // Secteur
            statement.setString(4, experienceYears);         // Experience
            statement.setString(5, StudyLevel);              // EtudeLevel
            statement.setString(6, contractType);            // ContratDetails
            statement.setString(7, annonceLink);             // URL
            statement.setString(8, company_name);            // Nom d'entreprise
            statement.setString(9, descriptionDentreprise);  // Description d'entreprise
            statement.setString(10, city);                   // City
            statement.setString(11, industry);               // Industry
            statement.setString(12, softskills);             // SoftSkills
            statement.setString(13, langues);                // Langue
            statement.setString(14, salary);                 // Salaire
            statement.setString(15, avantagesSociaux);       // AvantagesSociaux
            // Execute the query
            int rowsInserted = statement.executeUpdate();

            // Check if the insertion was successful
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("No rows were inserted.");
            }

            // Close the connection
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}