package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

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
