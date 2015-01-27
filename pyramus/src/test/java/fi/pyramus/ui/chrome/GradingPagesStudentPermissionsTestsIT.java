package fi.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.pyramus.ui.base.GradingPagesStudentPermissionsTestsBase;
import fi.pyramus.ui.base.SystemPagesStudentPermissionsTestsBase;

public class GradingPagesStudentPermissionsTestsIT extends GradingPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(new ChromeDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
