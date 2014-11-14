package fi.pyramus.rest;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RESTPermissionsTestsIT extends AbstractRESTPermissionsTest {
  /*
   * This method is called the the JUnit parameterized test runner and returns a
   * Collection of Arrays. For each Array in the Collection, each array element
   * corresponds to a parameter in the constructor.
   */
  @Parameters
  public static Collection<Object[]> generateData() {
    // The parameter generator returns a List of
    // arrays. Each array has two elements: { role, authcode}.
    return Arrays.asList(new Object[][] {
        { "GUEST" },
        { "USER" },
        { "MANAGER" },
        { "ADMINISTRATOR" },
        { "STUDENT" } });
  }

  public RESTPermissionsTestsIT(String role) {
    setRole(role);
  }
  
  
  
}
