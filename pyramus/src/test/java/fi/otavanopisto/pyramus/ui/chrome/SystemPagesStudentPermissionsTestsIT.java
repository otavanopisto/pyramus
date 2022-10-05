package fi.otavanopisto.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import fi.otavanopisto.pyramus.ui.base.SystemPagesStudentPermissionsTestsBase;

public class SystemPagesStudentPermissionsTestsIT extends SystemPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
