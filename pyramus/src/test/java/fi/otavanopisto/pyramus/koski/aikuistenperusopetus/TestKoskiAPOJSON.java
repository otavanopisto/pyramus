package fi.otavanopisto.pyramus.koski.aikuistenperusopetus;

import java.io.IOException;

import org.junit.Test;

import fi.otavanopisto.pyramus.koski.AbstractKoskiTest;

public class TestKoskiAPOJSON extends AbstractKoskiTest {

  @Test
  public void testOppimaaraMinimal() throws IOException {
    assertOppija(APOOppimaaraData.getTestStudentMinimal());
  }

  @Test
  public void testOppiaineenOppimaaraMinimal() throws IOException {
    assertOppija(APOOppiaineenOppimaaraData.getTestStudentMinimal());
  }

}
