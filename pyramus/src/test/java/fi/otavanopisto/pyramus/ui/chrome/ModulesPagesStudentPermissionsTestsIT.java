package fi.otavanopisto.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import fi.otavanopisto.pyramus.ui.base.ModulesPagesStudentPermissionsTestsBase;

public class ModulesPagesStudentPermissionsTestsIT extends ModulesPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
