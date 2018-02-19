/*
 * Classe Validation
 *
 * Le principe de cette classe est de validé son seul attribut, soit un fichier
 * JSON spécifique au programme CashFlow. Chaque méthode de validation privée
 * est appelée par la méthode public valideFichierEntree() et est responsable
  * de validé sa propre propriété.
 */

import manage.file.FileManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class JSONEntree {

    //------------//
    // CONSTANTES //
    //------------//

    final static int MINIMUM_DROIT_ANCIENNETEE = 0;
    final static int MAXIMUM_DROIT_ANCIENNETEE = 10;
    final static int MINIMUM_NOMBRE_DIPLOME = 0;
    final static int MAXIMUM_NOMBRE_DIPLOME = 5;
    final static int MINIMUM_TYPE_DEPARTEMENT = 0;
    final static int MAXIMUM_TYPE_DEPARTEMENT = 2;
    final static int MININUM_NOMBRE_EMPLOYE = 0;
    final static int MAXIMUM_NOMBRE_EMPLOYE = 10;
    final static int MINIMUM_CHARGE_TRAVAIL = 0;
    final static int MAXIMUM_CHARGE_TRAVAIL = 1950;


    //-----------//
    // ATTRIBUTS //
    //-----------//

    private JSONObject informations;
    private ArrayList<Employe> listeEmployes;


    //--------------//
    // CONSTRUCTEUR //
    //--------------//

    public JSONEntree () {

    }

    public JSONEntree(String fileLocationFolder , String entreeJson) throws Exception {
        try {
            String contenuJsonEntree = CashFlow.lireUnFichier(fileLocationFolder, entreeJson);
            informations = JSONObject.fromObject(contenuJsonEntree);
        } catch (FileNotFoundException e){
            throw new IOException(CustomException.ERREUR_LECTURE);
        } catch (Exception e) {
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_JSON);
        }
        validerFichierEntree();
        extraireListeEmploye();
    }

    //---------//
    // GETTERS //
    //---------//

    public JSONObject getInformations() {
        return informations;
    }

    public ArrayList<Employe> getListeEmployes() {
        return listeEmployes;
    }

    //---------//
    // SETTERS //
    //---------//

    public void setInformations(JSONObject informations) { this.informations = informations; }

    public void setListeEmployes(ArrayList<Employe> listeEmployes) { this.listeEmployes = listeEmployes; }

    //--------------------//
    // MÉTHODES PUBLIQUES //
    //--------------------//


    /**
     * Une methode qui prend le taux horaire en parametre sous forme de string
     * et le retourne en double.
     *
     * @param tauxHoraire : Un string qui représente le taux horaire.
     * @return Un double qui est le taux horaire modifié.
     */
    public double convertirTauxHoraireEnDouble(String tauxHoraire) {
        //enleve le signe dollar et parse en double
        if (tauxHoraire.contains(",")){
            tauxHoraire = tauxHoraire.replace(",",".");
        }
        tauxHoraire = tauxHoraire.replace(tauxHoraire.substring(tauxHoraire.length() - 1), "");
        return Double.parseDouble(tauxHoraire);
    }

    //------------------//
    // MÉTHODES PRIVÉES //
    //------------------//

    /**
     * Une methode boolean qui prend le JSONObject en entre en parametre et
     * verifie si tous les proprietes sont valides.
     *
     * @return le JSONObject validé par la classe
     * @throws Exception
     */
    private void validerFichierEntree() throws Exception{

        validerClesProprietesFichierEntree();
        validerClesEmployesProprietesFichierEntree();
        validerFormatProprietesFichierEntree();
        validerFormatMontantArgent();
        validerDroitAnciennete();
        validerNombreDiplome();
        validerDepartement();
        validerNombreEmploye();
        validerMontantArgent();
        validerChargeTravail();
        validerDate();
        validerNomsCleUnique();
    }

    /**
     * Une methode qui extrait d'un JSONObject tous les informations pour creer
     * une liste d'employés et leurs informations pour les ajouter dans
     * un ArrayList<employe>.
     *
     * @return listeEmployes : Un ArrayList d'employés.
     * @throws Exception : Une exception sera lance si le fichier en entree est
     * inexistant.
     */
    private void extraireListeEmploye() throws Exception {

        double tauxHoraireMin = convertirTauxHoraireEnDouble(this.informations.getString("taux_horaire_min"));
        double tauxHoraireMax = convertirTauxHoraireEnDouble(this.informations.getString("taux_horaire_max"));
        JSONArray employes = this.informations.getJSONArray("employes");
        this.listeEmployes = new ArrayList<>();

        for (int i = 0; i < employes.size(); i++) {
            JSONObject object = employes.getJSONObject(i);
            Employe employe = new Employe(object.getString("nom"),
                    object.getInt("charge_travail"),
                    object.getInt("nombre_diplomes"),
                    object.getInt("nombre_droit_anciennete"),
                    tauxHoraireMin, tauxHoraireMax);
            listeEmployes.add(employe);
        }
    }

    /**
     * Valide que le type de chaque propriétée est respecté, autant dans les informations du département
     * que dans la liste d'employé de ce même département.
     *
     * @throws Exception
     */
    private void validerFormatProprietesFichierEntree() throws CustomException{

        boolean valide = (informations.get("nom_departement") instanceof String) &&
                (informations.get("type_departement") instanceof Integer) &&
                (informations.get("taux_horaire_min") instanceof String) &&
                (informations.get("taux_horaire_max") instanceof String) &&
                (informations.get("employes") instanceof JSONArray);

        if (valide){
            JSONArray employes = informations.getJSONArray("employes");
            int i = 0;
            while(valide && i < employes.size()){
                JSONObject obj = (JSONObject)employes.get(i);
                valide = obj instanceof JSONObject;
                if (valide){
                    valide = (obj.get("nom") instanceof String) &&
                            (obj.get("nombre_droit_anciennete") instanceof Integer) &&
                            (obj.get("nombre_diplomes") instanceof Integer) &&
                            (obj.get("charge_travail") instanceof Integer) &&
                            (obj.get("date_revision_salaire") instanceof String);
                }
                i++;
            }
        }
        if (!valide){
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_ENTREE);
        }
    }

    /**
     * Valide que tous les propriétées recherchées sont présentes dans le
     * informations JSON, autant dans les informations du département que dans la
     * liste d'employé de ce même département.
     *
     * @throws CustomException
     */
    private void validerClesEmployesProprietesFichierEntree()throws CustomException{

        boolean valide = true;
        JSONArray employes = informations.getJSONArray("employes");
        int i = 0;
        while (valide && i < employes.size()) {
            valide = (employes.getJSONObject(i).containsKey("nom")
                    && employes.getJSONObject(i).containsKey("nombre_droit_anciennete")
                    && employes.getJSONObject(i).containsKey("nombre_diplomes")
                    && employes.getJSONObject(i).containsKey("charge_travail")
                    && employes.getJSONObject(i).containsKey("date_revision_salaire"));
            i++;
        }
        if (!valide) {
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_ENTREE);
        }
    }

    /**
     * Valide que tous les propriétées recherchées sont présentes dans le
     * informations JSON, autant dans les informations du département que dans la
     * liste d'employé de ce même département.
     *
     * @throws CustomException
     */
    private void validerClesProprietesFichierEntree ()throws CustomException{

        boolean valide = (informations.containsKey("nom_departement")
                && informations.containsKey("type_departement")
                && informations.containsKey("taux_horaire_min")
                && informations.containsKey("taux_horaire_max")
                && informations.containsKey("employes"));

        if (!valide) {
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_ENTREE);
        }
    }

    /**
     * Valide si le format des montants des taux horaires convient au
     * spécifications.
     *
     * @throws CustomException
     */
    private void validerFormatMontantArgent ()throws CustomException{

        String tauxHoraireMin = informations.getString("taux_horaire_min");
        String tauxHoraireMax = informations.getString("taux_horaire_max");
        boolean regexTauxHoraireMin = tauxHoraireMin.matches("^\\-?\\d+([\\.,]\\d{2})?([\\s+]?\\$)$");
        boolean regexTauxHoraireMax = tauxHoraireMax.matches("^\\-?\\d+([\\.,]\\d{2})?([\\s+]?\\$)$");

        if(!regexTauxHoraireMax || !regexTauxHoraireMin){
            throw  new CustomException(CustomException.ERREUR_FORMAT_MONTANT_ARGENT);
        }
    }

    /**
     * Une méthode boolean qui prend le JSON object en parametre et verifie si
     * le droit d'anciennete de tous les employes n'est pas inferieur a 0 ou
     * superieur a 10.
     *
     * @throws CustomException
     */
    private void validerDroitAnciennete () throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");

        for(int i = 0 ; i < employes.size(); i++){
            int droitAnciennete = employes.getJSONObject(i).getInt("nombre_droit_anciennete");
            if(droitAnciennete < MINIMUM_DROIT_ANCIENNETEE || droitAnciennete > MAXIMUM_DROIT_ANCIENNETEE){
                throw new CustomException(CustomException.ERREUR_DROIT_ANCIENNETE);
            }
        }
    }

    /**
     * Valide que le nombre de diplome soit compris entre 0 et 5.
     *
     * @throws CustomException
     */
    private void validerNombreDiplome () throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");

        for(int i = 0 ; i < employes.size(); i++){
            int nombreDiplome = employes.getJSONObject(i).getInt("nombre_diplomes");
            if(nombreDiplome < MINIMUM_NOMBRE_DIPLOME || nombreDiplome > MAXIMUM_NOMBRE_DIPLOME){
                throw new CustomException(CustomException.ERREUR_NOMBRE_DIPLOME);
            }
        }
    }

    /**
     * Valide que le type de département soit numéroter par 0, 1, ou 2
     * seulement.
     *
     * @throws CustomException
     */
    private void validerDepartement() throws CustomException{

        int typeDepartement = informations.getInt("type_departement");
        if(typeDepartement < MINIMUM_TYPE_DEPARTEMENT || typeDepartement > MAXIMUM_TYPE_DEPARTEMENT){
            throw new CustomException(CustomException.ERREUR_DEPARTEMENT);
        }
    }

    /**
     * Valide que le nombre d'employé ne dépasse pas 10 et que le département
     * ne soit pas vide.
     *
     * @throws CustomException
     */
    private void validerNombreEmploye() throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");

        if(employes.size() > MAXIMUM_NOMBRE_EMPLOYE){
            throw new CustomException(CustomException.ERREUR_NOMBRE_EMPLOYE);
        }
        if(employes.size() == MININUM_NOMBRE_EMPLOYE){
            throw new CustomException(CustomException.ERREUR_DEPARTEMENT_EMPLOYE);
        }
    }

    /**
     * Valide que les taux horaires minimum et maximum ne soient pas négatifs.
     *
     * @throws CustomException
     */
    private void validerMontantArgent() throws CustomException{

        double tauxMin = convertirTauxHoraireEnDouble(informations.getString("taux_horaire_min"));
        double tauxMax = convertirTauxHoraireEnDouble(informations.getString("taux_horaire_max"));

        if(tauxMin < 0 || tauxMax < 0){
            throw  new CustomException(CustomException.ERREUR_MONTANT_ARGENT);
        }
    }

    /**
     * Valide que la charge de travail se trouve entre 0 et 1950 heures.
     *
     * @throws CustomException
     */
    private void validerChargeTravail() throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");

        for(int i = 0; i < employes.size(); i++){
            int chargeTravail = employes.getJSONObject(i).getInt("charge_travail");
            if(chargeTravail < MINIMUM_CHARGE_TRAVAIL || chargeTravail > MAXIMUM_CHARGE_TRAVAIL){
                throw new CustomException(CustomException.ERREUR_CHARGE_TRAVAIL);
            }
        }
    }

    /**
     * Valide le format des dates de révision de salaire des employé.
     *
     * @throws CustomException
     */
    private void validerDate ()throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(int i = 0; i < employes.size(); i++){
            String dateRevisionSalaire = employes.getJSONObject(i).getString("date_revision_salaire");
            try {
                LocalDate.parse(dateRevisionSalaire,formatter);
            } catch (Exception e) {
                throw new CustomException(CustomException.ERREUR_DATE);
            }
        }
    }

    /**
     * Valide qu'il n'ait pas plus d'un employé avec le même nom.
     *
     * @throws CustomException
     */
    private void validerNomsCleUnique ()throws CustomException{

        JSONArray employes = informations.getJSONArray("employes");
        ArrayList<String> NomDesEmployes = new ArrayList<>();
        boolean estUnique = true;
        for (int i = 0; i < employes.size(); i++){
            String nom = employes.getJSONObject(i).getString("nom");
            if (NomDesEmployes.contains(nom)){
                estUnique = false;
                break;
            }else{
                NomDesEmployes.add(nom);
            }
        }
        if (!estUnique){
            throw new CustomException(CustomException.NOM_UNIQUE);
        }
    }
}
