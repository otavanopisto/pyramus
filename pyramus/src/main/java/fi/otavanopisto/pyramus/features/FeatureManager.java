package fi.otavanopisto.pyramus.features;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import fi.internetix.smvc.Feature;
import fi.internetix.smvc.controllers.RequestContext;

public class FeatureManager {

  public static FeatureManager getInstance() {
    return instance;
  }
  
  @SuppressWarnings("unchecked")
  public void collectFeatures(RequestContext requestContext) {
    HttpSession session = requestContext.getRequest().getSession(false);
    if (session != null) {
      Map<String, Boolean> features = (Map<String, Boolean>) session.getAttribute("loggedUserFeatures");
      if (features == null) {
        features = new HashMap<String, Boolean>();
      }
      for (FeatureResolver featureResolver : featureResolvers) {
        Feature feature = featureResolver.resolve(requestContext);
        if (feature != null) {
          features.put(feature.toString(), Boolean.TRUE);
        }
      }
      session.setAttribute("loggedUserFeatures", features);
    }
  }
  
  public void registerFeatureResolver(FeatureResolver featureResolver) {
    featureResolvers.add(featureResolver);
  }
  
  private static Set<FeatureResolver> featureResolvers = new HashSet<FeatureResolver>();
  private static FeatureManager instance = new FeatureManager(); 

}
