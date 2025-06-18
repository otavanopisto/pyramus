package fi.otavanopisto.pyramus.rest.oauth;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

@Path("/authorize")
public class PyramusOAuthAuthorizationService extends AuthorizationCodeGrantService {

  @Inject
  private HttpServletRequest httpServletRequest;
  
  @Inject
  public void init(PyramusOAuthDataProvider provider) {
    setDataProvider(provider);
  }
  
  @Override
  protected MultivaluedMap<String, String> getQueryParameters() {
    return toMultivaluedMap(httpServletRequest.getParameterMap());
  }

  private MultivaluedMap<String, String> toMultivaluedMap(Map<String, String[]> parameterMap) {
    if (parameterMap == null) {
      return null;
    }
    
    MultivaluedMap<String, String> mvmap = new MultivaluedHashMap<>();
    for (Entry<String, String[]> entry : parameterMap.entrySet()) {
      for (String value : entry.getValue()) {
        mvmap.add(entry.getKey(), value);
      }
    }
    
    System.out.println(parameterMap);
    System.out.println(mvmap);
    
    return mvmap;
  }
}
