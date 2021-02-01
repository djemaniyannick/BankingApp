package com.company.utils;
import com.company.domaine.Agence;
import com.company.domaine.Compte;
import com.company.domaine.Listing;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class JsonUtils {
    public static String JsonCompteToString(Compte compte) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(compte);
    }

    public static String JsonListingToString(Listing listing) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(listing);
    }

    public static String JsonAgenceToString(Agence agence) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(agence);
    }

    public  static Agence JsonAgenceReader(String json) throws IOException {
       return  new ObjectMapper().readValue(json,Agence.class);
    }

    public static Listing JsonListingReader(String json) throws JsonProcessingException {
        return  new ObjectMapper().readValue(json, Listing.class);
    }
}
