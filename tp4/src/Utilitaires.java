import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Fournis aux autres classe du package des méthodes et des constantes de formatage.
 */

public class Utilitaires {

    // Formatage
    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    final static DecimalFormatSymbols DECIMAL_SEPARATOR = DECIMAL_FORMAT.getDecimalFormatSymbols();

    /**
     * Une methode qui prend le taux horaire en parametre sous forme de string
     * et le retourne en double.
     *
     * @param tauxHoraire : Un string qui représente le taux horaire.
     * @return Un double qui est le taux horaire modifié.
     */
    public static double convertirTauxHoraireEnDouble(String tauxHoraire) {
        //enleve le signe dollar et parse en double
        if (tauxHoraire.contains(",")){
            tauxHoraire = tauxHoraire.replace(",",".");
        }
        tauxHoraire = tauxHoraire.replace(tauxHoraire.substring(tauxHoraire.length() - 1), "");
        return Double.parseDouble(tauxHoraire);
    }

    /**
     * Méthodes de la classe FileManager, indisponible sur Maven.
     * Utilisée pour écrire un fichier à partir d'un object de type String.
     *
     * @param fileDestinationFolder
     * @param fileName
     * @param stringToSave
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void createFileFromStringContent(String fileDestinationFolder, String fileName, String stringToSave) throws FileNotFoundException, IOException {
        File f = new File(fileDestinationFolder + "/" + fileName);
        FileUtils.writeStringToFile(f, stringToSave, "UTF-8");
    }

    /**
     * Méthodes de la classe FileManager, indisponible sur Maven.
     * Utilisée pour lire une String à partir d'un fichier.
     *
     * @param fileLocationFolder
     * @param fileName
     * @return Le contenu du fichier converti en String.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String createStringFromFileContent(String fileLocationFolder, String fileName) throws FileNotFoundException, IOException {
        return IOUtils.toString(new FileInputStream(fileLocationFolder + "/" + fileName), "UTF-8");
    }

}
