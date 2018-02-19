import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
* Classe Validation
*
* Le principe de cette classe est de validé un objet json contenant des informations d'un département. Non
* seulement elle valide sa forme mais aussi toutes les propriétées s'y retrouvant. Chaque méthode de
* validation privée est appelée par la méthode public valider(). Si une propriété est manquante ou
* non-valide, une Exception sera lancée avec un message personnalisé.
*/
public class Validation{

    //Constantes de classe
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

    /**
     * Valide les propriétés du fichier json contenant les informations du département.
     *
     * @param contenuFichierJson
     * @throws CustomException
     */
    public static void valider(JSONObject contenuFichierJson) throws CustomException{

        validerClesProprietesFichierEntree(contenuFichierJson);
        validerFormatProprietesFichierEntree(contenuFichierJson);
        validerDepartement(contenuFichierJson);
        validerNombreEmploye(contenuFichierJson);
        validerFormatTauxHoraires(contenuFichierJson);
        validerTauxHorairesPositifs(contenuFichierJson);
        validerEmployes(contenuFichierJson);
    }

    /**
     * Valide que tous les propriétées recherchées sont présentes dans l'objet JSON.
     *
     * @throws CustomException
     * @param contenuFichierJson
     */
    private static void validerClesProprietesFichierEntree(JSONObject contenuFichierJson) throws
            CustomException{

        boolean valide = (contenuFichierJson.containsKey("nom_departement") &&
                contenuFichierJson.containsKey("type_departement") &&
                contenuFichierJson.containsKey("taux_horaire_min") &&
                contenuFichierJson.containsKey("taux_horaire_max") &&
                contenuFichierJson.containsKey("employes"));

        if (!valide) {
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_JSON);
        }
    }

    /**
     * Valide que le type de chaque propriétée est respecté, autant dans les informations du département
     * que dans la liste d'employé de ce même département.
     *
     * @throws Exception
     * @param contenuFichierJson
     */
    private static void validerFormatProprietesFichierEntree(JSONObject contenuFichierJson) throws
            CustomException{

        boolean valide = (contenuFichierJson.get("nom_departement") instanceof String) &&
                (contenuFichierJson.get("type_departement") instanceof Integer) &&
                (contenuFichierJson.get("taux_horaire_min") instanceof String) &&
                (contenuFichierJson.get("taux_horaire_max") instanceof String) &&
                (contenuFichierJson.get("employes") instanceof JSONArray);

        if (!valide){
            throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_JSON);
        }
    }

    /**
     * Valide que le type de département soit numéroter par 0, 1, ou 2 seulement.
     *
     * @throws CustomException
     * @param contenuFichierJson
     */
    private static void validerDepartement(JSONObject contenuFichierJson) throws CustomException{

        int typeDepartement = contenuFichierJson.getInt("type_departement");
        if(typeDepartement < MINIMUM_TYPE_DEPARTEMENT || typeDepartement > MAXIMUM_TYPE_DEPARTEMENT){
            throw new CustomException(CustomException.ERREUR_DEPARTEMENT);
        }
    }

    /**
     * Valide que le nombre d'employé ne dépasse pas 10 et que le département ne soit pas vide.
     *
     * @throws CustomException
     * @param contenuFichierJson
     */
    private static void validerNombreEmploye(JSONObject contenuFichierJson) throws CustomException{

        JSONArray employes = contenuFichierJson.getJSONArray("employes");

        if(employes.size() > MAXIMUM_NOMBRE_EMPLOYE){
            throw new CustomException(CustomException.ERREUR_NOMBRE_EMPLOYE);
        }
        if(employes.size() == MININUM_NOMBRE_EMPLOYE){
            throw new CustomException(CustomException.ERREUR_DEPARTEMENT_EMPLOYE);
        }
    }

    /**
     * Valide si le format des montants des taux horaires convient aux spécifications.
     *
     * @throws CustomException
     * @param contenuFichierJson
     */
    private static void validerFormatTauxHoraires(JSONObject contenuFichierJson) throws CustomException{

        String tauxHoraireMin = contenuFichierJson.getString("taux_horaire_min");
        String tauxHoraireMax = contenuFichierJson.getString("taux_horaire_max");
        boolean regexTauxHoraireMin = tauxHoraireMin.matches("^\\-?\\d+([\\.,]\\d{2})?([\\s+]?\\$)$");
        boolean regexTauxHoraireMax = tauxHoraireMax.matches("^\\-?\\d+([\\.,]\\d{2})?([\\s+]?\\$)$");

        if(!regexTauxHoraireMax || !regexTauxHoraireMin){
            throw  new CustomException(CustomException.ERREUR_FORMAT_MONTANT_ARGENT);
        }
    }

    /**
     * Valide que les taux horaires minimum et maximum ne soient pas négatifs.
     *
     * @throws CustomException
     * @param contenuFichierJson
     */
    private static void validerTauxHorairesPositifs(JSONObject contenuFichierJson) throws CustomException{

        double tauxMin = Utilitaires.convertirTauxHoraireEnDouble(contenuFichierJson.getString
                ("taux_horaire_min"));
        double tauxMax = Utilitaires.convertirTauxHoraireEnDouble(contenuFichierJson.getString
                ("taux_horaire_max"));

        if(tauxMin < 0 || tauxMax < 0){
            throw  new CustomException(CustomException.ERREUR_MONTANT_ARGENT);
        }
    }

    /**
     * Valide les propriétés des employés du département.
     *
     * @param contenuFichierJson
     * @throws CustomException
     */
    private static void validerEmployes(JSONObject contenuFichierJson) throws CustomException{

        JSONArray employes = contenuFichierJson.getJSONArray("employes");

        validerClesEmployes(employes);
        validerFormatProprietesEmployes(employes);
        validerDroitAnciennete(employes);
        validerNombreDiplome(employes);
        validerChargeTravail(employes);
        validerDate(employes);
        validerNomsCleUnique(employes);
    }

    /**
     * Valide que tous les propriétées recherchées sont présentes dans le
     * informations JSON, autant dans les informations du département que dans la
     * liste d'employé de ce même département.
     *
     * @throws CustomException
     * @param employes
     */
    private static void validerClesEmployes(JSONArray employes) throws CustomException{

        for (int i = 0; i < employes.size(); i++) {
            if(!(employes.getJSONObject(i).containsKey("nom") &&
                    employes.getJSONObject(i).containsKey("nombre_droit_anciennete") &&
                    employes.getJSONObject(i).containsKey("nombre_diplomes") &&
                    employes.getJSONObject(i).containsKey("charge_travail") &&
                    employes.getJSONObject(i).containsKey("date_revision_salaire"))) {

                throw new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_JSON);
            }
        }
    }

    /**
     * Valide le format des employes inclus dans la JSONArray employes.
     *
     * @param employes
     */
    private static void validerFormatProprietesEmployes(JSONArray employes) throws CustomException {

        for(int i = 0; i < employes.size(); i++){
            JSONObject employe = (JSONObject)employes.get(i);
            if (!(employe instanceof JSONObject) || !validerFormatProprietesEmploye(employe)){
                throw  new CustomException(CustomException.ERREUR_PROPRIETES_FICHIER_JSON);
            }
        }
    }

    /**
     * Valide le format de chacunes des prorietés d'un employe sous forme d'objet JSON.
     *
     * @param employe
     * @return si le format est valide ou non.
     */
    private static boolean validerFormatProprietesEmploye(JSONObject employe){

        return (employe.get("nom") instanceof String) &&
                (employe.get("nombre_droit_anciennete") instanceof Integer) &&
                (employe.get("nombre_diplomes") instanceof Integer) &&
                (employe.get("charge_travail") instanceof Integer) &&
                (employe.get("date_revision_salaire") instanceof String);
    }

    /**
     * Valide que le droit d'ancienneté de tous les employés soit compris entre 0 et 10.
     *
     * @throws CustomException
     * @param employes
     */
    private static void validerDroitAnciennete(JSONArray employes) throws CustomException{

        for(int i = 0 ; i < employes.size(); i++){
            JSONObject employe = employes.getJSONObject(i);
            int droitAnciennete = employe.getInt("nombre_droit_anciennete");
            if(droitAnciennete < MINIMUM_DROIT_ANCIENNETEE || droitAnciennete > MAXIMUM_DROIT_ANCIENNETEE){
                throw new CustomException(CustomException.ERREUR_DROIT_ANCIENNETE);
            }
        }
    }

    /**
     * Valide que le nombre de diplome soit compris entre 0 et 5.
     *
     * @throws CustomException
     * @param employes
     */
    private static void validerNombreDiplome(JSONArray employes) throws CustomException{

        for(int i = 0 ; i < employes.size(); i++){
            JSONObject employe = employes.getJSONObject(i);
            int nombreDiplomes = employe.getInt("nombre_diplomes");
            if(nombreDiplomes < MINIMUM_NOMBRE_DIPLOME || nombreDiplomes > MAXIMUM_NOMBRE_DIPLOME){
                throw new CustomException(CustomException.ERREUR_NOMBRE_DIPLOME);
            }
        }
    }

    /**
     * Valide que la charge de travail se trouve entre 0 et 1950 heures.
     *
     * @throws CustomException
     * @param employes
     */
    private static void validerChargeTravail(JSONArray employes) throws CustomException{

        for(int i = 0; i < employes.size(); i++){
            JSONObject employe = employes.getJSONObject(i);
            int chargeTravail = employe.getInt("charge_travail");
            if(chargeTravail < MINIMUM_CHARGE_TRAVAIL || chargeTravail > MAXIMUM_CHARGE_TRAVAIL){
                throw new CustomException(CustomException.ERREUR_CHARGE_TRAVAIL);
            }
        }
    }

    /**
     * Valide le format des dates de révision de salaire des employé.
     *
     * @throws CustomException
     * @param employes
     */
    private static void validerDate(JSONArray employes) throws CustomException{

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for(int i = 0; i < employes.size(); i++){
            JSONObject employe = employes.getJSONObject(i);
            String dateRevisionSalaire = employe.getString("date_revision_salaire");
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
     * @param employes
     */
    private static void validerNomsCleUnique(JSONArray employes) throws CustomException{

        ArrayList<String> nomEmployes = new ArrayList<>();

        for(int i = 0; i < employes.size(); i++){
            JSONObject employe = employes.getJSONObject(i);
            String nom = employe.getString("nom");
            if (nomEmployes.contains(nom)){
                throw new CustomException(CustomException.NOM_UNIQUE);
            }
            nomEmployes.add(nom);
        }
    }
}
