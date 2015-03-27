package fi.pyramus.security.impl;

import fi.otavanopisto.security.ContextReference;
import fi.pyramus.domainmodel.users.User;

public class UserEntityContextResolverImpl implements UserContextResolver {

  @Override
  public boolean handlesContextReference(ContextReference contextReference) {
    return 
        User.class.isInstance(contextReference);
  }

  @Override
  public User resolveUser(ContextReference contextReference) {
    return (User) contextReference;
  }
  
}
