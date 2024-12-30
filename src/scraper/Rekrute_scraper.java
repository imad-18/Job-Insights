package scraper;

import app.Dashboard;
import rmi_api.Annonce;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Rekrute_scraper {

    private ArrayList<Annonce> listeAnnonce = new ArrayList<Annonce>();
    private int i=0;
    private String url = "jdbc:mysql://localhost:3306/job_insight";
    private String user = "root";
    private String password = "";
    private Connection connection = DriverManager.getConnection(url, user, password);



    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Rekrute_scraper() throws SQLException {
    }

    int size ;
    public  int getSize() {
        return size;
    }
    public void startScheduledScraping(long period) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println("Début du scrapping...");
            ScraperRekrute();
            List<Annonce> nouvellesAnnonces = getListeAnnonce();
            System.out.println("Nouvelles annonces récupérées : " + nouvellesAnnonces.size());
            size = nouvellesAnnonces.size();
            Dashboard.moyenneperjourRekrute = size;
            sauvegarderAnnonces(nouvellesAnnonces);
        }, 0, period, TimeUnit.HOURS);
    }
    public void ScraperRekrute() {
        boolean stop = false;

        for (int j=1;j<27;j++) {

            if(stop)
                break;

            try {
                String url = "https://www.rekrute.com/offres.html?p="+j+"&s=3&o=1";
                Document document = Jsoup.connect(url).get();

                Elements jobItems = document.select("ul.job-list.job-list2#post-data > li");
                for (Element item : jobItems) {
                    //traitement de la page initiale


                    String jobTitleAndLocation [] = item.select("h2 a.titreJob").text().split("\\|");
                    String jobTitle = jobTitleAndLocation[0];
                    String jobLocation = jobTitleAndLocation[1];
                    String jobLocation1 = item.selectFirst("i.fa.fa-industry").parent().selectFirst("span").text();
                    String moroccanCitiesRegex = "\\b(Casablanca|Rabat|Fès|Fez|Marrakech|Meknès|Meknes|Tétouan|Tetouan|Tanger|Tangier|Agadir|Oujda|Kénitra|Kenitra|Safi|El Jadida|Beni Mellal|Nador|Taza|Mohammedia|Khouribga|Settat|Larache|Ksar El Kebir|Essaouira|Al Hoceima|Inezgane|Taroudant|Berkane|Sidi Slimane|Sidi Kacem|Errachidia|Ouarzazate|Guelmim|Tan-Tan|Laâyoune|Laayoune|Dakhla|Chefchaouen|Midelt|Azrou|Ifrane|Tiznit|Zagora|Youssoufia|Sefrou|Boujdour)\\b";
                    Pattern pattern = Pattern.compile(moroccanCitiesRegex, Pattern.CASE_INSENSITIVE);
                    Matcher matcher = pattern.matcher(jobLocation);
                    String jobLocationF = "";
                    while (matcher.find()) {
                        jobLocationF = matcher.group();
                        break;
                    }
                    String jobDescription = item.selectFirst("i.fa.fa-binoculars").parent().selectFirst("span").text();
                    String jobDate = item.select("em.date span").text();
                    String Date[] = jobDate.split(" ");
                    String jobDateS="",jobDateE="";
                    int jobN=0;
                    if(Date.length == 3) {
                        jobDateS = Date[0];
                        jobDateE = Date[1];
                        jobN = Integer.valueOf(Date[2]);
                    }
                    if(Date.length == 2) {
                        jobDateS = Date[0];
                        jobDateE = Date[1];
                        jobN = 0;
                    }
                    if(Date.length == 1 || Date.length == 0) {
                        jobDateS = "Non Specifie";
                        jobDateE = "Non Specifie";
                        jobN = 0;
                    }
                    Elements Info = item.select("div ul > li");
                    String phase1 [] = Info.get(0).text().split(":");
                    String phase2 [] = Info.get(1).text().split(":");
                    String phase3 [] = Info.get(2).text().split(":");
                    String phase4 [] = Info.get(3).text().split(":");
                    String phase5 [] = Info.get(4).text().split(":");
                    String Secteur = phase1 [1].replace("  ", "/").replace("-", "/");
                    String Fonction = phase2 [1].replace("  ", "/").replace("-", "/").replace(",", "/");
                    String Experience = phase3 [1].replace("De", "").replace("ans", "").replace("-", "/").replace("et", "/").replace("à", "-").replace(" ", "");
                    String NiveauEtude = phase4 [1].replace(" ", "").toUpperCase();
                    String Contract[] = phase5 [1].split("-");
                    String ContratDetails = Contract[0];
                    String Teletravail = phase5 [2];
                    String urlSite = "https://www.rekrute.com"+item.selectFirst("div div.col-sm-2 div a").attr("href");
                    String SiteName = "rekrute";
                    String SiteWebEntreprise = "none";
                    String nomEntreprise = "https://www.rekrute.com"+item.selectFirst("div div.col-sm-2 a img").attr("title");
                    //scraping de la sous page
                    if (!isAnnonceDejaScrapee(urlSite)) {

                        Document document2 = Jsoup.connect(urlSite).get();
                        Elements divs = document2.select("div.contentbloc div.col-md-12");
                        String adresseEntreprise = divs.get(7).select("span").text();
                        String descriptionEntreprise = divs.get(4).select("p").text();
                        String City = jobLocationF;
                        String Region = "";
                        List<String> CasablancaSettat = Arrays.asList("Casablanca ", "Mohammedia", "Settat", "El Jadida", "Benslimane", "Nouaceur");
                        List<String> RabatSaléKénitra = Arrays.asList("Rabat", "Salé", "Kénitra", "Témara", "Skhirat", "Sidi Kacem");
                        List<String> MarrakechSafi = Arrays.asList("Marrakech", "Safi", "Al Haouz", "Chichaoua", "El Kelaâ des Sraghna");
                        List<String> FèsMeknès = Arrays.asList("Fès", "Meknès", "Ifrane", "Taza", "Moulay Yacoub", "Sefrou");
                        List<String> TangerTétouanAlHoceïma = Arrays.asList("Tanger", "Tétouan", "Al Hoceïma", "Chefchaouen", "Larache", "M'diq");
                        List<String> Oriental = Arrays.asList("Oujda", "Nador", "Berkane", "Driouch", "Jerada", "Taourirt");

                        List<String> SoussMassa = Arrays.asList("Agadir ", "Taroudant", "Inezgane", "Tiznit", "Chtouka Ait Baha");
                        List<String> DrâaTafilalet = Arrays.asList("Ouarzazate", "Errachidia", "Zagora", "Tinghir", "Rissani", "Midelt");
                        List<String> GuelmimOuedNoun = Arrays.asList("Guelmim", "Tan-Tan", "Assa-Zag", "Sidi Ifni");
                        List<String> LaâyouneSakiaElHamra = Arrays.asList("Laâyoune", "Smara", "Boujdour", "Tarfaya");
                        List<String> DakhlaOuedEdDahab = Arrays.asList("Dakhla", "Oued Ed-Dahab");
                        List<String> BenGuerir = Arrays.asList("Ben Guerir", "Tata", "Ouled Teima", "Tamegroute");

                        if (CasablancaSettat.contains(City)) {
                            Region = "Casablanca-Settat";
                        }
                        if (RabatSaléKénitra.contains(City)) {
                            Region = "Rabat-Salé-Kénitra";
                        }
                        if (MarrakechSafi.contains(City)) {
                            Region = "Marrakech-Safi";
                        }
                        if (FèsMeknès.contains(City)) {
                            Region = "Fès-Meknès";
                        }
                        if (TangerTétouanAlHoceïma.contains(City)) {
                            Region = "Tanger-Tétouan-Al Hoceïma";
                        }
                        if (Oriental.contains(City)) {
                            Region = "Oriental";
                        }
                        if (SoussMassa.contains(City)) {
                            Region = "Souss-Massa";
                        }
                        if (DrâaTafilalet.contains(City)) {
                            Region = "Drâa-Tafilalet";
                        }
                        if (GuelmimOuedNoun.contains(City)) {
                            Region = "Guelmim-Oued Noun";
                        }
                        if (LaâyouneSakiaElHamra.contains(City)) {
                            Region = "Laâyoune-Sakia El Hamra";
                        }
                        if (DakhlaOuedEdDahab.contains(City)) {
                            Region = "Dakhla-Oued Ed-Dahab";
                        }
                        if (BenGuerir.contains(City)) {
                            Region = "Ben Guerir";
                        }

                        String Industry = Secteur;
                        Elements spans = divs.get(8).select("p span");
                        String TraitsPersonnalite = "[" + spans.stream()
                                .map(Element::text)
                                .collect(Collectors.joining(", ")) + "]";
                        String competencesRequises = "";
                        try {
                            competencesRequises = "[" + divs.get(6).selectFirst("ul li").text() + "]";
                        } catch (Exception a) {
                            competencesRequises = "";
                        }

                        String SoftSkills = TraitsPersonnalite;
                        String CompetencesRecommandees = competencesRequises;
                        String Langue = "français";
                        String NiveauLangue = "";
                        String Salaire = "5000-9000";
                        String AvantagesSociaux = "[ ]";
                        i++;
                        Annonce annonce = new Annonce(i, jobTitle, jobDescription, jobDateS,
                                jobDateE, jobN, Secteur, Fonction, Experience, NiveauEtude,
                                ContratDetails, urlSite, SiteName, adresseEntreprise,
                                SiteWebEntreprise, nomEntreprise, descriptionEntreprise,
                                Region, City, Industry, TraitsPersonnalite, competencesRequises,
                                SoftSkills, CompetencesRecommandees, Langue, NiveauLangue,
                                Salaire, AvantagesSociaux, Teletravail);

                        listeAnnonce.add(annonce);
                    }else {
                        stop = true;
                        break;
                    }
                }
            }catch (Exception e) {
            }
        }
    }

    public ArrayList<Annonce> getListeAnnonce() {
        return listeAnnonce;
    }

    public void afficheAnnonce() {
        for (Annonce item : listeAnnonce) {
            System.out.println("---------------------------------------Offre d emploi-------------------------------");
            System.out.println("Job Title: " + item.getTitle());
            System.out.println("Job Location: " + item.getCity());
            System.out.println("Job Description: " + item.getDescription());
            System.out.println("Job Offer starts : " +item.getStartDate() + " Ends in : " + item.getEndDate());
            System.out.println("le nombre de postes : " + item.getPostsNum());
            System.out.println("Job Secteur: " + item.getSecteur());
            System.out.println("Job Fonction: " + item.getFonction());
            System.out.println("Job Experience required : " + item.getExperience());
            System.out.println("Job Niveau etude demande : " + item.getEtudeLevel());


        }
        System.out.println("Le nombre d annonce scraper : "+listeAnnonce.size());
    }

    private boolean isAnnonceDejaScrapee(String jobUrl) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM annonce WHERE url = ?")) {
            stmt.setString(1, jobUrl);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sauvegarderAnnonces(List<Annonce> annonces) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO annonce (url, title, description) VALUES (?, ?, ?)")) {
            for (Annonce annonce : annonces) {
                stmt.setString(1, annonce.getUrl());
                stmt.setString(2, annonce.getTitle());
                stmt.setString(3, annonce.getDescription());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
