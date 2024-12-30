package scraper;

import db.Database;
import rmi_api.Annonce;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Sitemanager {

    //database connection
    String url = "jdbc:mysql://localhost:3306/job_insight";
    String user = "root";
    String password = "";
    Database database = new Database(url,user,password);
    private Mjob mjob;

    //tables des annonces



    public List<Annonce> Insert_annonces(List<Annonce> annonces) throws SQLException {

        /*Mjob mjob = new Mjob();
        List<model.Annonce> MajobAnnonces = mjob.mjobscrapping();

        Rekrute_scraper rekruteScraper = new Rekrute_scraper();

        rekruteScraper.ScraperRekrute();

        ArrayList<model.Annonce> RecruteAnnonces = rekruteScraper.getListeAnnonce();

        emploiMA emp = new emploiMA();

        List<model.Annonce> emploiMAnnonces = emp.emploiMAScrapping();*/

        //List<Annonce> annonces = new ArrayList<Annonce>();

        //annonces.addAll(emploiMAnnonces);
        //annonces.addAll(MajobAnnonces);
        //annonces.addAll(RecruteAnnonces);

        System.out.println("Debut d'enregistrement des annonces from mjob website");

        for(Annonce annonce : annonces){
            System.out.println(annonce.toString());
            database.insertAnnonce(annonce);
            System.out.println("---------------------------------------------------- \n");
        }

        return database.getAllAnnonces();
    }

    public List<Annonce> ActualiserScrappingMjob() throws SQLException {

        List<Annonce> annonces = new ArrayList<Annonce>();

        Mjob mjob = new Mjob();
        List<Annonce> MajobAnnonces = mjob.mjobscrapping();

        Insert_annonces(MajobAnnonces);

        return MajobAnnonces;

    }

    public int ProgrammerScrappingMjob(long periode) throws SQLException {

        Mjob mjob = new Mjob();

        mjob.startScheduledScraping(periode);

        int size = mjob.getSize();

        System.out.println("Scrapping Mjob programmé chaque: "+periode);

        return size;

    }

    public List<Annonce> ActualiserScrappingEmploiMA() throws SQLException {

        List<Annonce> annonces = new ArrayList<Annonce>();

        emploiMA emploiMA = new emploiMA();
        List<Annonce> emploiMAAnnonces = emploiMA.emploiMAScrapping();

        Insert_annonces(emploiMAAnnonces);

        return emploiMAAnnonces;

    }

    public int ProgrammerScrappingEmploiMA(long periode) throws SQLException {

        emploiMA emploiMA = new emploiMA();

        emploiMA.startScheduledScraping(periode);

        int size = emploiMA.getSize();

        System.out.println("Scrapping EmploiMA programmé chaque: "+periode);

        return size;


    }

    public List<Annonce> ActualiserScrappingRekrute() throws SQLException {

        List<Annonce> annonces = new ArrayList<Annonce>();

        Rekrute_scraper Rekrute = new Rekrute_scraper();
        Rekrute.ScraperRekrute();
        List<Annonce> RekruteAnnonces = Rekrute.getListeAnnonce();

        Insert_annonces(RekruteAnnonces);

        return RekruteAnnonces;

    }

    public int ProgrammerScrappingRekrute(long periode) throws SQLException {

        Rekrute_scraper Rekrute = new Rekrute_scraper();

        Rekrute.startScheduledScraping(periode);

        int size = Rekrute.getSize();

        System.out.println("Scrapping Rekrute programmé chaque: "+periode);

        return size;

    }

    public Sitemanager() throws SQLException {
    }


}
