package app;

import data_analyse.NLPProcessor;
import db.Database;
import org.apache.commons.lang3.tuple.Pair;
import rmi_api.Annonce;
import rmi_api.BodyResponse;
import rmi_api.ResponseRMI;
import rmi_api.ServicesAPI;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;



public class Services implements ServicesAPI {

    String url = "jdbc:mysql://localhost:3306/job_insight";
    String user = "root";
    String password = "";
    Database database = new Database(url,user,password);
    ResponseRMI response = new ResponseRMI();
    BodyResponse body = new BodyResponse();

    public Services() throws SQLException {
    }

    @Override
    public ResponseRMI processUserQuery(String userQuery) throws RemoteException, SQLException {
        //Npl
        //
        NLPProcessor nlp = new NLPProcessor();

        String[] tokens = userQuery.split("\\s+");

        nlp.identifyIntent(tokens);

        Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> DataResponse = nlp.QuestionResponse();

        response.body.chatbootResp = DataResponse.getLeft();

        response.body.annonceList = DataResponse.getRight().getLeft();

        response.body.Statistics = DataResponse.getRight().getRight() ;

        return response ;
    }

    @Override
    public ResponseRMI getAllAnnonces() throws RemoteException, SQLException {

        System.out.println("Le client accéde à la méthode getAllAnnonces");
        response.statuscode = "200";
        System.out.println("response.statuscode = 200");
        body.annonceList = database.getAllAnnonces();
        System.out.println("response importer de base données avec succées"+body.annonceList.toString());
        response.body = body;
        System.out.println("response importer de base données avec succées");

        System.out.println("Response to send: " + response);
        return response;
    }

    @Override
    public void submitRating(int i) {
        System.out.println("connected Submit");

        database.insert(i);

        // Print the updated list
        System.out.println(i);
    }


}
