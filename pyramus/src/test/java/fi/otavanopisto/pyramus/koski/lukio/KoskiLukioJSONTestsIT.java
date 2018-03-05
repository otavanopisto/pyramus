package fi.otavanopisto.pyramus.koski.lukio;

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
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOppimaaranSuoritus;

public class KoskiLukioJSONTestsIT extends AbstractKoskiTest {

  @Test
  public void testOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppimaaraData.getTestStudentMinimal());
  }

  @Test
  public void testOppimaara() throws IOException {
    assertOppija(LukioOppimaaraData.getTestStudent());
  }
  
  @Test
  public void testOppiaineenOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppiaineenOppimaaraData.getTestStudentMinimal());
  }

  @Test
  public void testOppiaineenOppimaara() throws IOException {
    assertOppija(LukioOppiaineenOppimaaraData.getTestStudent());
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
      
      assertEquals("1.2.246.562.24.00000000010", henkilo.getOid());
      assertEquals("020655-2479", henkilo.getHetu());
      assertEquals("Liisa", henkilo.getEtunimet());
      assertEquals("Lukiolainen", henkilo.getSukunimi());
      assertEquals("Liisa", henkilo.getKutsumanimi());
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
      
      // TODO: check all the rest million properties
    }
  }
  
  private String getTestStudentJSON() throws IOException {
    URL testFile = this.getClass().getResource("test-student-lukio.json");
    return IOUtils.toString(testFile, "UTF-8");
  }
  
}
