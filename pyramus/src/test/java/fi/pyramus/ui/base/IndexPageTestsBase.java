package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.ui.AbstractUITest;

public class IndexPageTestsBase extends AbstractUITest {
  
  @Test
  public void testTitle() {
    testTitle("/", "Pyramus 2010");
  }
  
}
