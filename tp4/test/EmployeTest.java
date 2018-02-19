import static org.junit.Assert.*;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class EmployeTest {
    public static Employe e1;
    public static Employe e2;
    public static Employe e3;
    public static JSONObject o1;
    public static JSONObject o2;
    public static JSONObject o3;
    private final int NATIONAL = 0;
    private final int REGIONAL = 1;
    private final int INTERNATIONAL = 2;
    public double TAUX_HORAIRE_MIN = 10.00;
    public double TAUX_HORAIRE_MAX = 10.00;
    public double TAUX_HORAIRE_MOY = (TAUX_HORAIRE_MAX + TAUX_HORAIRE_MIN)/2;


    @BeforeClass
    public static void beforeClass() throws Exception {

        o1 = new JSONObject();
        o2 = new JSONObject();
        o3 = new JSONObject();

        o1.accumulate("nom","Stevie Wonder");
        o1.accumulate("nombre_droit_anciennete",1);
        o1.accumulate("nombre_diplomes", 10);
        o1.accumulate("charge_travail", 1000);
        o1.accumulate("date_revision_salaire", "2017-04-17");
        o2.accumulate("nom","Miles Davis");
        o2.accumulate("nombre_droit_anciennete",501);
        o2.accumulate("nombre_diplomes", 1);
        o2.accumulate("charge_travail", 2000);
        o2.accumulate("date_revision_salaire", "2017-06-27");
        o3.accumulate("nom","Charlie Parker");
        o3.accumulate("nombre_droit_anciennete",1001);
        o3.accumulate("nombre_diplomes", 0);
        o3.accumulate("charge_travail", 3000);
        o3.accumulate("date_revision_salaire", "2016-12-07");

        e1 = new Employe(o1);
        e2 = new Employe(o2);
        e3 = new Employe(o3);
    }

    @Before
    public void before(){
        e1.setSalaire(0);
        e2.setSalaire(0);
        e3.setSalaire(0);
    }

    private Method accederUneMethodesPrivees (String nomDeLaMethode) throws Exception{
        Method method = Employe.class.getDeclaredMethod(nomDeLaMethode);
        method.setAccessible(true);
        return method;
    }

    //TEST DES RÉSULTATS
    @org.junit.Test
    public void calculerSalaireTestNationale() throws Exception {
        Method calculerSalaireNational = accederUneMethodesPrivees("calculerSalaireNational");
        e1.setSalaire(e1.getChargeTravail() * TAUX_HORAIRE_MIN);
        calculerSalaireNational.invoke(e1);
        Double salaireNational = e1.getSalaire();

        e1.calculerSalaire(NATIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireNational, e1.getSalaire(),0.00);
    }

    @org.junit.Test
    public void calculerSalaireTestRegional() throws Exception {

        Method calculerSalaireRegional = accederUneMethodesPrivees("calculerSalaireRegional");

        e1.setSalaire(e1.getChargeTravail() * TAUX_HORAIRE_MOY);
        calculerSalaireRegional.invoke(e1);
        Double salaireRegional = e1.getSalaire();
        e1.calculerSalaire(REGIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireRegional, e1.getSalaire(),0.00);

        e2.setSalaire(e2.getChargeTravail() * TAUX_HORAIRE_MOY);
        calculerSalaireRegional.invoke(e2);
        salaireRegional = e2.getSalaire();
        e2.calculerSalaire(REGIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireRegional, e2.getSalaire(),0.00);

        e3.setSalaire(e3.getChargeTravail() * TAUX_HORAIRE_MOY);
        calculerSalaireRegional.invoke(e3);
        salaireRegional = e3.getSalaire();
        e3.calculerSalaire(REGIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireRegional, e3.getSalaire(),0.00);
    }

    @org.junit.Test
    public void calculerSalaireTestInternational() throws Exception {

        //case 3 salaire international
        Method calculerSalaireInternational= accederUneMethodesPrivees("calculerSalaireInternational");


        e1.setSalaire(e1.getChargeTravail() * TAUX_HORAIRE_MAX);
        calculerSalaireInternational.invoke(e1);
        Double salaireInternational = e1.getSalaire();
        e1.calculerSalaire(INTERNATIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireInternational, e1.getSalaire(),0.00);

        e2.setSalaire(e2.getChargeTravail() * TAUX_HORAIRE_MAX);
        calculerSalaireInternational.invoke(e2);
        salaireInternational = e2.getSalaire();
        e2.calculerSalaire(INTERNATIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireInternational, e2.getSalaire(),0.00);

        e3.setSalaire(e3.getChargeTravail() * TAUX_HORAIRE_MAX);
        calculerSalaireInternational.invoke(e3);
        salaireInternational = e3.getSalaire();
        e3.calculerSalaire(INTERNATIONAL,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        assertEquals(salaireInternational, e3.getSalaire(),0.00);
    }

    @org.junit.Test
    public void calculerSalaireDefaultTest() throws Exception {

        //case 4 default
        e1.calculerSalaire(3,TAUX_HORAIRE_MAX, TAUX_HORAIRE_MIN);
        double salaire = e1.getSalaire();
        assertEquals(0.00, salaire,0.00);
    }

    @org.junit.Test
    public void calculerSalaireNationalTest() throws Exception{
        Method calculerSalaireNational = accederUneMethodesPrivees("calculerSalaireNational");
        calculerSalaireNational.invoke(e1);
        Double salaireNational = e1.getSalaire();
        assertEquals(-5000, salaireNational, 0.00);
    }

    @org.junit.Test
    public void calculerSalaireRegionalTest() throws Exception {

        Method calculerSalaireRegional = accederUneMethodesPrivees("calculerSalaireRegional");
        calculerSalaireRegional.invoke(e1);
        Double salaireRegional = e1.getSalaire();
        assertEquals(1000, salaireRegional, 0.00);

        calculerSalaireRegional.invoke(e2);
        salaireRegional = e2.getSalaire();
        assertEquals(-2000, salaireRegional, 0.00);

        calculerSalaireRegional.invoke(e3);
        salaireRegional = e3.getSalaire();
        assertEquals(-3000, salaireRegional, 0.00);
    }

    @org.junit.Test
    public void calculerSalaireInternationalTest () throws Exception{

        Method calculerSalaireInternational = accederUneMethodesPrivees("calculerSalaireInternational");
        calculerSalaireInternational.invoke(e1);
        Double salaireInternational = e1.getSalaire();
        assertEquals(0, salaireInternational, 0.00);

        calculerSalaireInternational.invoke(e2);
        salaireInternational = e2.getSalaire();
        assertEquals(-500, salaireInternational, 0.00);

        calculerSalaireInternational.invoke(e3);
        salaireInternational = e3.getSalaire();
        assertEquals(-2000, salaireInternational, 0.00);
    }

    @org.junit.Test
    public void calculerMontantDiplomeTest () throws Exception{
        Method calculerMontantDiplome = Employe.class.getDeclaredMethod("calculerMontantDiplome");
        calculerMontantDiplome.setAccessible(true);
        Double montantDiplome = (Double) calculerMontantDiplome.invoke(e1);
        assertEquals(5000.00, montantDiplome, 0.00);

        montantDiplome = (Double) calculerMontantDiplome.invoke(e3);
        assertEquals(3000.00, montantDiplome, 0.00);
    }
}

