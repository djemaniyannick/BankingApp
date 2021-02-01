package com.company.service;

import com.company.Main;
import com.company.domaine.Agence;
import com.company.domaine.Compte;
import com.company.domaine.Listing;
import com.company.exceptions.ProcessorException;
import com.company.traitement.CommandLineService;
import com.company.utils.FileUtils;
import com.company.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Processor {

    private Listing listing = new Listing();

    /**
     * Cette methode permet de creer un compte dans l'agence
     *
     * @param commands
     * @return
     */
    public String createCompte(List<String> commands) {
        Optional<Compte> compte = Main.monAgence.findCompte(commands.get(1));
        if (compte.isPresent()) {
            return "Account " + commands.get(1) + " already exists";
        }
        Compte monCompte = new Compte();
        monCompte.setNumero(commands.get(1));
        Main.monAgence.addCompteToAgence(monCompte);
        return monCompte.toString();
    }

    public LocalDate createDate() {
        return LocalDate.now();
    }

    public String createTimeOfDay() {
        LocalDateTime ld = LocalDateTime.now();
        return ld.getHour() + ":" + ld.getMinute() + ":" + ld.getSecond();
    }

    /**
     * Creer une commande summary qui affiche tous les comptes de l'agence avec les
     * soldes
     */
    public String summary(Agence agence) {
        return agence.listingCompte().stream().collect(Collectors.joining("\n"));
    }

    /**
     * ----- agence ---- numero : c001 nom : xxxxxx adresse : xxxxx ----- comptes
     * ----- xxxxxxxxxxxxxxxxxx -------------------
     */
    public String listesAgence(Agence agence) {
        return "----- agence -----\n" + "numero : " + agence.getNumero() + "\n" + "nom : " + agence.getNom() + "\n"
                + "adresse : " + agence.getAdresse() + "\n" + "----- comptes -----\n" + "" + summary(agence) + "\n"
                + "---------------------";
    }

    /**
     * Cette methode permet de crediter un compte existant
     *
     * @param commands
     * @return
     */
    public String crediter(List<String> commands) {

        Optional<Compte> compte = Main.monAgence.findCompte(commands.get(1));
        if (compte.isPresent()) {
            compte.get().addSolde(new Double(commands.get(2)));
            this.listing.addOperation(commands.get(1), commands.stream().collect(Collectors.joining(" ")));
            return compte.get().toString();
        }
        return "Account " + commands.get(1) + " doesn't exist";
    }

    /**
     * Cette methode permet de debiter un compte existant
     *
     * @param commands
     * @return
     */
    public String debiter(List<String> commands) {
        String message = "";
        try {
            Optional<Compte> compte = Main.monAgence.findCompte(commands.get(1));
            if (compte.isPresent()) {
                compte.get().reduceSolde(new Double(commands.get(2)));
                this.listing.addOperation(commands.get(1), commands.stream().collect(Collectors.joining(" ")));
                return compte.get().toString();
            }
            message = "Account " + commands.get(1) + " doesn't exist";
        } catch (Exception e) {
            message = e.getMessage();
        }
        throw new ProcessorException(message);
    }

    /**
     * virement d'un compte à un autre
     *
     * @param commands
     * @return
     */
    public String virement(List<String> commands) {
        Optional<Compte> compteA = Main.monAgence.findCompte(commands.get(1));
        Optional<Compte> compteB = Main.monAgence.findCompte(commands.get(2));
        if (compteA.isPresent()) {
            if (compteB.isPresent()) {
                boolean result = compteA.get().reduceSolde(new Double(commands.get(3)));
                if (result) {
                    compteB.get().addSolde(new Double(commands.get(3)));
                    this.listing.addOperation(commands.get(1), commands.stream().collect(Collectors.joining(" ")));
                    this.listing.addOperation(commands.get(2), commands.stream().collect(Collectors.joining(" ")));
                    return compteB.get().toString();
                }
                return "Account " + commands.get(1) + " Solde Insuffisant\n";
            }
            return "Account " + commands.get(2) + "  exist\n";
        }
        return "Account " + commands.get(1) + " doesn't exist\n";
    }

    public String listing(List<String> commands) {
        Optional<Compte> compte = Main.monAgence.findCompte(commands.get(1));
        if (compte.isPresent()) {
            List<String> operations = this.listing.getOperation(commands.get(1));
            if (operations == null || operations.isEmpty()) {
                return "Account " + commands.get(1) + " No operation";
            }
            return operations.stream().collect(Collectors.joining("\n"));
        }
        return "Account " + commands.get(1) + " doesn't exist\n";
    }

    /**
     * méthode qui permet d'exécuter le contenu d'un fichier
     * process :
     * - verifie si le fichier avec le nom fileName exist
     * - verifie si le fichier est un fichier txt
     * - verifie si le fichier est vide ou pas
     * - lecture de tout les lignes du fichier
     * - synchronized traitement d'un fichier à la fois
     *
     * @param @fileName
     */
    public String readFile(List<String> commands, CommandLineService commandLineService) {

        File file = new File(commands.get(1));
        if (file.exists()) {
            String extension = getExtensionFile(commands.get(1));
            if (!extension.isEmpty() && extension.equals("txt")) {
                if (file.length() > 0) {
                    try {
                        Stream<String> stream = Files.lines(Paths.get(commands.get(1)));
                        synchronized (stream) {
                            processingLine(stream, commandLineService);
                        }
                        return "file run successfully";
                    } catch (IOException e) {
                        throw new RuntimeException("erreur lors de la lecture de fichier " + e.getMessage());
                    }
                }
                return "empty file content";
            }
            return "Please convert your ." + extension + " File to txt and try again";

        }
        return "File with name " + commands.get(1) + " doesn't exist\n";
    }

    /**
     * retour l'extension d'un fichier
     *
     * @param fileName
     * @return
     */
    public String getExtensionFile(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0)
            return fileName.substring(index + 1);
        return "";
    }

    /**
     * méthode qui traite chaque ligne du fichier
     *
     * @param @command
     * @return
     */
    public void processingLine(Stream<String> stream, CommandLineService commandLineService) {
        stream.filter(c -> !c.isEmpty())
                .forEach(commandLineService::traitementCommand);
    }

    /**
     * Methode qui retourne un compte au format json
     *
     * @param @command
     * @return
     */
    public String exportCompte(List<String> commands) {
        Optional<Compte> compte = Main.monAgence.findCompte(commands.get(1));
        if (!compte.isPresent()) {
            throw new ProcessorException(" compte " + commands.get(1) + "doesn't exist");
        }
        try {
            FileUtils.saveToFile("compte_" + commands.get(1) + ".json", JsonUtils.JsonCompteToString(compte.get()));
        } catch (JsonProcessingException e) {
            throw new ProcessorException("erreur lors du parsing json" + e.getMessage());
        } catch (IOException e) {
            throw new ProcessorException("Erreur de la creation du fichier" + e.getMessage());
        }
        return "exportation du compte avec success";
    }


    public String exportListing(List<String> commands) {

        try {
            FileUtils.saveToFile("listing.json", JsonUtils.JsonListingToString(this.listing));
        } catch (JsonProcessingException e) {
            throw new ProcessorException("erreur lors du parsing json" + e.getMessage());
        } catch (IOException e) {
            throw new ProcessorException("Erreur de la creation du fichier" + e.getMessage());
        }
        return "exportation du listing  avec success";
    }

    public String exportAgence(List<String> commands) {

        try {
            FileUtils.saveToFile("agence.json", JsonUtils.JsonAgenceToString(Main.monAgence));
        } catch (JsonProcessingException e) {
            throw new ProcessorException("erreur lors du parsing json" + e.getMessage());
        } catch (IOException e) {
            throw new ProcessorException("Erreur de la creation du fichier" + e.getMessage());
        }
        return "exportation de l'agence   avec success";
    }

    /**
     * Methode qui permet de convertir de convertir un string json agence  en objet java
     *
     * @param commands
     * @return
     */
    public Agence loadAgence(List<String> commands) {
        Agence agence;
        try {
            String content = new String(Files.readAllBytes(Paths.get("agence.json")));
            agence = JsonUtils.JsonAgenceReader(content);
            Main.monAgence=agence;
        } catch (IOException e) {
            throw new ProcessorException("erreur lors de la convertion du json:" + e.getMessage());
        }
        // return " convertion du fichier agence.json  en object agence  avec success";
        return agence;
    }

    public Listing loadListing(List<String> words) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("listing.json")));
            //listing = JsonUtils.JsonListingReader(content);
            Listing listingLocal=JsonUtils.JsonListingReader(content);
            for(Map.Entry<?,?> entry:listingLocal.getHistory().entrySet()){
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
            this.listing=listingLocal;

        } catch (IOException e) {
            throw new ProcessorException("erreur lors de la convertion du json:" + e.getMessage());
        }
        //return "convertion du fichier listing.json  en object listing  avec success ";
        return listing;
    }

    public String exit(List<String> commands) {
        exportAgence(commands);
        exportListing(commands);

        return "Exportation des fichiers agences et listing avec success";
    }
}
