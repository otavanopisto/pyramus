package fi.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.security.ContextReference;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;

@Stateless
public abstract class AbstractPermissionResolver {

  @Inject
  @Any
  private Instance<UserContextResolver> userContextResolvers;
  
  protected Role getEveryoneRole() {
    return Role.EVERYONE;
  }
  
  protected User getUser(fi.otavanopisto.security.User user) {
    return (User) user;
  }

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
