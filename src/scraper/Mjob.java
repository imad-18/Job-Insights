package scraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Mjob {

    String mjob = "https://www.m-job.ma/recherche";

    public void mjobscrapping(){
        try {
            Document document = Jsoup.connect(mjob).get();
            Elements jobItems = document.select("div.offer-box");


            for (Element jobItem : jobItems) {
                Element titleElement = jobItem.selectFirst("h3.offer-title");
                if (titleElement != null) {
                    String title = titleElement.text();
                    System.out.println("Title: "+title);
                }else {
                    System.out.println("No title found");
                }

                Element locationElement = jobItem.selectFirst(".location");
                if (locationElement != null) {
                    String location = locationElement.text();
                    System.out.println("Location: "+location);
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
                    String publishDate = publishDateElement.text();
                    System.out.println("Date de publication: " + publishDate);
                }

                Element voirplusElement = jobItem.selectFirst("div.form-group > a");
                if (voirplusElement != null) {
                    // Get the href attribute value
                    String hrefValue = voirplusElement.attr("href");
                    System.out.println("Niiice: " +hrefValue);

                    Document document1 = Jsoup.connect(hrefValue).get();

                    Element listDetails = document1.selectFirst("ul.list-details");

                    assert listDetails != null;
                    Element companyElement = listDetails.select("li > h3").first();
                    String company = companyElement.text();
                    System.out.println("Société: " + company);

                    Element contractTypeElement = listDetails.select("li > h3").get(1);
                    String contractType = contractTypeElement.text();
                    System.out.println("Contract type: " + contractType);

                    Element salaryElement = listDetails.select("li > h3").get(2);
                    String salary = salaryElement.text();
                    System.out.println("Salary: " + salary);


                    // Select parent div with class 'the-content'
                    Element otherInfos = document1.selectFirst("div.the-content");



                    // Safely check if the parent div exists
                    if (otherInfos != null) {
                        // Select all <div> elements within the parent
                        Elements divElements = otherInfos.select("div");

                        //first div containing company desc + avantages sociaux
                        Element companyDescription = divElements.get(0);
                        // Select all <p> elements inside the sub-div
                        Elements paragraphs = companyDescription.select("p");

                        //DEScription d'entreprise
                        String companyDescriptionText = paragraphs.get(0).text();
                        System.out.println("Company description: " + companyDescriptionText);


                        // Iterate through each <p> element and print its text
                        System.out.println("Description du poste:");
                        for (int i = 1; i < 3; i++) {
                            String paragraphText = paragraphs.get(i).text();
                            System.out.println( paragraphText);
                        }

                        String avantageSociaux = paragraphs.get(4).text();
                        System.out.println("Avantage sociaux: " + avantageSociaux);


                        //2nd div containing searched profile
                        Element requiredProfile = divElements.get(3);
                        Elements paragraphs2 = requiredProfile.select("p");
                        System.out.println("Profile recherché: ");
                        for (int i = 0; i < paragraphs2.size(); i++) {
                            String paragraphText = paragraphs2.get(i).text();
                            System.out.println(paragraphText);
                        }

                        Element activityAreaElement = divElements.get(4); // Index starts from 0
                        String activityArea = activityAreaElement.text();
                        System.out.println("ActivityArea: " + activityArea);

                        Element metierElement = divElements.get(5);
                        String metier = metierElement.text();
                        System.out.println("Metier: " + metier);

                        Element experienceYearsElement = divElements.get(6);
                        String experienceYears = experienceYearsElement.text();
                        System.out.println("ExperienceYears: " + experienceYears);

                        Element requiredStudyYearsElement = divElements.get(7);
                        String requiredStudyYears = requiredStudyYearsElement.text();
                        System.out.println("Niveau d'études exigé: " + requiredStudyYears);

                        Element requiredLanguagesElement = divElements.get(8);
                        String requiredLanguages = requiredLanguagesElement.text();
                        System.out.println("Langues: " + requiredLanguages);

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
    }
}