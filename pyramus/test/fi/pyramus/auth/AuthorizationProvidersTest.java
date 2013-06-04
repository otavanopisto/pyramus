package fi.pyramus.auth;

import org.testng.Assert;
import org.testng.annotations.Test;

import fi.pyramus.auth.InternalAuthorizationProvider;
import fi.pyramus.auth.AuthorizationProviders;

public class AuthorizationProvidersTest {

  @Test
  public void testGetInstance() {
    Assert.assertNotNull(AuthorizationProviders.getInstance());
    Assert.assertTrue(AuthorizationProviders.getInstance() instanceof AuthorizationProviders);
    AuthorizationProviders instance = AuthorizationProviders.getInstance();
    Assert.assertEquals(AuthorizationProviders.getInstance(), instance);
  }

  @Test
    public void testGetAuthorizationProviders() {
      Assert.assertNotNull(AuthorizationProviders.getInstance().getAuthorizationProviders());
      AuthorizationProviders.getInstance().initializeStrategies();
      Assert.assertTrue(AuthorizationProviders.getInstance().getAuthorizationProviders().size() > 0);
    }

  @Test
    public void testGetAuthorizationProvider() {
      InternalAuthorizationProvider authProvider = (InternalAuthorizationProvider) AuthorizationProviders.getInstance().getAuthorizationProvider("internal");
      Assert.assertNotNull(authProvider);
      Assert.assertEquals(authProvider.getName(), "internal");
    }

}
