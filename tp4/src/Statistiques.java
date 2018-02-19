
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe Statistiques
 *
 * Permet au programme de calculer des statistiques sur ses exécutions. Avec des méthodes publiques,
 * l'instance peut importer des statistiques existante et aussi sauvegarder de nouvelles statistiques.
 * Selon l'argument lors de l'appel du programme, l'instance peut réinitialiser ses statistiques et
 */
public class Statistiques {

    // Constantes
    final static String FICHIER_STATISTIQUES = "statistiques.json";

    // Formatage
    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    // Attributs
    private int nbrEmployesTotal;
    private int nbrEmployesSalaireInferieur50000;
    private int nbrEmployesSalaireEntre50000Et100000;
    private int nbrEmployesSalaireSuperieur100000;
    private JSONArray nbrEmployesParTypeDepartement;
    private int chargeTravailMaximale;
    private double valeurSalarialeMaximale;
    private double valeurSalarialeMinimale;

    // Getters
    public JSONArray getNbrEmployesParTypeDepartement() {
        return nbrEmployesParTypeDepartement;
    }

    /**
     * Importe les statistiques présente dans le fichier statistiques s'il existe pour les transmettre aux
     * attributs de l'instance.
     *
     * @throws IOException
     * @throws CustomException
     */
    public void importer() throws IOException {
        File fichierStatistiques = new File("json/statistiques.json");

        if (fichierStatistiques.exists()) {
            String contenu = Utilitaires.createStringFromFileContent("json", FICHIER_STATISTIQUES);
            JSONObject contenuJson = JSONObject.fromObject(contenu);
            extraireStatistiques(contenuJson);
        }
    }

    /**
     * Initialise les attibuts de classe à partir du JSONObject contenu.
     */
    private void extraireStatistiques(JSONObject contenuJson){

        this.nbrEmployesTotal = contenuJson.getInt("nombre_employes");
        this.nbrEmployesSalaireInferieur50000 = contenuJson.getInt
                ("nombre_employes_salaire_inferieur_50000");
        this.nbrEmployesSalaireSuperieur100000 = contenuJson.getInt
                ("nombre_employes_salaire_superieur_100000");
        this.nbrEmployesSalaireEntre50000Et100000 = contenuJson.getInt
                ("nombre_employes_salaire_entre_50000_et_100000");
        this.chargeTravailMaximale = contenuJson.getInt("charge_de_travail_maximal");
        this.valeurSalarialeMaximale = contenuJson.getDouble("valeur_salarial_maximale");
        this.valeurSalarialeMinimale = contenuJson.getDouble("valeur_salarial_minimale");
        this.nbrEmployesParTypeDepartement = contenuJson.getJSONArray("nombre_employes_par_departement");
    }

    /**
     * Calcule les nouveaux statistiques une fois le nouveau département traité. Les statistiques resteront
     * inchangées si le département est déjà comptabilisé.
     *
     * @param departement
     */
    public void calculerNouveauxStatistiques(Departement departement) throws CustomException {

        if (this.nbrEmployesParTypeDepartement == null){
            this.nbrEmployesParTypeDepartement = new JSONArray();
        }

        if (departementNonExistant(departement)){
            ArrayList<Employe> employes = departement.getEmployes();

            this.nbrEmployesTotal += employes.size();
            inclureNbrEmployesParDepartement(departement);
            calculerStatistiquesSalaires(employes);
            if (this.valeurSalarialeMinimale == 0) {
                this.valeurSalarialeMinimale = departement.getValeurTotale();
            }
            calculerStatistiquesValeurSalariale(departement);
        }
    }

    /**
     * Vérifie si le département traité n'est pas déjà comptabilisé.
     *
     * @param departement
     * @return si oui ou non le département est comptabilisé.
     */
    private boolean departementNonExistant(Departement departement){
        String nom = departement.getNom();
        Boolean nonExistant = true;
        for(int i = 0; i < nbrEmployesParTypeDepartement.size(); i++){
            JSONObject obj = (JSONObject) nbrEmployesParTypeDepartement.get(i);
            nonExistant = !(obj.containsKey(nom));
        }
        return nonExistant;
    }

    /**
     * Inclus dans la JSONArray nbrEmployesParDepartement les départements et le nombre d'employés y
     * travaillant.
     *
     * @param departement
     */
    private void inclureNbrEmployesParDepartement(Departement departement){

        JSONObject nouveauDepartement = new JSONObject();
        String nomDepartement = departement.getNom();
        int nbrEmploye = departement.getNbrEmployes();
        nouveauDepartement.accumulate(nomDepartement, nbrEmploye);
        this.nbrEmployesParTypeDepartement.add(nouveauDepartement);
    }

    /**
     * Calcul le nombre d'employés par catégorie de salaire soit:
     * -> Inférieur à 50.000$
     * -> Entre 50.000$ et 100.000$
     * -> Supérieur à 100.000$
     *
     * @param employes
     */
    private void calculerStatistiquesSalaires(ArrayList<Employe> employes){

        for(Employe employe : employes){
            if (employe.getChargeTravail() > this.chargeTravailMaximale){
                this.chargeTravailMaximale = employe.getChargeTravail();
            }
            if (employe.getSalaire() < 50000){
                this.nbrEmployesSalaireInferieur50000++;
            }else if (employe.getSalaire() > 100000){
                this.nbrEmployesSalaireSuperieur100000++;
            }else{
                this.nbrEmployesSalaireEntre50000Et100000++;
            }

        }
    }

    /**
     * Calcule quelle est la valeure salariale totale la plus élevée.
     * @param departement
     */
    private void calculerStatistiquesValeurSalariale(Departement departement){

        double valeurTotale = departement.getValeurTotale();

        if (valeurTotale >  this.valeurSalarialeMaximale) {
            this.valeurSalarialeMaximale = valeurTotale;
        }
        if (valeurTotale < this.valeurSalarialeMinimale){
            this.valeurSalarialeMinimale = valeurTotale;
        }
    }

    /**
     * Sauvegarde l'instance et ses attributs en écrasant le fichier statistiques s'il existe.
     * @throws IOException
     */
    public void sauvegarder() throws IOException {
        try {
            JSONObject contenuJson = inclureStatistiques();
            String contenu = contenuJson.toString(4);
            Utilitaires.createFileFromStringContent("json", FICHIER_STATISTIQUES, contenu);
        } catch (Exception e){
            throw new IOException(CustomException.ERREUR_SAUVEGARDE_STATISTIQUES);
        }
    }

    /**
     * Inclus (accumulate) toutes les contenu dans le JSONObject contenu
     */
    private JSONObject inclureStatistiques() {

        JSONObject contenu = new JSONObject();

        contenu.accumulate("nombre_employes", this.nbrEmployesTotal);
        contenu.accumulate("nombre_employes_salaire_inferieur_50000",
                this.nbrEmployesSalaireInferieur50000);
        contenu.accumulate("nombre_employes_salaire_superieur_100000",
                this.nbrEmployesSalaireSuperieur100000);
        contenu.accumulate("nombre_employes_salaire_entre_50000_et_100000",
                this.nbrEmployesSalaireEntre50000Et100000);
        contenu.accumulate("nombre_employes_par_departement", nbrEmployesParTypeDepartement);
        contenu.accumulate("charge_de_travail_maximal", this.chargeTravailMaximale);
        contenu.accumulate("valeur_salarial_maximale", this.valeurSalarialeMaximale);
        contenu.accumulate("valeur_salarial_minimale", this.valeurSalarialeMinimale);

        return contenu;
    }


    /**
     * Affiche sur la console les statistiques formattés.
     */
    public void afficher(){
        String contenu = formatterStatistiquesEnString();
        System.out.println(contenu);
    }

    /**
     * Concatène les contenu dans une seule chaîne.
     *
     * @return la chaîne formatée.
     */
    private String formatterStatistiquesEnString(){
        String stats = "------------\nSTATISTIQUES\n------------\n";
        stats += String.format("Nombre d'employés total: %d\nNombre d'employés avec un salaire " +
                "inférieur à 50 000$: %d\nNombre d'employés avec un salaire entre 50 000$ et " +
                "100 000$: %d\nNombre d'employés avec un salaire supérieur à 100 000$ : %d\n", this
                .nbrEmployesTotal, this.nbrEmployesSalaireInferieur50000, this
                .nbrEmployesSalaireEntre50000Et100000, this.nbrEmployesSalaireSuperieur100000);
        stats += formatterJsonArrayEnString();
        stats += String.format("Charge de travail maximale: %d\nValeur salariale minimale: %s\nValeur " +
                "salariale maximale %s", this.chargeTravailMaximale, DECIMAL_FORMAT.format(this
                .valeurSalarialeMinimale) + " $", DECIMAL_FORMAT.format(this.
                valeurSalarialeMaximale) + " $");

        return stats;
    }

    /**
     * Concatène la JSONArray à partir de l'attribut nbrEmployesParDepartement
     *
     * @return une chaîne formatée à partir d'un JSONArray
     */
    private String formatterJsonArrayEnString(){
        String array = "Nombre d'employés par département: ";
        if (nbrEmployesParTypeDepartement.isEmpty()){
            array += "VIDE";
        } else {
            for (int i = 0; i < nbrEmployesParTypeDepartement.size(); i++) {
                JSONObject departement = nbrEmployesParTypeDepartement.getJSONObject(i);
                Iterator<String> iter = departement.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    array += ("\n\t\t" + (i + 1) + "." + key + ": " + departement.getString(key) + "\n");
                }
            }
        }

        return array;
    }

    /**
     * Une fois les statistiques de l'instance réinitialiser, la méthode écrase le fichier statistiques
     * existant avec des statistiques remises à zéro. Affiche un message de confirmation.
     *
     * @throws IOException
     */
    public void reinitialiser() throws IOException{
        try {
            reinitialiserStatistques();
            JSONObject contenuJson = inclureStatistiques();
            String contenu = contenuJson.toString(4);
            Utilitaires.createFileFromStringContent("json", FICHIER_STATISTIQUES, contenu);
            System.out.println("Réinitialisation des statistiques complétée.");
        } catch (Exception e){
            throw new IOException(CustomException.ERREUR_REINIT_STATISTIQUES);
        }
    }

    /**
     * Réinitialise tous les statistiques de l'instance.
     */
    private void reinitialiserStatistques(){
        this.nbrEmployesTotal = 0;
        this.nbrEmployesSalaireInferieur50000 = 0;
        this.nbrEmployesSalaireEntre50000Et100000 = 0;
        this.nbrEmployesSalaireSuperieur100000 = 0;
        this.nbrEmployesParTypeDepartement = new JSONArray();
        this.chargeTravailMaximale = 0;
        this.valeurSalarialeMaximale = 0;
        this.valeurSalarialeMinimale = 0;
    }
}
