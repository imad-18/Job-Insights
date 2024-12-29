package data_analyse;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import model.Annonce;
import org.apache.commons.lang3.tuple.Pair;

public class BackendServiceImpl extends UnicastRemoteObject implements BackendService {

    // Constructor
    public BackendServiceImpl() throws RemoteException {
        super();
    }
    @Override
    public Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> FrontResponse(String req) throws RemoteException {
        String question = req;
        NLPProcessor nlp = new NLPProcessor();
        String[] tokens = nlp.tokenizeQuery(question);
        nlp.identifyIntent(tokens);
        Pair<String , Pair<ArrayList<Annonce>, ArrayList<Pair<String, Integer>>>> DataResponse = nlp.QuestionResponse();
        return DataResponse;
    }
}

