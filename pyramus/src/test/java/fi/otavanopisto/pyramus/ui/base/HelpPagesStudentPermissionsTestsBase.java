package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class HelpPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testViewHelpPagePermission() throws InterruptedException{
    assertStudentAllowed("/help/viewhelp.page", "#viewHelpNavigationContainer");
  }
  
}
