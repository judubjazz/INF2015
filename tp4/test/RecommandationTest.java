import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RecommandationTest {

    // Constantes
    final static String RECOMMAMDE_DATE_REVISION_SALAIRE = "L’écart maximal entre les dates de révision de " +
            "salaire des employés d’un même département devrait être de moins de 6 mois";
    final static String RECOMMAMDE_TAUX_HORAIRE = "Le taux horaire maximum ne devrait pas dépasser deux " +
            "fois le taux horaire minimum.";
    final static String RECOMMANDE_RENTE_FEDERALE = "La rente federale payable par l’entreprise nécessite " +
            "deux versements.";
    final static String RECOMMANDE_RENTE_PROVINCIALE = "La rente provinciale payable par l’entreprise " +
            "nécessite deux versements.";
    final static String RECOMMANDE_VALEUR_TOTALE = "La valeur salariale totale ne devrait pas dépasser " +
            "500 000.00 $.";

    @Test
    public void priveAjouterRecommandation() throws Exception {

        String[] args = {"testRecommandation/entree3.json", "testRecommandation/resultats3.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats3.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertEquals(1,listeRecommandation.size());


    }

    @Test
    public void priveVerifierDateRevisionSalaire() throws Exception {
        String[] args = {"testRecommandation/entree1.json", "testRecommandation/resultats.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains(RECOMMAMDE_DATE_REVISION_SALAIRE));
    }

    @Test
    public void priveVerifierTauxHoraire() throws Exception {
        String[] args = {"testRecommandation/entree1.json", "testRecommandation/resultats.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains(RECOMMAMDE_TAUX_HORAIRE));
    }

    @Test
    public void priveVerifierChargeTravailEmploye() throws Exception {

        String[] args = {"testRecommandation/entree1.json", "testRecommandation/resultats.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains("La charge de travail de l'employé Maxime Allie est inférieure à 500 heures"));
    }

    @Test
    public void priveVerifierSalaires() throws Exception {
        String[] args = {"testRecommandation/entree1.json", "testRecommandation/resultats.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains("L'employé Richard Lamoureux est trop coûteux."));
    }

    @Test
    public void priveVerifierRenteFederale() throws Exception {
        String[] args = {"testRecommandation/entree2.json", "testRecommandation/resultats2.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats2.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains(RECOMMANDE_RENTE_FEDERALE));
    }

    @Test
    public void priveVerifierRenteProvinciale() throws Exception {
        String[] args = {"testRecommandation/entree2.json", "testRecommandation/resultats2.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats2.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains(RECOMMANDE_RENTE_PROVINCIALE));
    }

    @Test
    public void priveVerifierValeurTotale() throws Exception {
        String[] args = {"testRecommandation/entree2.json", "testRecommandation/resultats2.json"};
        CashFlow.main(args);

        JSONObject resultats = JSONObject.fromObject(Utilitaires.createStringFromFileContent
                ("json/testRecommandation","resultats2.json"));

        JSONArray listeRecommandation = resultats.getJSONArray("Recommandations");

        assertTrue(listeRecommandation.contains(RECOMMANDE_VALEUR_TOTALE));
    }

}