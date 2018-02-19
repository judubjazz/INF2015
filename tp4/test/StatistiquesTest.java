import net.sf.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import java.lang.reflect.Method;
import static org.junit.Assert.*;

public class StatistiquesTest {

    Statistiques stats = new Statistiques();

    @Rule
    public final ExpectedException exception = ExpectedException.none();


    private Method accederUneMethodePrivee(String nomDeLaMethode) throws NoSuchMethodException {
        Method method = Statistiques.class.getDeclaredMethod(nomDeLaMethode);
        method.setAccessible(true);
        return method;
    }


    @Test
    public void calculerNouveauxStatistiquesNewArrayTest() throws Exception {
        Statistiques stats = new Statistiques();
        assertTrue(stats.getNbrEmployesParTypeDepartement() == null);
        stats.importer();
        assertTrue(stats.getNbrEmployesParTypeDepartement() != null);
    }

    @Test
    public void sauvegarder() throws Exception {
    }

    @Test
    public void formatterJsonArrayVideEnStringTest() throws Exception {
        Method formatterJsonArrayEnString = accederUneMethodePrivee("formatterJsonArrayEnString");
        Method reinitaliserStatistiques = accederUneMethodePrivee("reinitialiserStatistques");
        reinitaliserStatistiques.invoke(stats);
        String array = (String) formatterJsonArrayEnString.invoke(stats);
        assertEquals("Nombre d'employés par département: VIDE", array);
    }

    private Departement creerDepartement() throws Exception {
        Method reinitaliserStatistiques = accederUneMethodePrivee("reinitialiserStatistques");
        reinitaliserStatistiques.invoke(stats);
        String s = Utilitaires.createStringFromFileContent("json/testStatistiques", "entree.json");
        JSONObject objet = JSONObject.fromObject(s);
        return new Departement(objet);
    }

    @Test
    public void formatterJsonArrayEnStringTest() throws Exception {
        Method formatterJsonArrayEnString = accederUneMethodePrivee("formatterJsonArrayEnString");

        Departement dep = creerDepartement();
        stats.calculerNouveauxStatistiques(dep);
        String array = (String) formatterJsonArrayEnString.invoke(stats);
        assertEquals("Nombre d'employés par département: \n" +
                "\t\t1.Informatique: 3\n", array);
    }


    @Test
    public void formatterStatistiquesEnStringTest() throws Exception {
        Method formatterStatistiquesEnString = accederUneMethodePrivee("formatterStatistiquesEnString");
        Departement dep = creerDepartement();
        stats.calculerNouveauxStatistiques(dep);
        String statistiques = (String) formatterStatistiquesEnString.invoke(stats);
        assertEquals("------------\n" +
                "STATISTIQUES\n" +
                "------------\n" +
                "Nombre d'employés total: 3\n" +
                "Nombre d'employés avec un salaire inférieur à 50 000$: 0\n" +
                "Nombre d'employés avec un salaire entre 50 000$ et 100 000$: 2\n" +
                "Nombre d'employés avec un salaire supérieur à 100 000$ : 1\n" +
                "Nombre d'employés par département: \n" +
                "\t\t1.Informatique: 3\n" +
                "Charge de travail maximale: 1203\n" +
                "Valeur salariale minimale: 0,00 $\n" +
                "Valeur salariale maximale 0,00 $",statistiques);
    }

    @Test
    public void reinitialiser() throws Exception {
    }

}