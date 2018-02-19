/*
 * Classe Validation
 *
 * Le principe de cette classe est de validé son seul attribut, soit un fichier
 * JSON spécifique au programme CashFlow. Chaque méthode de validation privée
 * est appelée par la méthode public valideFichierEntree() et est responsable
  * de validé sa propre propriété.
 */
import static org.hamcrest.core.Is.isA;
import manage.file.FileManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Method;


public class ValidationTest {

    private static JSONObject contenu;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    public Method accederUneMethodePrivee(String fileLocationFOlder, String filename, String nomDeLaMethode) throws Exception {
        this.contenu = JSONObject.fromObject(FileManager.createStringFromFileContent(fileLocationFOlder,
                filename));
        Method method = Validation.class.getDeclaredMethod(nomDeLaMethode, JSONObject.class);
        method.setAccessible(true);
        method.invoke(null,contenu);
        return method;
    }

    public Method accederUneMethodePriveeAvecArgumentEmployes(String fileLocationFOlder, String filename,
                                                            String nomDeLaMethode) throws Exception {
        this.contenu = JSONObject.fromObject(FileManager.createStringFromFileContent(fileLocationFOlder,
                filename));
        JSONArray employes = contenu.getJSONArray("employes");
        Method method = Validation.class.getDeclaredMethod(nomDeLaMethode, JSONArray.class);
        method.setAccessible(true);
        method.invoke(null, employes);
        return method;
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest1() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_nom_departement_erreur.json","validerFormatProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest2() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_taux_horaire_min_erreur.json","validerFormatProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest3() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_type_departement_erreur.json","validerFormatProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest4() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_employes_erreur.json","validerFormatProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest5() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_nom_employes_erreur.json","validerFormatProprietesEmployes");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest6() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_droit_anciennete_erreur.json","validerFormatProprietesEmployes");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest7() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_nbr_diplome_erreur.json","validerFormatProprietesEmployes");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest8() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_charge_travail_erreur.json","validerFormatProprietesEmployes");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest9() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_date_revision_erreur.json","validerFormatProprietesEmployes");
    }

    @org.junit.Test
    public void validerFormatProprietesFichierEntreeTest10() throws Exception {
        exception.expectCause(isA(ClassCastException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerFormatProprietesFichierEntree",
                "entree_instance_object_employes_erreur.json","validerFormatProprietesEmployes");
    }



    @org.junit.Test
    public void validerClesProprietesFichierEntreeTest1() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerClesProprietesFichierEntree",
                "key_employes_erreur.json","validerClesProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerClesProprietesFichierEntreeTest2() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerClesProprietesFichierEntree",
                "key_nom_departement_erreur.json","validerClesProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerClesProprietesFichierEntreeTest3() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerClesProprietesFichierEntree",
                "key_taux_horaire_max_erreur.json","validerClesProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerClesProprietesFichierEntreeTest4() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerClesProprietesFichierEntree",
                "key_taux_horaire_min_erreur.json","validerClesProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerClesProprietesFichierEntreeTest5() throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerClesProprietesFichierEntree",
                "key_type_departement_erreur.json","validerClesProprietesFichierEntree");
    }

    @org.junit.Test
    public void validerClesEmployesProprietesFichierEntreeTest1 ()throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerClesEmployesProprietesFichierEntree",
                "key_charge_travail_erreur.json","validerClesEmployes");
    }

    @org.junit.Test
    public void validerClesEmployesProprietesFichierEntreeTest2 ()throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerClesEmployesProprietesFichierEntree",
                "key_date_revision_salaire_erreur.json","validerClesEmployes");
    }

    @org.junit.Test
    public void validerClesEmployesProprietesFichierEntreeTest3 ()throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerClesEmployesProprietesFichierEntree",
                "key_nom_erreur.json","validerClesEmployes");
    }

    @org.junit.Test
    public void validerClesEmployesProprietesFichierEntreeTest4 ()throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerClesEmployesProprietesFichierEntree",
                "key_nombre_diplomes_erreur.json","validerClesEmployes");
    }

    @org.junit.Test
    public void validerClesEmployesProprietesFichierEntreeTest5 ()throws Exception {
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerClesEmployesProprietesFichierEntree",
                "key_nombre_droit_anciennete_erreur.json","validerClesEmployes");
    }



    @org.junit.Test
    public void validerFormatMontantArgentTest1 ()throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatMontantArgent",
                "taux_horaire_1_decimale.json","validerFormatTauxHoraires");
    }

    @org.junit.Test
    public void validerFormatMontantArgentTest2 ()throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatMontantArgent",
                "taux_horaire_3_decimales.json","validerFormatTauxHoraires");
    }

    @org.junit.Test
    public void validerFormatMontantArgentTest3 ()throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerFormatMontantArgent",
                "taux_horaire_sans_signe_dollar.json","validerFormatTauxHoraires");
    }


    @org.junit.Test
    public void validerDroitAncienneteTest1 () throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDroitAnciennete",
                "minimum_droit_anciennete_erreur.json","validerDroitAnciennete");

    }

    @org.junit.Test
    public void validerDroitAncienneteTest2 () throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDroitAnciennete",
                "maximum_droit_anciennete_erreur.json","validerDroitAnciennete");
    }

    @org.junit.Test
    public void validerNombreDiplomeTest1 () throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerNombreDiplome",
                "maximum_nombre_diplome_erreur.json","validerNombreDiplome");
    }

    @org.junit.Test
    public void validerNombreDiplomeTest2 () throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerNombreDiplome",
                "minimum_nombre_diplome_erreur.json","validerNombreDiplome");
    }


    @org.junit.Test
    public void validerDepartementTest1() throws Exception{
        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerDepartement",
                "minimum_type_departement_erreur.json","validerDepartement");
    }

    @org.junit.Test
    public void validerDepartementTest2() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerDepartement",
                "maximum_type_departement_erreur.json","validerDepartement");
    }

    @org.junit.Test
    public void validerNombreEmployeTest1() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerNombreEmploye",
                "maximum_nombre_employe_erreur.json","validerNombreEmploye");
    }

    @org.junit.Test
    public void validerNombreEmployeTest2() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerNombreEmploye",
                "minimum_nombre_employe_erreur.json","validerNombreEmploye");
    }


    @org.junit.Test
    public void validerMontantArgentTest1() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerMontantArgent",
                "taux_min_erreur.json","validerTauxHorairesPositifs");
    }

    @org.junit.Test
    public void validerMontantArgentTest2() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePrivee("json/testJSONEntree/validerMontantArgent",
                "taux_max_erreur.json","validerTauxHorairesPositifs");
    }

    @org.junit.Test
    public void validerChargeTravailTest1() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerChargeTravail",
                "charge_travail_maximum_erreur.json","validerChargeTravail");
    }

    @org.junit.Test
    public void validerChargeTravailTest2() throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerChargeTravail",
                "charge_travail_minimum_erreur.json","validerChargeTravail");
    }


    @org.junit.Test
    public void validerDateTest1 ()throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDate",
                "annee_date_erreur.json","validerDate");
    }

    @org.junit.Test
    public void validerDateTest2 ()throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDate",
                "jour_date_erreur.json","validerDate");
    }

    @org.junit.Test
    public void validerDateTest3 ()throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDate",
                "mois_date_erreur.json","validerDate");
    }

    @org.junit.Test
    public void validerDateTest4 ()throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerDate",
                "date_avec_slash_erreur.json","validerDate");
    }

    @org.junit.Test
    public void validerNomsCleUniqueTest ()throws Exception{

        exception.expectCause(isA(CustomException.class));
        accederUneMethodePriveeAvecArgumentEmployes("json/testJSONEntree/validerNomsCleUnique",
                "nom_cle_unique_erreur.json","validerNomsCleUnique");
    }
}



