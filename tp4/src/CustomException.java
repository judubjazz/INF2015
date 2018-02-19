/**
 * Classe CustomException
 * Permet de personnaliser une exception avec un message spécifique qui
 * arrête le programme et qui traite l'exception dans le main pour finir par
 * afficher le message à l'utilisateur dans un fichier de sortie JSON ou à la console.
 */
public class CustomException extends Exception {

    // Messages d'erreur
    final static String ERREUR_DROIT_ANCIENNETE = "Le nombre de droits " +
            "d’ancienneté doit être entre 0 et 10 inclusivement.";
    final static String ERREUR_PROPRIETES_FICHIER_JSON = "Le format du " +
            "fichier JSON est incorrect. Assurez vous d'avoir " +
            "toutes les propriétées demandées et qu'elles soient nommées " +
            "correctement. Tous les noms des propriétés doivent être écris " +
            "obligatoirement en minuscule et ne contenir aucun espace";
    final static String ERREUR_NOMBRE_DIPLOME = "Le nombre de diplômes doit être entre 0 et 5 inclusivement.";
    final static String ERREUR_DEPARTEMENT = "Le type de département doit prendre la valeur 0, 1 ou 2.";
    final static String ERREUR_NOMBRE_EMPLOYE = "Le nombre d’employés ne doit jamais dépasser 10 employés.";
    final static String ERREUR_DEPARTEMENT_EMPLOYE = "Un département doit avoir au moins un employé.";
    final static String ERREUR_MONTANT_ARGENT = "Un montant d'argent ne peut pas être négatif";
    final static String ERREUR_FORMAT_MONTANT_ARGENT = "le format du montant d'argent est erroné";
    final static String ERREUR_CHARGE_TRAVAIL = "La charge de travail ne peut " +
            "pas être négative et ne peut pas être supérieure à 1950 heures.";
    final static String ERREUR_DATE = "Le format des dates de révision de " +
            "salaire n'est pas respecté. (ISO 8601)";
    final static String ERREUR_LECTURE = "Le fichier de lecture n'est pas disponible.";
    final static String ERREUR_ECRITURE = "Le fichier d'écriture est impossible a créer.";
    final static String NOM_UNIQUE = "Deux employés ont le même nom.";
    final static String ERREUR_ARGUMENTS = "Usage: En mode bash entré la commande: $\"java -jar Sortie.jar " +
            "[fichier d'entré] [fichier de sortie]\" pour exécuter le programme et avoir les résultats de " +
            "l'évaluation salariale du département.\n Utiliser la commande java -jar avec soit les options " +
            "suivantes:\n\t - [-S] pour afficher sur la console les statistiques globales de l'entreprise. " +
            "\n\t - [-SR] pour réinitialiser les statistiques.";
    final static String ERREUR_SAUVEGARDE_STATISTIQUES = "Une erreur est survenue lors de la sauvegarde des" +
            " statistiques. Le fichier statistiques.json est peut-être inexistant ou introuvable.";
    final static String ERREUR_REINIT_STATISTIQUES = "Une erreur est survenue lors de la réinitialisation " +
            "des statistiques. Le fichier statistiques.json est peut-être inexistant ou introuvable.";

    // Constructeur
    public CustomException(String message) {
        super(message);
    }

}
