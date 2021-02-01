package com.company;

import com.company.domaine.Agence;
import com.company.exceptions.CommandLineException;
import com.company.exceptions.ProcessorException;
import com.company.traitement.CommandLineService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public class Main { 

    public static Agence monAgence = new Agence("001", "agence", "BP 01", new HashSet<>());

    public static void main(String[] args) throws IOException {

	// write your code here
        System.out.println(" Banking App");
        CommandLineService interpreteur = new CommandLineService();

        Scanner sc = new Scanner(System.in);
        String s;
        do{
            s = sc.nextLine();
            	try {
            		interpreteur.traitementCommand(s);
                    if(s.equalsIgnoreCase("Exit")) {
                        break;
                    }
				} catch (CommandLineException e) {
					System.err.println("Erreur lors de l'analyse de la commande "+e.getMessage());
				} catch (ProcessorException e) {
					System.err.println("Erreur lors de l'exécution  de la commande "+e.getMessage());
				} catch (Exception e) {
					System.err.println("Fatal Error "+e.getMessage());
				}

        } while (true);
    }

}
