package fi.pyramus.rest;

  import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {
  
  @Override
  public ObjectMapper getContext(Class<?> type) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
    return objectMapper;
  }

}
