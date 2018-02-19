
import net.sf.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
* CLASSE EMPLOYE
*
* Créer un employe è partir d'un objet JSON récupéré et validé. Cet employé appartient au département
 * lui-même créé à partir du fichier JSON. Une fois qu'une instance Employe est créée, on peut calculer son
 * salaire.
*/

public class Employe{

    // Constantes
    public static final int NATIONAL = 0;
    public static final int REGIONAL = 1;
    public static final int INTERNATIONAL = 2;

    // Attributs d'instance.
    private String nom;
    private int chargeTravail;
    private int nbrDiplomes;
    private int nbrDroitsAnciennete;
    private LocalDate dateRevision;
    private double salaire;

	// Constructeur
    public Employe(){ }

    public Employe(JSONObject employe)throws Exception{

        this.nom = employe.getString("nom");
        this.chargeTravail = employe.getInt("charge_travail");
        this.nbrDiplomes = employe.getInt("nombre_diplomes") + 2;
        this.nbrDroitsAnciennete = employe.getInt("nombre_droit_anciennete");
        extraireDateFormatee(employe);
        this.salaire = 0.00;
    }

    /**
     * Extrait la date en format String de l'objet Json employe et la formate.
     *
     * @param employe :     Un object json validé
     */
    private void extraireDateFormatee(JSONObject employe) {

        String date = employe.getString("date_revision_salaire");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.dateRevision = LocalDate.parse(date, formatter);
    }

    // Getter.
    public String getNom() {
        return nom;
    }
    public double getSalaire() {
        return salaire;
    }
    public int getChargeTravail() {
        return chargeTravail;
    }
    public LocalDate getDateRevision() {
        return dateRevision;
    }

    public int getNbrDiplomes() {
        return nbrDiplomes;
    }

    public int getNbrDroitsAnciennete() {
        return nbrDroitsAnciennete;
    }

    // Setter
    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }

    /**
     * Une methode d'instance pour calculer le salaire d'un employé.
     *
     * @param typeDepartement : Il s'agit d'un int de 0 a 2 selon le type de departement.
     * @param tauxHoraireMin
     * @param tauxHoraireMax
     */
    public void calculerSalaire(int typeDepartement, double tauxHoraireMin, double tauxHoraireMax)  {
    
        if(typeDepartement == NATIONAL) {
            this.salaire = chargeTravail * tauxHoraireMin;
            calculerSalaireNational();
        } else if(typeDepartement == REGIONAL) {
            this.salaire = chargeTravail * (tauxHoraireMin + tauxHoraireMax) / 2;
            calculerSalaireRegional();
        } else if(typeDepartement == INTERNATIONAL){
                this.salaire = chargeTravail * tauxHoraireMax;
                calculerSalaireInternational();
        }
    }
    
    /*
    * Méthode qui calcule le salaire en fonction du nombre d'années d'ancienneté
    * pour un employé d'un département national.
    */
    private void calculerSalaireNational(){

        this.salaire += (nbrDroitsAnciennete * (5 * this.salaire / 100) - 5000);
    }

    /*
    * Méthode qui calcule le salaire en fonction du nombre d'années d'ancienneté
    * et du nombre de diplômes pour un employé d'un département régional.
    */
    private void calculerSalaireRegional() {

        this.salaire += (nbrDroitsAnciennete * (10 * this.salaire / 100) - 5000);

        if (chargeTravail > 500 && chargeTravail <= 1000) {
            this.salaire += (500 * nbrDiplomes);
        } else if (chargeTravail > 1000) {
            this.salaire += (1000 * nbrDiplomes);
        }
    }

    /*
    * Méthode qui calcule le salaire en fonction du nombre d'années d'ancienneté
    * et du nombre de diplômes pour un employé d'un département international.
	*/
    private void calculerSalaireInternational(){

        this.salaire += (nbrDroitsAnciennete * (15 * this.salaire / 100) - 5000);

        if (chargeTravail <= 500) {
            this.salaire += (500 * nbrDiplomes);
        } else {
            this.salaire += calculerMontantDiplome();
        }
    }

    /*
   * Méthode qui calcule le montant alloué à un employé du département international
   * pour ses diplômes. Le montant ne peut dépassé 5000$.
   *
   * @return le montant alloué pour le nombre de diplômes de l'employé.
   */
    private double calculerMontantDiplome (){

        int montantDiplome = 1500 * this.nbrDiplomes;
        if (montantDiplome > 5000) {
            return  5000;
        } else {
            return montantDiplome;
        }
    }
}

