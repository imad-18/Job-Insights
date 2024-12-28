package scraper;

import model.Annonce;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class anapec_scraper {
    private ArrayList<Annonce> listeAnnonce = new ArrayList<Annonce>();
    private int i=0;

    public anapec_scraper() {
    }

    public void ScraperAnapec() {

            try {
                String url = "https://anapec.ma/home-page-o1/chercheur-emploi/offres-demploi/?pg=1";
                Document document = Jsoup.connect(url).get();

                Elements jobItems = document.select("div.offres-container > div.offres-item");
                for (Element item : jobItems) {
                    //parsing the href title number of posts and startDate and endDate and SiteName
                    Elements link = item.select("a");
                    String URL = link.attr("href");
                    Elements paragraphs = item.select("div a div.offre-content p");
                    String title = paragraphs.get(0).text();
                    String StartDate = paragraphs.get(1).text();
                    String endDate = "non specifie";
                    String SiteName = "anapec";
                    int PostsNum = 0;
                    String numberRegex = "\\((\\d+)\\)";
                    Pattern pattern = Pattern.compile(numberRegex);
                    Matcher matcher = pattern.matcher(title);
                    if (matcher.find()) {
                        PostsNum = Integer.parseInt(matcher.group(1));
                    }
                    //parsing the href link
                    String descriptionEntreprise = "description non specifie";
                    String Secteur= "secteur non specifie";
                    String ContratDetails = "ContratDetails non specifie";
                    String description = "description non specifie";
                    String Fonction = "Fonction non specifie";
                    String Experience = "Experience non specifie";
                    String CompetencesRequises = "Competences requises non specifie";
                    String EtudeLevel = "EtudeLevel non specifie";
                    String City = "City non specifie";
                    String CompetencesRecommandees = "Competences recommandees  non specifie";

                    try{
                        Document document2 = Jsoup.connect(URL).get();
                        Elements paragraphs2 = document2.select("div#annonce_emploi div#oneofmine p");
                        //descriptionEntreprise
                        descriptionEntreprise = paragraphs2.get(0).text();
                        if(descriptionEntreprise == ""){
                            descriptionEntreprise = "description non specifie";
                        }

                        //autres
                        Elements para = paragraphs2.select("p:has(span):has(span)");

                        for (Element paragraph : paragraphs) {
                            Elements spans = paragraph.select("span");

                            if (spans.size() == 2) {
                                String label = spans.get(0).text();
                                String value = spans.get(1).text();

                                if (label.contains("Secteur d’activité  :")) {
                                    Secteur = value ;
                                } else if (label.contains("Type de contrat : ")) {
                                    ContratDetails = value ;
                                } else if (label.contains("Description du profil : ")) {
                                    description = value;
                                } else if (label.contains("Formation : ")) {
                                    if(value.contains("Bac")){
                                        EtudeLevel = value ;
                                    }else {
                                        Fonction = value;
                                    }
                                }else if (label.contains(" Poste : ")) {
                                    Fonction = value;
                                }else if (label.contains(" Expérience professionnelle  : ")) {
                                    Experience = value.replaceAll("[()]", "");
                                }else if (label.contains(" Compétences spécifiques : ")) {
                                    CompetencesRequises = value ;
                                }else if (label.contains("Lieu de travail :")) {
                                    City = value ;
                                }
                            }
                        }
                        if(CompetencesRequises != "Competences requises non specifie"){
                            CompetencesRecommandees = CompetencesRequises ;
                        }
                    }catch(Exception e){
                        System.out.println("the second path scraping error : "+e);
                    }
                    //Affichage de resultats
                    title = title.replaceAll("^\\(\\d+\\)\\s*", "");
                    System.out.println("title : " + title);
                    System.out.println("Startdate : " + StartDate);
                    System.out.println("Enddate : " + endDate);
                    System.out.println("nombre de posts : " + PostsNum);
                    System.out.println("link : " + URL);
                    System.out.println("Description d'entreprise : " + descriptionEntreprise);
                    System.out.println("secteur : " + Secteur);
                    System.out.println("SiteName : " + SiteName);
                    System.out.println("ContratDetails : " + ContratDetails);
                    System.out.println("description : " + description);
                    System.out.println("Fonction : " + Fonction);
                    System.out.println("Experience : " + Experience);
                    System.out.println("CompetencesRequises : " + CompetencesRequises);
                    System.out.println("CompetencesRecommandees : " + CompetencesRecommandees);
                    System.out.println("EtudeLevel : " + EtudeLevel);
                    System.out.println("City : " + City);
                    System.out.println("------------------------------------------------------");
                }
            }catch (Exception e) {
                System.out.println("the first path scraping error : "+e);
            }

    }
}
