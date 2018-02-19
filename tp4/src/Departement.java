
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.util.ArrayList;

/**
 * Classe Département
 *
 * Classe public créant un instance à partir des informations extraites d'un fichier JSON. Elle contient
 * plusieurs informations sur le département, donc une liste d'objets Employe. À l'aide d'une méthode
 * d'instance privée, la classe se charge de calculer sa valeur monétaire totale ainsi que les rentes
 * fédérale et provinciale.
 */
public class Departement{

    // Constantes
    final static double VALEUR_BASE = 9733.70;
    final static double TAUX_RENTE_PROVINCIALE = 7.2;
    final static double TAUX_RENTE_FEDERALE = 12.5;

    // Attributs
	private String nom;
	private int type;
	private double tauxHoraireMin;
	private double tauxHoraireMax;
    private double valeurTotale;
    private double renteFederale;
    private double renteProvinciale;
	private ArrayList<Employe> employes;

	// Constructeur
    public Departement(){}

    public Departement(JSONObject contenuFichierJson) throws Exception {

        this.nom = contenuFichierJson.getString("nom_departement");
        this.type = contenuFichierJson.getInt("type_departement");
        extraireTauxHoraireEnDouble(contenuFichierJson);
        JSONArray jsonEmployes = contenuFichierJson.getJSONArray("employes");
        extraireListeEmployeDuJsonArray(jsonEmployes);
    }

	/**
	* Extrait du contenu du fichier json, les valeurs des taux horaires de type String et les convertis en
     * type Double.
	* 
	* @param contenuFichierJson :       contenant les taux horaire a convertir
	*/
    private void extraireTauxHoraireEnDouble(JSONObject contenuFichierJson) {

        String tauxHoraire;

        tauxHoraire = contenuFichierJson.getString("taux_horaire_min");
        this.tauxHoraireMin = Utilitaires.convertirTauxHoraireEnDouble(tauxHoraire);
        tauxHoraire = contenuFichierJson.getString("taux_horaire_max");
        this.tauxHoraireMax = Utilitaires.convertirTauxHoraireEnDouble(tauxHoraire);
    }


	/**
	* Extrait d'une JSONArray, la liste d'employés du département.
	*
	* @param jsonEmployes:          La JSONArray contenant la liste d'employés.
	*/
    private void extraireListeEmployeDuJsonArray(JSONArray jsonEmployes) throws Exception {

        this.employes = new ArrayList<>();

        for (int i = 0; i < jsonEmployes.size(); i++) {
            JSONObject jsonEmploye = jsonEmployes.getJSONObject(i);
            Employe employe = new Employe(jsonEmploye);
            employe.calculerSalaire(this.type, this.tauxHoraireMin, this.tauxHoraireMax);
            this.employes.add(employe);
        }
    }

    // Getters
    public ArrayList<Employe> getEmployes() {
        return employes;
    }

    public double getTauxHoraireMin() {
        return tauxHoraireMin;
    }

    public double getTauxHoraireMax() {
        return tauxHoraireMax;
    }

    public double getRenteFederale() {
        return renteFederale;
    }

    public double getRenteProvinciale() {
        return renteProvinciale;
    }

    public double getValeurTotale() {
        return valeurTotale;
    }

    public String getNom() {
        return nom;
    }

    public int getNbrEmployes(){
        return employes.size();
    }

    // Setters
    public void setEmployes(ArrayList <Employe> employes) {
        this.employes = employes;
    }

    /**
     * Calcul, à partir de la liste d'employés, la valeur salariale totale du, la rente provinciale
     * et la rente federale.
     */
    public void calculerValeurTotaleEtRentes() {

        for (Employe employe : this.employes) {
            double salaire = employe.getSalaire();
            this.valeurTotale += salaire;
        }
        this.valeurTotale = arrondirCinqSousLePlusPres(VALEUR_BASE + this.valeurTotale);
        this.renteProvinciale = arrondirCinqSousLePlusPres(TAUX_RENTE_PROVINCIALE* this.valeurTotale/100);
        this.renteFederale = arrondirCinqSousLePlusPres(TAUX_RENTE_FEDERALE* this.valeurTotale/100);
    }

    /**
     * Une methode qui arrondie au multiple de 5 superieur le nombre de cent du montant.
     *
     * @param montant :         un double qui est le montant a arrondir.
     * @return                  un double qui est le salaire ajusté.
     */
    private Double arrondirCinqSousLePlusPres(double montant) {
        return Math.round(montant * 20.0) / 20.0;
    }
}
