package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;




public class JobScraper {
    public static void main(String[] args) {
        try {
            String url = "https://www.emploi.ma/recherche-jobs-maroc";
            Document mainPage = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                    .get();

            Elements jobLinks = mainPage.select(".card.card-job.featured");

            if (jobLinks.isEmpty()) {
                System.out.println("Aucune annonce trouvée sur cette page.");
                return;
            }

            for (Element job : jobLinks) {
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
                    
                    String secteurActivite1 = getTextFromArrowList(jobPage, "Secteur d'activité");
                    String niveauExperience = getTextFromArrowList(jobPage, "Niveau d'expérience");
                    String niveauEtude = getTextFromArrowList(jobPage, "Niveau d'études");
                    String numPosts = getTextFromArrowList(jobPage, "Nombre de poste(s)");
                    String salaire = getTextFromArrowList(jobPage, "Salaire proposé");
                    String managementEquipe = getTextFromArrowList(jobPage, "Management d'équipe");
                    
                    // Affichage
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
                } catch (Exception e) {
                    System.out.println("Erreur lors du traitement de l'annonce : " + jobUrl);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTextFromArrowList(Document jobPage, String searchText) {
        try {
            // Select all list items in the "arrow-list"
            Elements arrowListItems = jobPage.select("ul.arrow-list li");
            
            // Loop through the list items to find the one containing the search text
            for (Element item : arrowListItems) {
                // Check if the list item contains a span with the specified searchText
                if (item.text().contains(searchText)) {
                    // Find the span and return its text (e.g., for "Secteur d'activité")
                    return item.select("span").text();
                }
            }
            // If no match is found, return "Non spécifié"
            return "Non spécifié";
        } catch (Exception e) {
            return "Erreur";
        }
    }


	// Méthode pour extraire un seul texte
    private static String getText(Document doc, String cssQuery) {
        try {
            Element element = doc.selectFirst(cssQuery);
            return element != null ? element.text() : "Non spécifié";
        } catch (Exception e) {
            return "Erreur";
        }
    }

    // Méthode pour extraire plusieurs valeurs (listes)
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




