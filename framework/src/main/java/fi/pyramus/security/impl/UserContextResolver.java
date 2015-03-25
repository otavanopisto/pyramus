package fi.pyramus.security.impl;

import fi.otavanopisto.security.ContextReference;
import fi.pyramus.domainmodel.users.User;

public interface UserContextResolver extends ContextResolver {

  User resolveUser(ContextReference contextReference);
}
