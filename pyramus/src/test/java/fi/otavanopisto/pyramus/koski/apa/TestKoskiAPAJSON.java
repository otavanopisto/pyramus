package fi.otavanopisto.pyramus.koski.apa;

import java.io.IOException;

import org.junit.Test;

import fi.otavanopisto.pyramus.koski.AbstractKoskiTest;

public class TestKoskiAPAJSON extends AbstractKoskiTest {

  @Test
  public void testMinimal() throws IOException {
    assertOppija(APAData.getTestStudentMinimal());
  }

  @Test
  public void test() throws IOException {
    assertOppija(APAData.getTestStudent());
  }

}
