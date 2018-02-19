import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class DepartementTest {

    public static Employe e1;
    public static Employe e2;
    public static Employe e3;
    public static Departement dep;


    @BeforeClass
    public static void beforeClass() throws Exception {
        e1 = new Employe();
        e2 = new Employe();
        e3 = new Employe();

        e1.setSalaire(75000.00);
        e2.setSalaire(143321.04);
        e3.setSalaire(98765.4321);

        ArrayList<Employe> employes = new ArrayList <>();
        employes.add(e1);
        employes.add(e2);
        employes.add(e3);

        dep = new Departement();
        dep.setEmployes(employes);

    }

    private Method accederUneMethodesPriveesArgJsonObject (String nomDeLaMethode) throws Exception{
        Method method = Departement.class.getDeclaredMethod(nomDeLaMethode, JSONObject.class);
        method.setAccessible(true);
        return method;
    }

    private Method accederUneMethodesPriveesArgJsonArray (String nomDeLaMethode) throws Exception{
        Method method = Departement.class.getDeclaredMethod(nomDeLaMethode, JSONArray.class);
        method.setAccessible(true);
        return method;
    }

    private Method accederUneMethodesPriveeArgDouble (String nomDeLaMethode) throws Exception{
        Method method = Departement.class.getDeclaredMethod(nomDeLaMethode, Double.TYPE);
        method.setAccessible(true);
        return method;
    }


    @Test
    public void calculerValeurTotaleTest() throws Exception {
        dep.calculerValeurTotaleEtRentes();
        assertEquals(326820.15,dep.getValeurTotale(),0);
        assertEquals(40852.50,dep.getRenteFederale(),0);
        assertEquals(23531.05,dep.getRenteProvinciale(),0);
    }

    @Test
    public void extraireTauxHoraireEnDoubleTest() throws Exception {
        String s = Utilitaires.createStringFromFileContent("json","entree.json");
        JSONObject obj = JSONObject.fromObject(s);

        Method extraireTauxHoraireEnDouble = accederUneMethodesPriveesArgJsonObject("extraireTauxHoraireEnDouble");
        extraireTauxHoraireEnDouble.invoke(dep,obj);

        Double taux = dep.getTauxHoraireMax();
        assertEquals(73.0,taux,0);
        taux = dep.getTauxHoraireMin();
        assertEquals(31.45, taux, 0);
    }

    @Test
    public void extraireListeEmployeDuJsonArrayTest() throws Exception {
        String s = Utilitaires.createStringFromFileContent("json","entree.json");
        JSONObject obj = JSONObject.fromObject(s);
        JSONArray array = obj.getJSONArray("employes");

        Method extraireListeEmployeDuJsonArray = accederUneMethodesPriveesArgJsonArray
                ("extraireListeEmployeDuJsonArray");
        extraireListeEmployeDuJsonArray.invoke(dep,array);

        ArrayList<Employe> employes = dep.getEmployes();

        ArrayList<Employe> employes2 = new ArrayList <>();
        for (int i = 0; i < array.size(); i++){
            //Redéfinition de la méthode equals pour comparer deux objets Employe.
            employes2.add(new Employe(array.getJSONObject(i)){
                public boolean equals(Object employe2){
                    return this.getNom().equals(((Employe)employe2).getNom()) &&
                            this.getChargeTravail() == ((Employe)employe2).getChargeTravail() &&
                            this.getDateRevision().isEqual(((Employe)employe2).getDateRevision()) &&
                            this.getNbrDiplomes() ==((Employe)employe2).getNbrDiplomes() &&
                            this.getNbrDroitsAnciennete() == ((Employe)employe2).getNbrDroitsAnciennete();
                }
            });
        }
        assertTrue(employes.containsAll(employes2));
    }

    @Test
    public void arrondirCinqSousLePlusPresTest() throws Exception {
        Method arrondirCinqSousLePlusPres = accederUneMethodesPriveeArgDouble("arrondirCinqSousLePlusPres");
        Double resultat = (Double)arrondirCinqSousLePlusPres.invoke(dep, 100.12);
        assertEquals(100.10,resultat, 0);

    }
}