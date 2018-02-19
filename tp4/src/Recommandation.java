import java.time.LocalDate;
import java.util.ArrayList;

/**
 * La classe recommantation
 * 
 * A partir des données comptabilisées avec le fichier JSON entré, la classe recommandation a pour but de
 * batir une liste de recommendation qui sera ajouter sous forme de chaine de caractère à la fin du fichier
 * de sortie.
 */

public class Recommandation {

    // Constantes
    final static String RECOMMAMDE_DATE_REVISION_SALAIRE = "L’écart maximal entre les dates de révision de " +
            "salaire des employés d’un même département devrait être de moins de 6 mois";
    final static String RECOMMAMDE_TAUX_HORAIRE = "Le taux horaire maximum ne devrait pas dépasser deux " +
            "fois le taux horaire minimum.";
    final static  String RECOMMANDE_CHARGE_TRAVAIL_EMPLOYE = "La charge de travail de l'employé %s est " +
            "inférieure à 500 heures";
    final static  String RECOMMANDE_RENTE_FEDERALE = "La rente federale payable par l’entreprise nécessite " +
            "deux versements.";
    final static  String RECOMMANDE_RENTE_PROVINCIALE = "La rente provinciale payable par l’entreprise " +
            "nécessite deux versements.";
    final static  String RECOMMANDE_VALEUR_PAR_EMPLOYE = "L'employé %s est trop coûteux.";
    final static  String RECOMMANDE_VALEUR_TOTALE = "La valeur salariale totale ne devrait pas dépasser " +
            "500 000.00 $.";

    // Attributs
    private ArrayList<String> recommandations;

    // Constructeurs
    public Recommandation() {
        recommandations = new ArrayList<>();
    }

    // Getters
    public ArrayList<String> getRecommandations() {
        return recommandations;
    }

    public boolean nonVide(){
        return !this.recommandations.isEmpty();
    }

    /**
     * Une méthode qui fait appel à une série de méthode pour vérifier quel messages seront ajouter dans la
     * liste de recommandation
     *
     * @param departement:      Le département à évaluer.
     */
    public void donnerRecommandations(Departement departement){

        ArrayList<Employe> employes = departement.getEmployes();

        verifierDateRevisionSalaire(employes);
        verifierChargeTravailEmploye(employes);
        verifierTauxHoraire(departement);
        verifierSalaires(employes);
        verifierRenteFederale(departement);
        verifierRenteProvinciale(departement);
        verifierValeureTotale(departement);
    }

    /**
     * Une méthode qui vérifie si il a moins de six mois entre les dates de révision de salaire entre les
     * employés d'un même département.
     * 
     * @param employes
     */
    private void verifierDateRevisionSalaire(ArrayList<Employe> employes){

        for(Employe employe : employes){

            LocalDate dateRevison = employe.getDateRevision();
            LocalDate dateSixMoisAvant = dateRevison.minusMonths(6);
            LocalDate dateSixMoisApres = dateRevison.plusMonths(6);

            for (Employe autreEmploye : employes){
                LocalDate autreDateRevision = autreEmploye.getDateRevision();
                if (autreDateRevision.isAfter(dateSixMoisApres) || autreDateRevision.isBefore(dateSixMoisAvant)){
                    ajouterRecommandation(RECOMMAMDE_DATE_REVISION_SALAIRE);
                }
            }
        }
    }

    /**
     * Une méthode qui vérifie que la charge de travail de chaque employé n'est pas inférieur à 500 heures.
     * Si tel est le cas, le message de recommandation affichera quel employé est en cause.
     *
     * @param employes
     */
    private void verifierChargeTravailEmploye(ArrayList<Employe> employes){

        for(Employe employe : employes){
            int chargeTravail = employe.getChargeTravail();
            if(chargeTravail < 500){
                String nom = employe.getNom();
                String message = String.format(RECOMMANDE_CHARGE_TRAVAIL_EMPLOYE, nom);
                ajouterRecommandation(message);
            }
        }
    }
    
    /**
     * Une méthode qui vérifie si le taux horaire maximum ne dépasse pas deux fois le taux horaire minimum.
     * 
     * @param departement
     */
    private void verifierTauxHoraire(Departement departement){

        double tauxMin = departement.getTauxHoraireMin();
        double tauxMax = departement.getTauxHoraireMax();

        if( tauxMax > tauxMin * 2){
            ajouterRecommandation(RECOMMAMDE_TAUX_HORAIRE);
        }
    }
    
    /**
     * Une méthode qui vérifie si la valeur d'un employé ne dépasse pas 150000.00$.
     * Si c'est le cas, le message de recommandation affichera quel employé est en cause.
     * 
     * @param employes
     *
     */
    private void verifierSalaires(ArrayList<Employe> employes) {

        for(Employe employe : employes){
            double salaire = employe.getSalaire();
            if(salaire > 150000){
                String nom = employe.getNom();
                String messageRecommandation = String.format(RECOMMANDE_VALEUR_PAR_EMPLOYE, nom);
                ajouterRecommandation(messageRecommandation);
            }
        }
    }
    
    /**
     * Une méthode qui vérifie la rente fédérale. Un message de recommandation sera ajouté a la liste si la
     * rente dépasse 150000.00$
     *
     * @param departement
     */

    private void verifierRenteFederale(Departement departement) {

        double renteFederale = departement.getRenteFederale();
        if (renteFederale > 150000){
            ajouterRecommandation(RECOMMANDE_RENTE_FEDERALE);
        }

    }
    
    /**
     * Une méthode qui vérifie la rente provinciale. Un message de recommandation sera ajouté à la liste si
     * la rente dépasse 75000.00$
     * 
     * @param departement : Le JSON object de sortie.
     */

    private void verifierRenteProvinciale(Departement departement) {

        double renteprovinciale = departement.getRenteProvinciale();
        if (renteprovinciale > 75000){
            ajouterRecommandation(RECOMMANDE_RENTE_PROVINCIALE);
        }

    }
    
    /**
     * Une méthode qui vérifie la valeur salariale totale. Un message de recommandation sera ajouté a la
     * liste si la valeur salariale total dépasse 500 000.00$
     * 
     * @param departement : Le JSON object de sortie.
     */

    private void verifierValeureTotale(Departement departement) {

        double valeurTotale = departement.getValeurTotale();
        if (valeurTotale > 500000){
            ajouterRecommandation(RECOMMANDE_VALEUR_TOTALE);
        }
    }

    /**
     * Une méthode qui ajoute les messages de recommandation à la liste si le message ne s'y trouve pas déjà.
     *
     * @param messageRecommandation
     */
    private void ajouterRecommandation(String messageRecommandation) {

        if (!recommandations.contains(messageRecommandation)){
            recommandations.add(messageRecommandation);
        }
   }
}
