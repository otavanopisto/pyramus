package fi.pyramus.ui;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.remote.RemoteWebDriver;

import fi.pyramus.AbstractIntegrationTest;

public class AbstractUITest extends AbstractIntegrationTest {

  protected void setWebDriver(RemoteWebDriver webDriver) {
    this.webDriver = webDriver;
  }
  
  protected RemoteWebDriver getWebDriver() {
    return webDriver;
  }

  protected void testTitle(String path, String expected) {
    getWebDriver().get(getAppUrl(true) + path);
    assertEquals(expected, getWebDriver().getTitle());
  }
  
  private RemoteWebDriver webDriver;
}
