package fi.pyramus.I18N;

import java.util.Locale;
import java.util.ResourceBundle;

import org.testng.Assert;
import org.testng.annotations.Test;


import fi.pyramus.I18N.Messages;
import fi.testutils.UnitTest;


public class MessagesTest extends UnitTest {

  private static String parameterlessLocale = "generic.loginLink";
  private static String parameterlessValue  = "Login";
  private static String parameterizedLocale = "generic.errorPage.errorPageTitle";
  private static String parameterValue = "test";
  private static String parameterizedValue  = "Error: " + parameterValue;
    
  @Test
  public void testGetTextLocaleString() {
    Assert.assertEquals(Messages.getInstance().getText(new Locale("en", "US"), parameterlessLocale), parameterlessValue);
  }

  @Test
  public void testGetTextLocaleStringObjectArray() {
    String[] params = {parameterValue};
    Assert.assertEquals(Messages.getInstance().getText(new Locale("en", "US"), parameterizedLocale, params), parameterizedValue);
  }

  @Test
  public void testGetResourceBundle() {
    ResourceBundle fiBundle = Messages.getInstance().getResourceBundle(new Locale("fi_FI"));
    ResourceBundle enBundle = Messages.getInstance().getResourceBundle(new Locale("en_US"));
    Assert.assertNotNull(fiBundle);
    Assert.assertNotNull(enBundle);
    Assert.assertNotSame(fiBundle, enBundle);
  }
}
