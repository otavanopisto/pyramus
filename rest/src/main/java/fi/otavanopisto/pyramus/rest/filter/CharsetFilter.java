package fi.otavanopisto.pyramus.rest.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

@Provider
public class CharsetFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    if (requestContext.getProperty(InputPart.DEFAULT_CHARSET_PROPERTY) == null) {
      requestContext.setProperty(InputPart.DEFAULT_CHARSET_PROPERTY, "UTF-8");
    }
  }

}
