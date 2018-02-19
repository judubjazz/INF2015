/**
 * Programme CashFlow
 *
 * Ce programme a pour but de calculer le salaire d'un groupe d'employés
 * en fonction de certains critères. Le programme prend un fichier JSON
 * en entrée et il en produit un autre après l'exécution.
 */
import java.io.*;

import manage.file.FileManager;
import net.sf.json.JSONObject;

/**
 * PROGRAMME CASHFLOW(MAIN)
 *
 * Ce programme a pour but de dresser un portrait financier de base d'un département incluant
 * la liste des salaires par employé, la valeur salariale totale et le montant des rentes.
 * Le programme donne également des recommandations sur certains critères. Des statistiques sont
 * enregistrés et permettent d'être affichés
 */
/**
 *
 * @author Alexandre Leblanc
 * Code permanent : LEBA11028506
 * Courriel : leblanc.alexandre.8@courrier.uqam.ca
 *
 * @author David Quirion
 * Code permanent : QUID26099001
 * Courriel : quirion.david.2@courrier.uqam.ca
 *
 * @author Julien Guité-Vinet
 * Code permanent : GUIJ09058407
 * Courriel :guite-vinet.julien@courrier.uqam.ca
 */

public class CashFlow {

    /**
     * Importe les statistiques à partir du fichier de sauvegarde s'il existe.
     *
     * @return statistiques:    Un objet Statistique.
     * @throws IOException
     */
    private static Statistiques importerStatistiques() throws IOException {
        Statistiques statistiques = new Statistiques();
        statistiques.importer();

        return statistiques;
    }

    /**
     * Méthode qui exécute le programme principal CashFlow. Elle fait d'abord la lecture du fichier d'entree,
     * le calcul des résultats, l'ajout des recommandations à donner et l'écriture du fichier de sortie.
     * Elle enregistre aussi les statistiques automatiquement.
     *
     * @param args:             arg[0] est le fichier entree.JSON et arg [1] sera le fichier resultats.JSON.
     * @param statistiques:     l'instance des statistiques importer ou nouvellement créée.
     * @throws Exception
     */
    private static void executerCashFlow(String[] args, Statistiques statistiques) throws Exception {

        JSONObject contenuFichierJson = extraireObjetJson(args[0]);
        Validation.valider(contenuFichierJson);

        Departement departement = new Departement(contenuFichierJson);
        departement.calculerValeurTotaleEtRentes();

        Recommandation recommandations = new Recommandation();
        recommandations.donnerRecommandations(departement);

        FichierResultats fichierResultats = new FichierResultats();
        fichierResultats.ajouterContenu(departement, recommandations);
        fichierResultats.ecrire(args[1]);

        statistiques.calculerNouveauxStatistiques(departement);
        statistiques.sauvegarder();
    }

    /**
     * La fonction permet d'extraire un objet JSON d'un fichier.
     *
     * @param nomFichier:       nom du fichier à lire.
     * @return un objet JSON
     * @throw une exception si le fichier n'existe pas ou si le fichier n'est pas de format JSON.
     */
    private static JSONObject extraireObjetJson(String nomFichier) throws IOException {
        try {
            String contenu = Utilitaires.createStringFromFileContent("json", nomFichier);
            return JSONObject.fromObject(contenu);
        } catch (Exception e){
            throw new IOException(CustomException.ERREUR_LECTURE);
        }
    }

    /**
     * Une méthode qui donne des fonctions aux commandes -S et -SR passées en argument.
     * -S   ---> Affiche les statistiques
     * -SR  ---> Réinistilise les statistiques.
     *
     * @param args:             args[0] contenant l'option choisie.
     * @throws IOException:     Exception levée s'il y a un problème dans l'écriture des statistiques
     *                          réinitialisées.
     */
    private static void gererOptions(String [] args, Statistiques statistiques) throws Exception {

        if (args.length == 1 && args[0].equals("-S")) {
            statistiques.afficher();
        } else if (args.length == 1 && args[0].equals("-SR")) {
            statistiques.reinitialiser();
        } else {
            throw new Exception(CustomException.ERREUR_ARGUMENTS);
        }
    }


    public static String lireUnFichier (String dossier, String fichier) throws FileNotFoundException{

        File myFile = new File("./"+dossier+"/"+fichier);
        BufferedReader in = new BufferedReader(new FileReader(myFile));
        String ligne;
        String stringRetour = "";

        try {
            while (in.ready()) {
                ligne = in.readLine();
                stringRetour = stringRetour+ligne+"\n";
            }
            in.close();
        }catch(IOException e) {

        }
        return stringRetour.trim();
    }

    /**
     * Methode MAIN.
     * Importe les statistiques puis valide le nombre d'arguments lors de l'exécution du programme:
     * 2 argument --> Exécution du programme Cashflow.
     * 1 argument --> Selon le choix, l'affichage ou la réinitialisation des statistiques.
     *
     * La MAIN gère aussi l'affichage des exceptions CustomExceptions levées à la console ou dans le fichier
     * de sortie JSON.
     *
     * @param args:     arg[0] est le fichier entree.JSON et arg [1] sera le fichier resultats.JSON. Dans
     *                  le cas d'un argument unique, arg[0] contiendra l'option des statistiques.
     */
    public static void main(String[] args){
        try {
            Statistiques statistiques = importerStatistiques();

            if (args.length == 2) {
                executerCashFlow(args, statistiques);
            } else if (args.length == 1) {
                gererOptions(args, statistiques);
            }

        } catch (CustomException e) { //Attrape les exeptions à afficher dans le fichier résultats.
            FichierResultats fichierResultats = new FichierResultats(e.getMessage());
            fichierResultats.ecrire(args[1]);

        } catch (Exception e) { //Les autres exceptions sont affichés à la console.
            System.err.print(e.getLocalizedMessage());
        }
    }
}
