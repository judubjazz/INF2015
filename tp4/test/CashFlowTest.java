import static org.junit.Assert.*;
import manage.file.FileManager;
import net.sf.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class CashFlowTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUpStreams() {
        System.setErr(new PrintStream(errContent));
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void cleanUpStreams() {
        System.setErr(null);
        System.setOut(null);
    }

//TEST DES RÉSULTATS
    @Test
    public void mainTest() throws Exception {
        String[] args = {"testMain/entree.json", "resultats.json"};
        CashFlow.main(args);
        JSONObject resultats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                args[1]));
        JSONObject resultatsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/resultats.json"));
        assertEquals(resultatsPrevus, resultats);

    }
    @Test
    public void mainTest1() throws Exception {
        String[] args = {"testMain/entree1.json", "resultats.json"};
        CashFlow.main(args);
        JSONObject resultats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                args[1]));
        JSONObject resultatsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/resultats1.json"));
        assertEquals(resultatsPrevus, resultats);

    }

    @Test
    public void mainTest2() throws Exception {
        String [] args = {"testMain/entree2.json", "resultats.json"};
        CashFlow.main(args);
        JSONObject resultats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                args[1]));
        JSONObject resultatsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/resultats2.json"));
        assertEquals(resultatsPrevus,resultats);

    }

    //TEST STATISTIQUES
    @Test
    public void mainTest3() throws Exception {
        String [] args = {"-SR"};
        CashFlow.main(args);
        String [] args2 = {"testMain/entree.json", "resultats.json"};
        CashFlow.main(args2);
        args2[0] = "testMain/entree1.json";
        CashFlow.main(args2);
        JSONObject stats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "statistiques.json"));
        JSONObject statsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/stats.json"));
        assertEquals(statsPrevus,stats);

    }

    @Test
    public void mainTest4() throws Exception {
        String [] args = {"-SR"};
        CashFlow.main(args);
        String [] args2 = {"testMain/entree.json", "resultats.json"};
        CashFlow.main(args2);
        CashFlow.main(args2);
        JSONObject stats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "statistiques.json"));
        JSONObject statsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/stats1.json"));
        assertEquals(statsPrevus,stats);

    }

    @Test
    public void mainTest5() throws Exception {
        String [] args = {"-SR"};
        CashFlow.main(args);
        JSONObject stats = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "statistiques.json"));
        JSONObject statsPrevus = JSONObject.fromObject(FileManager.createStringFromFileContent("json",
                "testMain/stats2.json"));
        assertEquals(statsPrevus,stats);
    }


    //TEST EXCEPTIONS
    @Test
    public void MainTest6() throws IOException {
        String [] args = {"testMain/entreeInvalide.json", "resultats.json"};
        CashFlow.main(args);
        String resultats = Utilitaires.createStringFromFileContent("json", "resultats.json");
        assertEquals("{\"Message: \": \"Le format du fichier JSON est incorrect. Assurez vous d'avoir " +
                "toutes les propriétées demandées et qu'elles soient nommées correctement. Tous les noms " +
                "des propriétés doivent être écris obligatoirement en minuscule et ne contenir aucun " +
                "espace\"}", resultats);

    }

    @Test
    public void MainTest7() throws IOException {
        String [] args = {"testMain/inexistant.json", "resultats.json"};
        CashFlow.main(args);
        assertEquals("Le fichier de lecture n'est pas disponible.", errContent.toString());

    }

    @Test
    public void MainTest8() throws IOException {
        String [] args = {"OPTION_INVALIDE"};
        CashFlow.main(args);
        assertEquals("Usage: En mode bash entré la commande: $\"java -jar Sortie.jar " +
                "[fichier d'entré] [fichier de sortie]\" pour exécuter le programme et avoir les résultats " +
                "de l'évaluation salariale du département.\n Utiliser la commande java -jar avec soit les " +
                "options suivantes:\n\t - [-S] pour afficher sur la console les statistiques globales de " +
                "l'entreprise. \n\t - [-SR] pour réinitialiser les statistiques.", errContent.toString());

    }

    @Test
    public void MainTest9(){
        String [] args = {"-SR"};
        CashFlow.main(args);
        args[0] = "-S";
        CashFlow.main(args);
        assertEquals("Réinitialisation des statistiques complétée.\n" +
                "------------\n" +
                "STATISTIQUES\n" +
                "------------\n" +
                "Nombre d'employés total: 0\n" +
                "Nombre d'employés avec un salaire inférieur à 50 000$: 0\n" +
                "Nombre d'employés avec un salaire entre 50 000$ et 100 000$: 0\n" +
                "Nombre d'employés avec un salaire supérieur à 100 000$ : 0\n" +
                "Nombre d'employés par département: VIDECharge de travail maximale: 0\n" +
                "Valeur salariale minimale: 0,00 $\n" +
                "Valeur salariale maximale 0,00 $\n",outContent.toString());
    }
}

