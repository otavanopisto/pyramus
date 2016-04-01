package fi.otavanopisto.pyramus.security.impl;

import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.security.ContextReference;

public interface UserContextResolver extends ContextResolver {

  User resolveUser(ContextReference contextReference);
}
