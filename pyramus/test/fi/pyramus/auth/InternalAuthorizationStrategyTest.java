package fi.pyramus.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

import fi.pyramus.auth.InternalAuthorizationProvider;
import fi.pyramus.auth.AuthorizationProviders;
import fi.testutils.DatabaseDependingTest;


public class InternalAuthorizationStrategyTest extends DatabaseDependingTest {

  private static String AUTHPROVIDER = "internal";
  
  @Test
  public void testGetName() {
    Assert.assertEquals(authProvider.getName(), AUTHPROVIDER);
  }

  private static InternalAuthorizationProvider authProvider;
  static {
    AuthorizationProviders.getInstance().initializeStrategies();
    authProvider = (InternalAuthorizationProvider) AuthorizationProviders.getInstance().getAuthorizationProvider(AUTHPROVIDER);
  }
  
}
