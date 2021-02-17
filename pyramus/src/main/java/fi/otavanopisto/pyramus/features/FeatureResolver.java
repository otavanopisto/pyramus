package fi.otavanopisto.pyramus.features;

import fi.internetix.smvc.Feature;
import fi.internetix.smvc.controllers.RequestContext;

public interface FeatureResolver {

  public Feature resolve(RequestContext requestContext);
  
}
