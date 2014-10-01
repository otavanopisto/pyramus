package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class LoginPageTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testLoginPageUsernameElement(){
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    testPageElementsByName("username");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testLoginPagePasswordElement(){
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    testPageElementsByName("password");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testLoginPageLoginbuttonElement(){
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    testPageElementsByName("login");
  }
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testLogin() throws InterruptedException{
    testLogin(ADMIN_USERNAME, ADMIN_PASSWORD);
  }

}
