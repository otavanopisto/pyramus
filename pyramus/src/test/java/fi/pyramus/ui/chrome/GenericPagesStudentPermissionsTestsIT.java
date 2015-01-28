package fi.pyramus.ui.chrome;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;

import fi.pyramus.ui.base.GenericPagesStudentPermissionsTestsBase;

public class GenericPagesStudentPermissionsTestsIT extends GenericPagesStudentPermissionsTestsBase {
  
  @Before
  public void setUp() {
    setWebDriver(new ChromeDriver());
  }
  
  @After
  public void tearDown() {
    getWebDriver().quit();
  }
  
}
