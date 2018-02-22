package fi.otavanopisto.pyramus.koski;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;

public class KoskiJSONValidatorTestsIT extends AbstractKoskiTest {

  @Test
  public void testSchemaMatching() throws IOException {
    assertThat(getTestStudentJSON(), getSchemaValidator());
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
      
      assertEquals("1.2.246.562.24.00000000044", henkilo.getOid());
      assertEquals("111111A111C", henkilo.getHetu());
      assertEquals("Lilli", henkilo.getEtunimet());
      assertEquals("Liputin", henkilo.getSukunimi());
      assertEquals("Lilli", henkilo.getKutsumanimi());
    }
    
    /**
     * Opiskeluoikeus
     */
    assertEquals(1, oppija.getOpiskeluoikeudet().size());
    assertTrue(oppija.getOpiskeluoikeudet().get(0) instanceof LukionOpiskeluoikeus);
    
    if (oppija.getOpiskeluoikeudet().get(0) instanceof LukionOpiskeluoikeus) {
      LukionOpiskeluoikeus opiskeluoikeus = (LukionOpiskeluoikeus) oppija.getOpiskeluoikeudet().get(0);
      
      assertEquals(2, opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().size());
      assertEquals(1, opiskeluoikeus.getSuoritukset().size());
      
      assertTrue(opiskeluoikeus.getSuoritukset().iterator().next() instanceof LukionOppimaaranSuoritus);
      
      if (opiskeluoikeus.getSuoritukset().iterator().next() instanceof LukionOppimaaranSuoritus) {
        LukionOppimaaranSuoritus suoritus = (LukionOppimaaranSuoritus) opiskeluoikeus.getSuoritukset().iterator().next();
        
        assertEquals(8, suoritus.getOsasuoritukset().size());
        
        // TODO: check all the rest million properties
      }
    }
  }
  
  private String getTestStudentJSON() throws IOException {
    URL testFile = this.getClass().getResource("test-student.json");
    return IOUtils.toString(testFile, "UTF-8");
  }
}
