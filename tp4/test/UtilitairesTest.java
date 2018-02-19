import org.junit.Test;

import static org.junit.Assert.*;

public class UtilitairesTest {
    @Test
    public void convertirTauxHoraireEnDoubleTest() throws Exception {
            double tauxHoraire = Utilitaires.convertirTauxHoraireEnDouble("31.45$");
            assertEquals(31.45, tauxHoraire, 0.00);

            tauxHoraire = Utilitaires.convertirTauxHoraireEnDouble("31,45$");
            assertEquals(31.45, tauxHoraire, 0.00);

            tauxHoraire = Utilitaires.convertirTauxHoraireEnDouble("31,45              $");
            assertEquals(31.45, tauxHoraire, 0.00);

            tauxHoraire = Utilitaires.convertirTauxHoraireEnDouble("-31,45$");
            assertEquals(-31.45, tauxHoraire, 0.00);
    }
}