package scraper;

import db.Database;
import model.Annonce;

import java.sql.SQLException;
import java.util.List;

public class Sitemanager {

    //database connection
    String url = "jdbc:mysql://localhost:3306/job_insight";
    String user = "root";
    String password = "";
    Database database = new Database(url,user,password);

    //tables des annonces



    public List<Annonce> Insert_mjob_annonces() throws SQLException {

        //Mjob mjob = new Mjob();
        //List<Annonce> annonces = mjob.mjobscrapping();

        emploiMA emp = new emploiMA();

        List<Annonce> annonces = emp.emploiMAScrapping();

        System.out.println("Debut d'enregistrement des annonces from mjob website");

        for(Annonce annonce : annonces){
            System.out.println(annonce.toString());
            database.insertAnnonce(annonce);
            System.out.println("---------------------------------------------------- \n");
        }

        return database.getAllAnnonces();
    }

    public Sitemanager() throws SQLException {
    }


}
