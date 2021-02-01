package com.company.traitement;

import com.company.Main;
import com.company.exceptions.CommandLineException;
import com.company.service.Processor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La class CommandLineService Service d'interpretation d'une line de commande
 */
public class CommandLineService {

	private Processor processor = new Processor();

	/**
	 * Cette method reçoit les commandes saisis à l'interpreteur de command et
	 * affiche
	 *
	 * @param command: C'est un ou plusieurs mots correspondant à des commandes
	 *                 reçues pour le systeme
	 * @throws IOException 
	 */
	public  void traitementCommand(String command) {

		String output;

		// Splitter une create c002;
		command = command.trim();
		List<String> words = Arrays.asList(command.split(" "));
		words = words.stream().filter(w -> !w.isEmpty()).collect(Collectors.toList());
		switch (words.get(0).toLowerCase()) {
		case "date":
			output = this.valideDate(words);
			// System.out.println(LocalDate.now());
			if (output.isEmpty()) {
				output = "" + this.processor.createDate();
			}
			break;
		case "time":
			output = this.valideTime(words);
			// System.out.println(LocalDateTime.now());
			if (output.isEmpty()) {
				output = "" + this.processor.createTimeOfDay();
			}
			break;
		case "create":
			output = this.valideCreate(words);
			if (output.isEmpty()) {
				output = "" + this.processor.createCompte(words);
			}
			break;
		case "summary":
			output = this.valideSummary(words);
			if (output.isEmpty()) {
				output = "" + this.processor.summary(Main.monAgence);
			}
			break;
		case "agence":
			output = this.valideAgence(words);
			if (output.isEmpty()) {
				output = "" + this.processor.listesAgence(Main.monAgence);
			}
			break;
		case "crediter":
			output = this.valideCrediter(words);
			if (output.isEmpty()) {
				output = "" + this.processor.crediter(words);
			}
			break;
		case "debiter":
			output = this.valideDebiter(words);
			if (output.isEmpty()) {
				output = "" + this.processor.debiter(words);
			}
			break;
		case "virer":
			output = this.valideVirement(words);
			if (output.isEmpty()) {
				output = "" + this.processor.virement(words);

			}
			break;
		case "listing":
			output = this.valideListing(words);
			if (output.isEmpty()) {
				output = "" + this.processor.listing(words);
			}
			break;
			case "export-compte":
				output = this.valideExportCompte(words);
				if (output.isEmpty()) {
					this.processor.exportCompte(words);
					output = "" + this.processor.exportCompte(words);

				}
				break;
			case "export-listing":
				output = this.valideExportListing(words);
				if (output.isEmpty()) {
					this.processor.exportListing(words);
					output = "" + this.processor.exportListing(words);

				}
				break;

			case "export-agence":
				output = this.valideExportListing(words);
				if (output.isEmpty()) {
					this.processor.exportAgence(words);
					output = "" + this.processor.exportAgence(words);

				}
				break;

			case "load":
			output = this.valideListing(words);
			if (output.isEmpty()) {
				output = "" + this.processor.readFile(words, this);

			}
			break;
			case "exit":
				output = this.valideExit(words);
				if (output.isEmpty()){
					output = "" + this.processor.exit(words);
				}
			break;
			case "load-agence":
				output = this.valideLoadAgence(words);
				if (output.isEmpty()) {
					//this.processor.loadAgence(words);
					output = "" + this.processor.loadAgence(words);

				}
				break;

			case "load-listing":
				output = this.valideLoadAgence(words);
				if (output.isEmpty()) {
					//this.processor.loadListing(words);
					output = "" + this.processor.loadListing(words);
				}
				break;

		default:
			// System.out.println(command + " Command not found");
			output = command + " Command not found";
			break;
		}
		System.out.println(output);
		return;
	}

	private String valideVirement(List<String> words) {
		if (words.size() == 4) {
			return "";
		} else {
			return "Syntax error, required 3 parameters in format |virer numeroCompteA numeroCompteB montant|";
		}
		// return (words.size()==2)? "Syntax error, required 2 parameter" : "";
	}

	private String valideCrediter(List<String> words) {
		if (words.size() == 3) {
			return "";
		} else {
			return "Syntax error, required 2 parameter in format |Crediter NumeroCompte Montant|";
		}
		// return (words.size()==2)? "Syntax error, required 2 parameter" : "";
	}

	private String valideDebiter(List<String> words) {
		String message="";
		try {
			if (words.size() == 3) {
				return message;
			} else {
				message= "Syntax error, required 2 parameter in format |Debiter NumeroCompte Montant|";
			}
		} catch (Exception e) {
			message=e.getMessage();
		}
		throw new CommandLineException(message);
		
	}

	private String valideDate(List<String> words) {
		return (words.size() > 1) ? "Syntax error, no parameter expected" : "";
	}

	private String valideAgence(List<String> words) {
		return (words.size() > 1) ? "Syntax error, no parameter expected" : "";
	}

	private String valideListing(List<String> words) {
		return (words.size() == 2) ? "" : "Syntax error, Expected 1 parameter";
	}

	private String valideTime(List<String> words) {
		return (words.size() > 1) ? "Syntax error, no parameter expected" : "";
	}

	private String valideCreate(List<String> words) {
		if (words.size() == 2) {
			return "";
		} else {
			return "Syntax error, Expected 1 parameter";
		}
		// return (words.size()==2)? "Syntax error, Expected 1 parameter" : "";
	}

	private String valideExportCompte(List<String> words) {
		if (words.size() == 2) {
			return "";
		} else {
			return "Syntax error, Expected 1 parameter";
		}
		// return (words.size()==2)? "Syntax error, Expected 1 parameter" : "";
	}

	private String valideExportListing(List<String> words) {
		if (words.size() ==1) {
			return "";
		} else {
			return "Syntax error, no  parameter expected ";
		}
		// return (words.size()==2)? "Syntax error, Expected 1 parameter" : "";
	}

	private String valideLoadAgence(List<String> words) {
		if (words.size() ==1) {
			return "";
		} else {
			return "Syntax error, no  parameter expected ";
		}
		// return (words.size()==2)? "Syntax error, Expected 1 parameter" : "";
	}

	private String valideSummary(List<String> words) {
		return (words.size() > 1) ? "Syntax error, no parameter expected" : "";
	}

	private String valideExit(List<String> words) {
		return (words.size() == 1) ? "" : "Syntax error, no parameter required";
	}

}
