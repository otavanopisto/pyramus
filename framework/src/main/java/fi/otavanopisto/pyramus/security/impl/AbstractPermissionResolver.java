package fi.otavanopisto.pyramus.security.impl;

import javax.ejb.Stateless;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

@Stateless
public abstract class AbstractPermissionResolver {

  @Inject
  @Any
  private Instance<UserContextResolver> userContextResolvers;
  
  @Inject
  @Any
  private Instance<PyramusPermissionCollection> permissionCollections;

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
      if (resolver.handlesContextReference(contextReference)) {
        return resolver.resolveUser(contextReference);
      }
    }
    
    return null;
  }
  
  protected Course resolveCourse(ContextReference contextReference) {
    if (contextReference instanceof Course) {
      return (Course) contextReference;
    }
    
    return null;
  }
  
  protected PyramusPermissionCollection findCollection(String permission) {
    for (PyramusPermissionCollection collection : permissionCollections) {
      if (collection.containsPermission(permission))
        return collection;
    }
    
    return null;
  }
  
}
