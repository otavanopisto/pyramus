package fi.otavanopisto.pyramus.ui.base;

import org.junit.jupiter.api.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class IndexPageTestsBase extends AbstractUITest {
  
  @Test
  public void testTitle() {
    testTitle("/", "Pyramus 2010");
  }
  
}
