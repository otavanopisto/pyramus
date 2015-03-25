package fi.pyramus.security.impl;

import fi.otavanopisto.security.ContextReference;


public interface ContextResolver {

  boolean handlesContextReference(ContextReference contextReference);
  
}
