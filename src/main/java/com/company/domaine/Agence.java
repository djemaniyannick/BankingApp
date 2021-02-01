package com.company.domaine;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * class Agence
 */
public class Agence {
    public static double capital= 1000000;
    private String numero;
    private String nom;
    private String adresse;
    private Set<Compte> comptes;

    public Agence (){
        this.comptes = new HashSet<>();
    }

    public Agence(String numero, String nom, String adresse, Set<Compte> comptes){
        this.numero = numero;
        this.nom = nom;
        this.adresse = adresse;
        this.comptes = comptes;

    }


    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(Set<Compte> comptes) {
        this.comptes = comptes;
    }

    public double getCapital() {
        return capital;
    }

    public void setCapital(double capital) {
        this.capital = capital;
    }

			
	@Override
	public String toString() {
		return "Agence [numero=" + numero + ", nom=" + nom + ", adresse=" + adresse + ", comptes=" + comptes + "]";
	}

	/**
     * methode qui ajoute un compte dans une agence
     *
     * @param compte
     * @return Set<Compte> qui represente la liste des comptes mise a jour.
     */
    public Set<Compte> addCompteToAgence(Compte compte){
        this.comptes.add(compte);
        return this.comptes;
    }

    /**
     * Cette methode va transformer des comptes en the chaine de caracteres pour les afficher
     */
    public List<String> listingCompte(){
        return this.comptes.stream().map(Compte::toString).collect(Collectors.toList());
    }

    public  String  listingAgence(){
        return  this.toString();

    }

    public Optional<Compte> findCompte(String numeroCompte){
        return this.comptes.stream().filter(x -> x.getNumero().equalsIgnoreCase(numeroCompte)).findFirst();
    }

}
