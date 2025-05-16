package fi.otavanopisto.pyramus.koski.apa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.koski.AbstractKoskiTest;
import fi.otavanopisto.pyramus.koski.koodisto.SuorituksenTyyppi;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.aikuistenperusopetus.AikuistenPerusopetuksenSuoritus;
import fi.otavanopisto.pyramus.koski.model.apa.APASuoritus;

public class KoskiAPAJSONTestsIT extends AbstractKoskiTest {

  @Test
  public void testMinimal() throws IOException {
    assertOppija(APAData.getTestStudentMinimal());
  }

  @Test
  public void test() throws IOException {
    assertOppija(APAData.getTestStudent());
  }

  @Test
  public void testDeserialization() throws JsonParseException, JsonMappingException, IOException {
    String testStudent = getTestStudentJSON();
    
    ObjectMapper mapper = new ObjectMapper();
    Oppija oppija = mapper.readValue(testStudent, Oppija.class);
    
    assertNotNull(oppija);
    assertTrue(oppija.getHenkilo() instanceof HenkiloTiedotJaOID);
    
    /**
     * Henkilö
     */
    if (oppija.getHenkilo() instanceof HenkiloTiedotJaOID) {
      HenkiloTiedotJaOID henkilo = (HenkiloTiedotJaOID) oppija.getHenkilo();
      
      assertEquals("1.2.246.562.24.00000000021", henkilo.getOid());
      assertEquals("280598-2415", henkilo.getHetu());
      assertEquals("Aini", henkilo.getEtunimet());
      assertEquals("Aikuisopiskelija", henkilo.getSukunimi());
      assertEquals("Aini", henkilo.getKutsumanimi());
    }
    
    /**
     * Opiskeluoikeus
     */
    assertEquals(1, oppija.getOpiskeluoikeudet().size());
    assertTrue(oppija.getOpiskeluoikeudet().get(0) instanceof AikuistenPerusopetuksenOpiskeluoikeus);
    
    if (oppija.getOpiskeluoikeudet().get(0) instanceof AikuistenPerusopetuksenOpiskeluoikeus) {
      AikuistenPerusopetuksenOpiskeluoikeus opiskeluoikeus = (AikuistenPerusopetuksenOpiskeluoikeus) oppija.getOpiskeluoikeudet().get(0);
      
      assertEquals(2, opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().size());
      assertEquals(2, opiskeluoikeus.getSuoritukset().size());
      
      /**
       * APA
       */
      AikuistenPerusopetuksenSuoritus suoritus = opiskeluoikeus.getSuoritukset().stream()
          .filter(apoSuoritus -> apoSuoritus.getTyyppi().getValue() == SuorituksenTyyppi.aikuistenperusopetuksenoppimaaranalkuvaihe)
          .findFirst().orElse(null);
      
      assertTrue(suoritus instanceof APASuoritus);
      
      if (suoritus instanceof APASuoritus) {
        APASuoritus apaSuoritus = (APASuoritus) suoritus;
        
        assertEquals(7, apaSuoritus.getOsasuoritukset().size());
        
        // TODO: check all the rest million properties
      }
    }
  }
  
  private String getTestStudentJSON() throws IOException {
    URL testFile = this.getClass().getResource("test-student-apa.json");
    return IOUtils.toString(testFile, "UTF-8");
  }

}
