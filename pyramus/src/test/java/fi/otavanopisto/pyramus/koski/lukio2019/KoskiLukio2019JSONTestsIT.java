package fi.otavanopisto.pyramus.koski.lukio2019;

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
import fi.otavanopisto.pyramus.koski.koodisto.Kielivalikoima;
import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;
import fi.otavanopisto.pyramus.koski.koodisto.ModuuliKoodistoLOPS2021;
import fi.otavanopisto.pyramus.koski.koodisto.OpintojenLaajuusYksikko;
import fi.otavanopisto.pyramus.koski.model.HenkiloTiedotJaOID;
import fi.otavanopisto.pyramus.koski.model.Oppija;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionOpiskeluoikeus;
import fi.otavanopisto.pyramus.koski.model.lukio.LukionSuoritus;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonSuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnisteMuuModuuli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOpintojaksonTunnisteVierasKieli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritus2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusMuuValtakunnallinen2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppiaineenSuoritusVierasKieli2019;
import fi.otavanopisto.pyramus.koski.model.lukio.ops2019.LukionOppimaaranSuoritus2019;

public class KoskiLukio2019JSONTestsIT extends AbstractKoskiTest {

  @Test
  public void testOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppimaara2019Data.getTestStudentMinimal());
  }

  @Test
  public void testOppimaara() throws IOException {
    assertOppija(LukioOppimaara2019Data.getTestStudent());
  }
  
  @Test
  public void testOppiaineenOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppiaineenOppimaara2019Data.getTestStudentMinimal());
  }

  @Test
  public void testOppiaineenOppimaara() throws IOException {
    assertOppija(LukioOppiaineenOppimaara2019Data.getTestStudent());
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
      
      assertEquals("1.2.246.562.24.00000000118", henkilo.getOid());
      assertEquals("050200A0138", henkilo.getHetu());
      assertEquals("Oskari 2019", henkilo.getEtunimet());
      assertEquals("Opiskelija 2019", henkilo.getSukunimi());
      assertEquals("Oskari", henkilo.getKutsumanimi());
    }
    
    /**
     * Opiskeluoikeus
     */
    assertEquals(1, oppija.getOpiskeluoikeudet().size());
    assertTrue(oppija.getOpiskeluoikeudet().get(0) instanceof LukionOpiskeluoikeus);
    
    if (oppija.getOpiskeluoikeudet().get(0) instanceof LukionOpiskeluoikeus) {
      LukionOpiskeluoikeus opiskeluoikeus = (LukionOpiskeluoikeus) oppija.getOpiskeluoikeudet().get(0);
      
      assertEquals(1, opiskeluoikeus.getTila().getOpiskeluoikeusjaksot().size());
      assertEquals(1, opiskeluoikeus.getSuoritukset().size());
      
      LukionSuoritus lukionSuoritus = opiskeluoikeus.getSuoritukset().iterator().next();
      assertTrue(String.format("Deserialization returned wrong type %s, expected %s", lukionSuoritus.getClass().getSimpleName(), LukionOppimaaranSuoritus2019.class.getSimpleName()), 
          lukionSuoritus instanceof LukionOppimaaranSuoritus2019);
      
      LukionOppimaaranSuoritus2019 lukionOppimaaranSuoritus2019 = (LukionOppimaaranSuoritus2019) lukionSuoritus;
      
      assertEquals(4, lukionOppimaaranSuoritus2019.getOsasuoritukset().size());
      
      // KE
      
      LukionOppiaineenSuoritus2019 kemia = lukionOppimaaranSuoritus2019.getOsasuoritukset().stream()
          .filter(lo2019 -> lo2019 instanceof LukionOppiaineenSuoritus2019)
          .map(lo2019 -> (LukionOppiaineenSuoritus2019) lo2019)
          .filter(los2019 -> los2019.getKoulutusmoduuli() instanceof LukionOppiaineenSuoritusMuuValtakunnallinen2019)
          .filter(los2019 -> ((LukionOppiaineenSuoritusMuuValtakunnallinen2019) los2019.getKoulutusmoduuli()).getTunniste().getValue() == KoskiOppiaineetYleissivistava.KE)
          .findFirst()
          .orElse(null);

      assertNotNull("KE not found", kemia);
      
      LukionOpintojaksonSuoritus2019 ke1 = kemia.getOsasuoritukset().get(0);
      
      assertTrue(ke1.getKoulutusmoduuli() instanceof LukionOpintojaksonTunnisteMuuModuuli2019);
      
      LukionOpintojaksonTunnisteMuuModuuli2019 moduuli = (LukionOpintojaksonTunnisteMuuModuuli2019) ke1.getKoulutusmoduuli();
      
      assertNotNull(moduuli.getTunniste());
      assertEquals(ModuuliKoodistoLOPS2021.KE1, moduuli.getTunniste().getValue());
      assertTrue(moduuli.isPakollinen());
      assertEquals(10, moduuli.getLaajuus().getArvo());
      assertEquals(OpintojenLaajuusYksikko.op, moduuli.getLaajuus().getYksikko().getValue());
      
      // ENA

      LukionOppiaineenSuoritus2019 ena = lukionOppimaaranSuoritus2019.getOsasuoritukset().stream()
          .filter(lo2019 -> lo2019 instanceof LukionOppiaineenSuoritus2019)
          .map(lo2019 -> (LukionOppiaineenSuoritus2019) lo2019)
          .filter(los2019 -> los2019.getKoulutusmoduuli() instanceof LukionOppiaineenSuoritusVierasKieli2019)
          .filter(los2019 -> ((LukionOppiaineenSuoritusVierasKieli2019) los2019.getKoulutusmoduuli()).getTunniste().getValue() == KoskiOppiaineetYleissivistava.A)
          .findFirst()
          .orElse(null);

      assertNotNull("ENA not found", ena);

      LukionOppiaineenSuoritusVierasKieli2019 ena_ainetunniste = (LukionOppiaineenSuoritusVierasKieli2019) ena.getKoulutusmoduuli();

      assertNotNull(ena_ainetunniste.getKieli());
      assertEquals(Kielivalikoima.EN, ena_ainetunniste.getKieli().getValue());
      
      LukionOpintojaksonSuoritus2019 ena1 = ena.getOsasuoritukset().get(0);
      
      assertTrue(ena1.getKoulutusmoduuli() instanceof LukionOpintojaksonTunnisteVierasKieli2019);
      
      LukionOpintojaksonTunnisteVierasKieli2019 ena1_tunniste = (LukionOpintojaksonTunnisteVierasKieli2019) ena1.getKoulutusmoduuli();
      
      assertNotNull(ena1_tunniste.getTunniste());
      assertEquals(ModuuliKoodistoLOPS2021.ENA1, ena1_tunniste.getTunniste().getValue());
      assertTrue(ena1_tunniste.isPakollinen());
      assertEquals(38, ena1_tunniste.getLaajuus().getArvo());
      assertEquals(OpintojenLaajuusYksikko.op, ena1_tunniste.getLaajuus().getYksikko().getValue());
      
      // TODO: check all the rest million properties
    }
  }
  
  private String getTestStudentJSON() throws IOException {
    URL testFile = this.getClass().getResource("test-student-lukio2019.json");
    return IOUtils.toString(testFile, "UTF-8");
  }
  
}
