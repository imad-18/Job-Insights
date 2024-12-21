package scraper;

import java.io.IOException;
import java.time.LocalDate;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Annonce;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

import static scraper.Mjob.softskills;

public class Rekrute {
    private ArrayList<String> links = new ArrayList<String>();
    private ArrayList<Annonce> annonces = new ArrayList<Annonce>();
    static String rekruteSoftSkills;

    public void Scrapper() throws ParseException {
        LocalDate actualdate = LocalDate.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Pattern pattern1 = Pattern.compile("Postulez avant le\\s*(\\d{1,2}/\\d{1,2}/\\d{4})");
        Pattern pattern2 = Pattern.compile("Publiée aujourd'hui");
        Pattern pattern3 = Pattern.compile("Publiée il y a\\s(\\d{1,2}) jours sur ReKrute.com");
        Pattern patregion = Pattern.compile("\\b(Casablanca|Marrakesh|Fes|Tangier|Rabat|Agadir|Meknes|Oujda|Tetouan|Safi|El Jadida|Nador|Kénitra|Taroudant|Ifrane|Chefchaouen|Beni Mellal|Azilal|Al Hoceima|Errachidia|Laayoune|Dakhla|Tiznit|Khouribga|Settat|Ben Guerir)\\b");

        try {
            // Récupérer les liens des pages d'annonces
            for (int j = 0; j <= 27; j++) {
                String URL = "https://www.rekrute.com/fr/offres.html?s=3&p=" + j + "&o=1";
                Document document = Jsoup.connect(URL).get();
                Elements linkElements = document.select("a");

                for (Element link : linkElements) {
                    String linkURL = link.attr("href");
                    if (linkURL.startsWith("/fr/offre-emploi-") && linkURL.endsWith(".html")) {
                        links.add("https://www.rekrute.com" + linkURL);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Parcourir les liens et extraire les informations de chaque annonce
        for (String lien : links) {
            try {
                Document doc_of_link = Jsoup.connect(lien).get();

                // Titre
                String title = doc_of_link.select("title").first() != null ? 
                               doc_of_link.select("title").first().text() : "Titre non trouvé";

                // SiteName 
                String sitename = "Rekrute.com";

                // URL 
                String url = lien;


                Elements divElements = doc_of_link.select("div.col-md-12.blc"); // Select all <div> elements with the specified class

                if (divElements.isEmpty()) {
                    System.out.println("No <div> elements found with class 'col-md-12 blc'.");
                } else {
                    System.out.println("8- 🥇 Compétences requises:");

                    for (Element divElement : divElements) {
                        // Check if this <div> contains an <h2> with the text "Profil recherché :"
                        Element h2Element = divElement.selectFirst("h2");
                        if (h2Element != null && h2Element.text().equals("Profil recherché :")) {
                            // Found the correct <div>; now look for the <ul> or the third <p> element
                            Element ulElement = divElement.selectFirst("ul"); // Check for <ul>
                            Element pElement = null;

                            // Check if there is a third <p> element
                            Elements pElements = divElement.select("p");
                            if (pElements.size() >= 3) {
                                pElement = pElements.get(2); // Get the third <p> element (index 2)
                            }

                            if (ulElement != null) {
                                // Process the <ul> element
                                Elements liElements = ulElement.select("li");
                                if (liElements.isEmpty()) {
                                    System.out.println("No <li> elements found inside the <ul>.");
                                } else {
                                    for (Element liElement : liElements) {
                                        String rekruteSoftSkills = liElement.text();
                                        System.out.println("✨ " + rekruteSoftSkills);
                                    }
                                }
                            } else if (pElement != null) {
                                // Process the third <p> element
                                String myTargetedP = pElement.html().replace("<br>", "\n"); // Replace <br> tags with newlines
                                System.out.println("✨ Soft Skills (from <p>): " + myTargetedP);
                            } else {
                                System.out.println("No <ul> or third <p> element found inside this <div>.");
                            }

                            break; // Exit the loop since the desired <div> has been found and processed
                        }
                    }
                }




                //For fonction and Compétences 
                String fct_result="",cmpt_result="";
                Elements col_class = doc_of_link.select("div.col-md-12.blc");

                for(Element div : col_class) {
                	Element h2 = div.selectFirst("h2");
                    if (h2 != null && h2.text().equals("Poste :")) {
                    	String full_text = div.text();
                    	int index = full_text.indexOf("Poste :");
                    	if(index !=-1) {
                    		fct_result = full_text.substring(index + "Poste :".length()).trim();
                    	}
                        break;  
                    }
                    else if (h2 != null && h2.text().trim().toLowerCase().equals("Profil recherché :")) {
                        // Select the <ul> under the current div
                        Element ul = div.selectFirst("ul");
                        
                        if (ul != null) {
                            // Extract the list items (li)
                            Elements liElements = ul.select("li");

                            // Store the text from each li element, e.g., in a list or a single string
                            StringBuilder cmptText = new StringBuilder();
                            for (Element li : liElements) {
                                cmptText.append(li.text()).append("\n");
                            }
                            
                            // Trim any leading/trailing spaces and set the result
                            cmpt_result = cmptText.toString().trim();
                            cmpt_result = Jsoup.parse(cmpt_result).text();
                        }
                        break;  // Stop processing once we've handled this section
                    }

                }
                
                
                /*// Fonction
                Element post = doc_of_link.select(".col-md-12.blc").size() > 3? 
                               doc_of_link.select(".col-md-12.blc").get(3) : null;
                Element p = post != null ? post.selectFirst("p"):null;
                String fct = p != null ? Jsoup.parse(p.text()).text() : "";*/

                // Description
                Element description = doc_of_link.select("meta[name=description]").first();
                String desc = description != null ? description.attr("content") : "Description non trouvée";
                String desc_text = Jsoup.parse(desc).text();
                
                
                // Secteur et Industry
                Element secteurElement = doc_of_link.selectFirst(".h2italic");
                String[] title_span = secteurElement != null ? secteurElement.text().split("- Secteur") : new String[0];
                String secteur = title_span.length > 0 ? title_span[1] : "Secteur non spécifié";
                String industry = title_span.length > 1 ? title_span[0] : "Industrie non spécifiée";

                /*// Compétences requises  
                Element profil = doc_of_link.select(".col-md-12.blc").size() > 4 ? 
                                 doc_of_link.select(".col-md-12.blc").get(4) : null;
                String profil_text = profil != null ? Jsoup.parse(profil.select("p").text()).text() : "Compétences non spécifiées";*/

                // Expérience, région, niveau, contrat, télétravail, post
                Elements ul_info = doc_of_link.select(".featureInfo li");
                String experience = "", region = "", niveau = "", contrat = "", teletravail = "", city = "", poste="";
                Matcher match_region;
                boolean matchfoundregion;
                for (Element liste : ul_info) {
                    String titre = liste.attr("title");
                    if (titre.equals("Expérience requise")) experience = liste.text();
                    else if (titre.equals("Région")) { 
                        poste = liste.select("b").text();
                        region = liste.text();
                        match_region = patregion.matcher(region);
                        matchfoundregion = match_region.find();
                        if (matchfoundregion && match_region.groupCount() > 0) {
                            region = match_region.group(1);
                            city = region;
                        }
                    } else if (titre.equals("Niveau d'étude et formation")) niveau = liste.text();
                    else if (titre.equals("Type de contrat")) contrat = liste.text();
                    else if (titre.equals("Télétravail")) teletravail = liste.text();
                }

                // Traits de personnalité 
                Elements skills = doc_of_link.select(".tagSkills");
                StringBuilder skillsList = new StringBuilder();
                for (Element skill : skills) {
                    skillsList.append(skill.text()).append(", ");
                }

                // Date de publication et Date postulation
                Element pubDateElement = doc_of_link.selectFirst(".newjob");
                String[] result;
                LocalDate datepublication = null, datedelaipost = null;
                String datepub_text = " ", enddate_text = " ";
                Matcher matcher, matcher1, matcher2;
                boolean matchfound, matchfound1, matchfound2;

                if (pubDateElement != null) {
                    result = pubDateElement.text().split("-");
                    datepub_text = result.length > 0 ? result[0] : "";
                    matcher = pattern3.matcher(datepub_text);
                    matcher1 = pattern2.matcher(datepub_text);
                    matchfound = matcher.find();
                    matchfound1 = matcher1.find();

                    if (matchfound) {
                        int days = Integer.parseInt(matcher.group(1));
                        datepublication = actualdate.minusDays(days);
                    } else if (matchfound1) {
                        datepublication = actualdate;
                    }

                    enddate_text = result.length > 1 ? result[1] : "";
                    matcher2 = pattern1.matcher(enddate_text);
                    matchfound2 = matcher2.find();

                    if (matchfound2) {
                        enddate_text = matcher2.group(1);
                        datedelaipost = LocalDate.parse(enddate_text, dateFormat);
                    }
                }

                // Nom entreprise
                String name = title.split("-")[2];
                
                // Adresse de l'entreprise 
                Element adr_balise = doc_of_link.selectFirst("#address");
                String adr_entr = adr_balise !=null ? adr_balise.text() : "";
                
                // Affichage des informations extraites
                System.out.println("Titre : " + title);
                System.out.println("Description : " + desc_text);
                System.out.println("PostNum :" + poste);
                System.out.println("SiteName :" + sitename);
                System.out.println("URL :" + url);
                System.out.println("Nom entreprise :" + name);
                System.out.println("Adresse entreprise :" + adr_entr);
                System.out.println("Industrie :" + industry);
                System.out.println("Fonction :" + fct_result);
                //System.out.println("Compétences requises :" + rekruteSoftSkills);
                System.out.println("Secteur : " + secteur);
                System.out.println("Expérience : " + experience);
                System.out.println("Région : " + region);
                System.out.println("City : " + city);
                System.out.println("Niveau : " + niveau);
                System.out.println("Contrat : " + contrat);
                System.out.println(teletravail);
                System.out.println("Traits de perso : " + skillsList.toString());
                System.out.println("Date de publication : " + (datepublication != null ? datepublication : "Non spécifiée"));
                System.out.println("Date de début de postulation :" +actualdate);
                System.out.println("Dernière date de postulation : " + (datedelaipost != null ? datedelaipost : "Non spécifiée"));
                System.out.println("----------");
                
                

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws ParseException {
        Rekrute rekrute = new Rekrute();
        rekrute.Scrapper();


    }
}

    /*public void connectDB() throws SQLException, ClassNotFoundException {
        String driverClassName = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/scrapping";
        String user = "root";
        String password = "";

        Connection connection = null;
        Class.forName(driverClassName);
        connection = DriverManager.getConnection(url, user, password);
    }*/
