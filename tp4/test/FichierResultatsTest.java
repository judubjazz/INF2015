import org.junit.Test;

import static org.junit.Assert.*;

public class FichierResultatsTest {
    @Test
    public void ecrireMessageErreur() throws Exception {
        String messageErreur = "ERREUR";
        FichierResultats fr = new FichierResultats(messageErreur);
        fr.ecrire("/testResultats/resultatsErreur.json");

        messageErreur = Utilitaires.createStringFromFileContent("json", "/testResultats/resultatsErreur" +
                ".json");
        assertEquals("{\"Message: \": \"ERREUR\"}", messageErreur);
    }

}