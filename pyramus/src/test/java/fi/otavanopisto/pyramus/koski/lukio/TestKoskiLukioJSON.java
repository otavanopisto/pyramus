package fi.otavanopisto.pyramus.koski.lukio;

import java.io.IOException;

import org.junit.Test;

import fi.otavanopisto.pyramus.koski.AbstractKoskiTest;

public class TestKoskiLukioJSON extends AbstractKoskiTest {

  @Test
  public void testOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppimaaraData.getTestStudentMinimal());
  }

  @Test
  public void testOppiaineenOppimaaraMinimal() throws IOException {
    assertOppija(LukioOppiaineenOppimaaraData.getTestStudentMinimal());
  }
}
