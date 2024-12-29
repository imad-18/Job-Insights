package scraper;

import model.Annonce; // Assurez-vous que la classe Annonce est bien définie dans ce package
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class emploiMA {

    public List<Annonce> emploiMAScrapping() {
        List<Annonce> annonces = new ArrayList<>();
        int id = 0 ;

        String url = "https://www.emploi.ma/recherche-jobs-maroc";


        String PageNumber = "24";
        try {

            Document mainPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                    .get();

            Element lastPageElement = mainPage.selectFirst("a[title='Aller à la dernière page']");

            if (lastPageElement != null) {
                // Récupérer le numéro de la dernière page
                PageNumber = lastPageElement.text();
                System.out.println("Nombre de page : " + PageNumber);

            }

            for (int j=0; j<Integer.parseInt(PageNumber) ; j++){

                System.out.println("Page:"+(j+1));
                if (j != 0) {
                    url = "https://www.emploi.ma/recherche-jobs-maroc?page="+j;
                    System.out.println(url);

                }
                mainPage = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                        .get();

                Elements jobLinks = mainPage.select(".card.card-job");

            if (jobLinks.isEmpty()) {
                System.out.println("Aucune annonce trouvée sur cette page.");
                return annonces;
            }

            for (Element job : jobLinks) {
                id++;
                System.out.println("ID : " + id);

                String jobUrl = job.attr("data-href");

                try {
                    Document jobPage = Jsoup.connect(jobUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                            .get();

                    // Extraction sécurisée des données
                    String title = getText(jobPage, "h1.text-center");
                    String startDate = getText(jobPage, ".page-application-details p");
                    String entrepriseNom = getText(jobPage, ".card-block-company h3 a");
                    String secteur = extractMultiValues(jobPage, ".field-name-field-entreprise-secteur .field-item");
                    String entrepriseDescription = getText(jobPage, ".company-description .truncated-text");
                    String fonction = getText(jobPage, "section .job-title");
                    String description = getText(jobPage, ".job-description p");
                    String mission = extractMultiValues(jobPage, ".job-description ul li");
                    String profilRecherche = getText(jobPage, "section .job-qualifications");
                    String metier = getText(jobPage, "ul.arrow-list li:contains(Métier) span");
                    String contratDetails = getText(jobPage, "ul.arrow-list li:contains(Type de contrat) span");
                    String region = getText(jobPage, "ul.arrow-list li:contains(Région) span");
                    String ville = getText(jobPage, "ul.arrow-list li:contains(Ville) span");
                    String teletravail = getText(jobPage, "ul.arrow-list li:contains(Travail à distance) span");
                    String langue = getText(jobPage, "ul.arrow-list li:contains(Langues exigées) span");
                    String niveauExperience = getTextFromArrowList(jobPage, "Niveau d'expérience");
                    String niveauEtude = getTextFromArrowList(jobPage, "Niveau d'études");
                    String numPosts = getTextFromArrowList(jobPage, "Nombre de poste(s)");
                    String salaire = getTextFromArrowList(jobPage, "Salaire proposé");
                    String managementEquipe = getTextFromArrowList(jobPage, "Management d'équipe");

                    // Création d'une instance d'Annonce
                    Annonce annonce = pretraitement(title, startDate,jobUrl, entrepriseNom, secteur, entrepriseDescription,
                            fonction, description, mission, profilRecherche, metier, contratDetails, region, ville,
                            teletravail, langue, niveauExperience, niveauEtude, numPosts, salaire, managementEquipe);

                    annonces.add(annonce);

                } catch (Exception e) {
                    System.out.println("Erreur lors du traitement de l'annonce : " + jobUrl);
                    e.printStackTrace();
                }
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return annonces;
    }

    private Annonce pretraitement(String title, String startDate,String jobUrl, String entrepriseNom, String secteur,
                                  String entrepriseDescription, String fonction, String description, String mission,
                                  String profilRecherche, String metier, String contratDetails, String region,
                                  String ville, String teletravail, String langue, String niveauExperience,
                                  String niveauEtude, String numPosts, String salaire, String managementEquipe) {


        System.out.println("=======================================");
        System.out.println("Title: " + title);
        System.out.println("Start Date: " + startDate);
        System.out.println("Entreprise Nom: " + entrepriseNom);
        System.out.println("Secteur: " + secteur);
        System.out.println("Entreprise Description: " + entrepriseDescription);
        System.out.println("Fonction: " + fonction);
        System.out.println("Description: " + description);
        System.out.println("Missions:\n" + mission);
        System.out.println("Profil Recherché: " + profilRecherche);
        System.out.println("Métier: " + metier);
        System.out.println("Contrat Details: " + contratDetails);
        System.out.println("Région: " + region);
        System.out.println("Ville: " + ville);
        System.out.println("Télétravail: " + teletravail);
        System.out.println("Langues Exigées: " + langue);
        System.out.println("Niveau d'expérience: " + niveauExperience);
        System.out.println("Niveau d'études: " + niveauEtude);
        System.out.println("Nombre de postes: " + numPosts);
        System.out.println("Salaire: " + salaire);
        System.out.println("Management d'équipe: " + managementEquipe);
        System.out.println("=======================================\n");

        Annonce annonce = new Annonce();
        annonce.setTitle(title);
        annonce.setDescription(description);
        annonce.setStartDate(PretraitementemploiMA.regulerDate(startDate));
        annonce.setEndDate("");
        annonce.setPostsNum(Integer.parseInt(numPosts));
        annonce.setSecteur(PretraitementemploiMA.formatSecteur(secteur));
        annonce.setFonction(PretraitementMjob.extracting_fonction(fonction , metier));///   Meme que Job MA----------//-----json
        annonce.setExperience(PretraitementemploiMA.formatExperience(niveauExperience));
        annonce.setEtudeLevel(PretraitementemploiMA.transformTextEtudeLevel(niveauEtude));
        annonce.setContratDetails(contratDetails);
        annonce.setUrl(jobUrl);
        annonce.setSiteName("emploi.ma");
        annonce.setAdresseEntreprise(ville);
        annonce.setSiteWebEntreprise("");//-----------//
        annonce.setNomEntreprise(entrepriseNom);
        annonce.setDescriptionEntreprise(entrepriseDescription);
        annonce.setRegion(region);
        annonce.setCity(ville);
        annonce.setIndustry(metier);
        annonce.setTraitsPersonnalite(PretraitementMjob.parseTexts(description,profilRecherche,mission).get("Personality Traits").toString());//-----
        annonce.setCompetencesRequises(PretraitementMjob.parseTexts(description,profilRecherche,mission).get("Competencies").toString());//-----
        annonce.setSoftSkills(PretraitementMjob.parseTexts(description,profilRecherche,mission).get("Soft Skills").toString());//--------
        annonce.setCompetencesRecommandees(PretraitementMjob.parseTexts(description,profilRecherche,mission).get("Competencies").toString());
        annonce.setLangue(PretraitementemploiMA.parseLanguageLevel(langue).get("langue"));
        annonce.setNiveauLangue(PretraitementemploiMA.parseLanguageLevel(langue).get("niveau"));
        annonce.setSalaire(PretraitementemploiMA.formatRangeSalaire(salaire));
        annonce.setAvantagesSociaux(PretraitementMjob.parseTexts(description,profilRecherche,mission).get("Social Benefits").toString());//---------
        annonce.setTeletravail(teletravail);


        return annonce ;
    }

    private static String getTextFromArrowList(Document jobPage, String searchText) {
        try {
            Elements arrowListItems = jobPage.select("ul.arrow-list li");
            for (Element item : arrowListItems) {
                if (item.text().contains(searchText)) {
                    return item.select("span").text();
                }
            }
            return "Non spécifié";
        } catch (Exception e) {
            return "Erreur";
        }
    }

    private static String getText(Document doc, String cssQuery) {
        try {
            Element element = doc.selectFirst(cssQuery);
            return element != null ? element.text() : "Non spécifié";
        } catch (Exception e) {
            return "Erreur";
        }
    }

    private static String extractMultiValues(Document doc, String cssQuery) {
        try {
            Elements elements = doc.select(cssQuery);
            StringBuilder builder = new StringBuilder();
            for (Element el : elements) {
                builder.append("- ").append(el.text()).append("\n");
            }
            return builder.toString().trim();
        } catch (Exception e) {
            return "Erreur";
        }
    }


}
