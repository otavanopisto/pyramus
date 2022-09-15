package fi.otavanopisto.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import fi.otavanopisto.pyramus.ui.base.SettingsPagesStudentPermissionsTestsBase;

public class SettingsPagesStudentPermissionsTestsIT extends SettingsPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
