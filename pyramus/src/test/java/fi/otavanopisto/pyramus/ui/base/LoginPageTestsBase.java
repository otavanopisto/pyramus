package fi.otavanopisto.pyramus.ui.base;

import org.junit.jupiter.api.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class LoginPageTestsBase extends AbstractUITest {

  @Test
  public void testLogin() throws InterruptedException{
    testLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

}
