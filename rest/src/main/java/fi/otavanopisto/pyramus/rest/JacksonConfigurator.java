package fi.otavanopisto.pyramus.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

@Provider
public class JacksonConfigurator implements ContextResolver<ObjectMapper> {

  @Override
  public ObjectMapper getContext(Class<?> type) {
	ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JSR310Module());
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return objectMapper;
  }

}
