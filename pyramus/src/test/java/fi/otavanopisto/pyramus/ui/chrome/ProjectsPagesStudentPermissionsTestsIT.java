package fi.otavanopisto.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.otavanopisto.pyramus.ui.base.ProjectsPagesStudentPermissionsTestsBase;

public class ProjectsPagesStudentPermissionsTestsIT extends ProjectsPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(createLocalDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
