package fi.pyramus.security.impl;

import fi.muikku.security.ContextReference;


public interface ContextResolver {

  boolean handlesContextReference(ContextReference contextReference);
  
}
