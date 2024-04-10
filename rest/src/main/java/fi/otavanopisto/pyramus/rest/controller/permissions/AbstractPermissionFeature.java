package fi.otavanopisto.pyramus.rest.controller.permissions;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.security.impl.UserContextResolver;
import fi.otavanopisto.security.ContextReference;

public abstract class AbstractPermissionFeature {

  @Inject
  @Any
  private Instance<UserContextResolver> userContextResolvers;

  /**
   * Uses ContextResolvers to resolve user from ContextReference
   * 
   * @param contextReference
   * @return user if found, else null
   */
  protected User resolveUser(ContextReference contextReference) {
    for (UserContextResolver resolver : userContextResolvers) {
      if (resolver.handlesContextReference(contextReference))
        return resolver.resolveUser(contextReference);
    }
    
    return null;
  }

}