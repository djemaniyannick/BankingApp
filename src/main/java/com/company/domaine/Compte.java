package com.company.domaine;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;

/**
 * Class Compte
 */
public class Compte {

    private String numero;
    private Double solde;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfCreation;

    public Compte(){
        this.solde = 0.0;
        this.dateOfCreation = LocalDate.now();
    }


    public Compte(String numero, Double solde, LocalDate dateOfCreation){
        this.numero = numero;
        this.solde = solde;
        this.dateOfCreation = dateOfCreation;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }
    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setCreationDate(LocalDate dateCreation) {
        this.dateOfCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "compte " + this.numero + "   Solde " + this.solde + "    date " + this.dateOfCreation + "|";
    }

    /**
     * Cette methode permet de crediter le solde du compte
     * @param montant
     */
    public void addSolde(Double montant){

        synchronized (this) {
            if(montant<0){
                System.err.println("amount  doit etre positif");
            }
            this.solde += montant;
            Agence.capital+=montant;
        }
    }
    /**
     * Cette methode permet de debiter le solde du compte
     * @param montant
     */
    public synchronized boolean reduceSolde(Double montant){
        if(montant >=Agence.capital){
            System.err.println("capital insuffisant, try again later ");
            return false;
        }
        else if ((this.solde>montant)){
            this.solde -= montant;
            Agence.capital-=montant;
            return true;
        }
        else{
            System.err.println("Solde Insuffisant\n");
            return false;
        }
    }
}
