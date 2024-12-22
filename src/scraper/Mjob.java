package scraper;
import model.Annonce;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mjob {

    static public List<Annonce> listeAnnonce = new ArrayList<Annonce>();
    int id = 0;
    static String title;
    static String searchedProfile; //profile à recherché
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
    static String posteAoccuper; //Poste à occuper
    static String langues;
    static String salary;
    static String avantagesSociaux;

    public Mjob() {
    }

    public Mjob(String title, String searchedProfile, String publishDate, String secteur,
                String experienceYears, String StudyLevel, String contractType, String annonceLink,
                String company_name, String descriptionDentreprise, String city, String industry,
                String softskills, String langues, String salary, String avantagesSociaux) {
        Mjob.title = title;
        Mjob.searchedProfile = searchedProfile;
        Mjob.publishDate = publishDate;
        Mjob.secteur = secteur;
        Mjob.experienceYears = experienceYears;
        Mjob.StudyLevel = StudyLevel;
        Mjob.contractType = contractType;
        Mjob.annonceLink = annonceLink;
        Mjob.company_name = company_name;
        Mjob.descriptionDentreprise = descriptionDentreprise;
        Mjob.city = city;
        Mjob.industry = industry;
        Mjob.posteAoccuper = softskills;
        Mjob.langues = langues;
        Mjob.salary = salary;
        Mjob.avantagesSociaux = avantagesSociaux;
    }

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

    public static String getPosteAoccuper() {
        return posteAoccuper;
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

    public List<Annonce> mjobscrapping() {

        String totalPages="15" ;
        try {
            Document document = Jsoup.connect(mjob).get();
            Elements spanElements = document.select("div.offers-list-header > span");

            if (!spanElements.isEmpty()) {
                // Récupérer le texte (le bon <span>)
                String text = spanElements.first().text();

                // Extraire le nombre de pages avec une regex
                totalPages = text.replaceAll(".*Page \\d+ / (\\d+) .*", "$1");
                try {
                    // Convertir en entier
                    int totalPagesInt = Integer.parseInt(totalPages);
                    System.out.println("Nombre total de pages : " + totalPagesInt);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur lors de la conversion en entier !");
                }
            } else {
                System.out.println("Aucun texte correspondant trouvé !");
            }
            for(int j=0 ; j<Integer.parseInt(totalPages);j++ ){

                Elements jobItems = document.select("div.offer-box");

                for (Element jobItem : jobItems) {
                id++;
                System.out.println(id);
                Element titleElement = jobItem.selectFirst("h3.offer-title");
                if (titleElement != null) {
                    title = titleElement.text();
                    System.out.println("1- Title: " + title);
                } else {
                    System.out.println("No title found");
                }

                Element locationElement = jobItem.selectFirst(".location");
                if (locationElement != null) {
                    city = locationElement.text();
                    System.out.println("2- Location: " + city);
                } else {
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
                } else {
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
                    System.out.println("Niiice: " + annonceLink);

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
                        /**/
                        Element companyDescription = divElements.get(0);


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
                            posteAoccuper = paragraphs2.get(i).text();
                            System.out.println(searchedProfile);
                            for (String keyword : softSkillsKeywords) {
                                if (posteAoccuper.toLowerCase().contains(keyword.toLowerCase())) {
                                    System.out.println("-.-> " + posteAoccuper);
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

                            } else if (headingText.equals("Secteur(s) d'activité :")) {
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
                                System.out.println("12- Niveau d'expériences requis: " + experienceYears);
                            } else if (headingText.equals("Niveau d'études exigé :")) {
                                Element requiredStudyYearsElement = heading.nextElementSibling();

                                assert requiredStudyYearsElement != null;
                                StudyLevel = requiredStudyYearsElement.text();
                                System.out.println("13- Niveau d'études exigé: " + StudyLevel);

                            } else if (headingText.equals("Langue(s) exigée(s) :")) { // Replace with your target text
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

                Annonce annonce = pretraitement(id, title, searchedProfile, publishDate, secteur, experienceYears,
                        StudyLevel, contractType, annonceLink, company_name, descriptionDentreprise, city, industry,
                        posteAoccuper, langues, salary, avantagesSociaux);

                listeAnnonce.add(annonce);
            }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return listeAnnonce;
    }

    private Annonce pretraitement(int id,
                                  String title, String searchedProfile, String publishDate,
                                  String secteur, String experienceYears, String studyLevel,
                                  String contractType, String annonceLink, String companyName,
                                  String descriptionDentreprise, String city, String industry,
                                  String softskills, String langues, String salary,
                                  String avantagesSociaux
                                    )
    {

        Annonce annonce = new Annonce();
        annonce.setId(id);
        annonce.setTitle(title);
        annonce.setDescription(searchedProfile);
        annonce.setStartDate(PretraitementAnnonceAttribut.convertToDate(publishDate));
        annonce.setEndDate("");
        annonce.setPostsNum(-1);
        annonce.setSecteur(secteur);

        return annonce ;
    }


}
