package fi.otavanopisto.pyramus.rest.security;

import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit.Handling;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.security.ContextReference;
import fi.otavanopisto.security.Identity;

/**
 * RESTSecurity is essentially a copy of PermitInterceptor in muikku.security. But as CDI Interceptor
 * doesn't for some reason work in REST endpoints we have to manually check for rights which sucks ass.
 */
@Dependent
public class RESTSecurity {
  
  @Inject
  private Logger logger;
  
  @Inject
  private SessionController sessionController;
  
  @Inject
  private Instance<Identity> identityInstance;
  
  /**
   * RESTPermit version of hasPermission with method DOES NOT SUPPORT CONTEXTREFERENCE.
   * If you need to supply ContextReference for a permission, use other hasPermission functions.
   * 
   * Returns true if RESTPermit is not set for method.
   * 
   * @param method
   * @return
   */
  public boolean hasPermission(Method method) {
    RESTPermit permit = method.getAnnotation(RESTPermit.class);

    if (permit != null) {
      // Inline checks are handled in the rest endpoint code so they are skipped here. 
      if (permit.handling() == Handling.INLINE)
        return true;
      
      String[] permissions = permit.value();
      RESTPermit.Style style = permit.style();
      ContextReference permitContext = null;
      
      return hasPermission(permissions, permitContext, style);
    } else
      return false;
  }

  public boolean hasPermission(String[] permissions) {
    return hasPermission(permissions, null, RESTPermit.Style.OR);
  }

  public boolean hasPermission(String[] permissions, ContextReference permitContext) {
    return hasPermission(permissions, permitContext, RESTPermit.Style.OR);
  }
  
  public boolean hasPermission(String[] permissions, ContextReference permitContext, RESTPermit.Style style) {
    boolean permitted = false;

    if (identityInstance.isUnsatisfied())
      throw new RuntimeException("PermitInterceptor - Identity bean unavailable");
    if (identityInstance.isAmbiguous())
      throw new RuntimeException("PermitInterceptor - Identity bean is ambiguous");
    
    Identity identity = identityInstance.get();
    
    switch (style) {
      case OR:
        // For or we break when permit is true
        for (String permission : permissions) {
          if (identity.hasPermission(permission, permitContext)) {
            permitted = true;
            break;
          }
        }
      break;
      
      case AND:
        // And is true by default (as long as at least one permission exists) and breaks if permit hits false
        permitted = permissions.length > 0;
        for (String permission : permissions) {
          if (!identity.hasPermission(permission, permitContext)) {
            permitted = false;
            break;
          }
        }
      break;
    }

    return permitted;
  }

}
